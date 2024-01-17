package com.example.stockmarketapi.controller;

import com.example.stockmarketapi.Repo.UserRepo;
import com.example.stockmarketapi.model.*;
import com.example.stockmarketapi.service.GetService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class StokeMarketController {

    // stock price request parameters
    @Value("${MARKET_DATA_TOKEN:undefined}")
    private String token;
    @Value("${FORMAT:undefined}")
    private String format;
    @Value("${DATE_FORMAT}")
    private String dateformat;
    @Value("${SYMBOL_LOOKUP}")
    private String symbol_lookup;
    @Value("${HUMAN}")
    private String human;

    final Map<String, Object> params = new HashMap<>();

    // endpoint for stock price request
    @GetMapping("/getPrice/{ticker}")
    public Response getPrice(@PathVariable String ticker) throws InterruptedException, IOException, ExecutionException {

        params.put("token", token);
        params.put("format", format);
        params.put("dateformat", dateformat);
        params.put("symbol_lookup", symbol_lookup);
        params.put("human", human);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        CompletableFuture<HttpResponse<String>> res = GetService.getPrice(ticker, params);

        Response resp = new Response();
        resp.setSuccess(true);
        resp.setData(gson.fromJson(res.get().body(), PriceResp.class));

        return resp;
    }

    @Autowired
    UserRepo userRepo;

    // endpoint to get user profile using user ID
    @PostMapping("/loadData")
    public Response loadData(@RequestBody UserID data) throws ExecutionException, InterruptedException {
        String userID = data.getUserID();
        User user = userRepo.findByUserID(userID);
        Response resp = new Response();
        if (user == null) {
            User newUser = new User();
            newUser.setUserID(userID);
            Object newData = userRepo.save(newUser);
            resp.setSuccess(true);
            resp.setData(newData);
            System.out.println("NEW USER: " + newUser.toString());
        } else {
            resp.setSuccess(true);

            params.put("token", token);
            params.put("format", format);
            params.put("dateformat", dateformat);
            params.put("symbol_lookup", symbol_lookup);
            params.put("human", human);

            int pSize = user.getPortfolio().getTicker().size();
            ArrayList<String> ticArr = new ArrayList<String>();
            ticArr.addAll(user.getPortfolio().getTicker());
            ticArr.addAll(user.getWatchlist().getTicker());

            if (!ticArr.isEmpty()) {
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                ArrayList<CompletableFuture<HttpResponse<String>>> respArr = GetService.getPriceArr(ticArr, params);
                String resp1 = respArr.get(0).get().body();
                List<PriceResp> respObjArr = respArr.stream().map(re -> {
                    try {
                        return gson.fromJson(re.get().body(), PriceResp.class);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
                List<String> priceList = respObjArr.stream().map(respEle -> respEle.getAsk().get(0).toString()).toList();
                ArrayList<String> portfolioPrice = new ArrayList<String>(priceList.subList(0, pSize));
                ArrayList<String> watchlistPrice = new ArrayList<String>(priceList.subList(pSize, respObjArr.size()));
                user.getPortfolio().setPrice(portfolioPrice);
                user.getWatchlist().setPrice(watchlistPrice);
            }

            resp.setData(user);
            System.out.println("EXISTING USER: " + user.toString());

        }

        return resp;
    }

    // endpoint to update stock price watchlist for a given user
    @PostMapping("/updateWatchlist")
    public Response updateWatchlist(@RequestBody WatchlistReq data) {
        String userID = data.getUserID();
        Watchlist watchlist = data.getWatchlist();
        Response resp = new Response();
        resp.setSuccess(true);
        userRepo.updateWatchlist(userID, watchlist);
        resp.setData(userRepo.findByUserID(userID).getWatchlist());
        return resp;
    }

    // endpoint that executes buying shares of a given stock
    @PostMapping("/buyTicker")
    public Response buyTicker(@RequestBody BuyTicReq data) throws IOException, InterruptedException, ExecutionException {
        String userID = data.getUserID();
        String ticker = data.getTicker();
        Integer shares = data.getShares();
        Double limitPrice = data.getLimitPrice();
        User user = userRepo.findByUserID(userID);
        double fund = user.getFund();

        params.put("token", token);
        params.put("format", format);
        params.put("dateformat", dateformat);
        params.put("symbol_lookup", symbol_lookup);
        params.put("human", human);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        CompletableFuture<HttpResponse<String>> res = GetService.getPrice(ticker, params);

        Double price = gson.fromJson(res.get().body(), PriceResp.class).getAsk().get(0);

        Response response = new Response();

        if (limitPrice == null || price <= limitPrice) {
            if (shares * price > fund) {
                response.setSuccess(false);
                response.setError("Your fund of $" + fund + " is less than what is needed to buy " + shares + " shares of " + ticker);
            } else {
                History history = user.getHistory();
                history.getTicker().add(ticker.toUpperCase());
                history.getPrice().add(price.toString());
                history.getShares().add(shares);
                history.getLimit().add(limitPrice != null ? "Limit Buy" : "Market Buy");
                Date date = new Date();
                history.getDate().add(date);
                history.getValue().add(-price * shares);
                fund = fund - (price * shares);

                Portfolio portfolio = user.getPortfolio();
                if (!portfolio.getTicker().contains(ticker.toUpperCase())) {
                    portfolio.getTicker().add(ticker.toUpperCase());
                    portfolio.getShares().add(shares);
                    portfolio.getAverageC().add(price.toString());
                    portfolio.getPrice().add(price.toString());
                    String message = "Success! " + shares + " shares of " + ticker.toUpperCase() + " bought at a price of $" + price + ".";
                } else {
                    int index = portfolio.getTicker().indexOf(ticker.toUpperCase());
                    Integer newShares = portfolio.getShares().get(index) + shares;
                    Double cost = (portfolio.getShares().get(index) * Double.parseDouble(portfolio.getAverageC().get(index)) + (shares * price)) / newShares;
                    portfolio.getShares().set(index, newShares);
                    portfolio.getAverageC().set(index, cost.toString());
                    portfolio.getPrice().set(index, price.toString());
                }

                userRepo.updateBuyTic(userID, portfolio, fund, history);
                user = userRepo.findByUserID(userID);
                response.setSuccess(true);
                response.setData(user);
            }
        } else {
            response.setSuccess(false);
            response.setError("Current price $" + price + " is greater than limit price of $" + limitPrice);
        }

        return response;
    }

    // endpoint that executes selling shares of a given stock
    @PostMapping("/sellTicker")
    public Response sellTicker(@RequestBody SellTicReq data) throws IOException, InterruptedException, ExecutionException {
        String userID = data.getUserID();
        String ticker = data.getTicker();
        Integer shares = data.getShares();
        Double limitOrder = data.getLimitOrder();
        User user = userRepo.findByUserID(userID);
        Double fund = user.getFund();
        params.put("token", token);
        params.put("format", format);
        params.put("dateformat", dateformat);
        params.put("symbol_lookup", symbol_lookup);
        params.put("human", human);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        CompletableFuture<HttpResponse<String>> res = GetService.getPrice(ticker, params);

        Double price = gson.fromJson(res.get().body(), PriceResp.class).getAsk().get(0);

        Response response = new Response();

        if (limitOrder == null || price >= limitOrder) {
            Date date = new Date();
            Portfolio portfolio = user.getPortfolio();
            int index = portfolio.getTicker().indexOf(ticker.toUpperCase());
            Integer currentShares = portfolio.getShares().get(index);
            History history = user.getHistory();
            history.getTicker().add(ticker.toUpperCase());
            history.getPrice().add(price.toString());
            history.getShares().add(Math.min(shares, currentShares));
            history.getValue().add(price * Math.min(shares, currentShares));
            history.getLimit().add(limitOrder != null ? "Limit Sell" : "Market Sell");
            history.getDate().add(date);

            fund += Math.min(shares, currentShares) * price;

            Integer newShares = Math.max(currentShares - shares, 0);

            if (newShares == 0) {
                portfolio.getTicker().remove(index);
                portfolio.getPrice().remove(index);
                portfolio.getShares().remove(index);
                portfolio.getAverageC().remove(index);
            }
            else {
                portfolio.getShares().set(index, newShares);
                portfolio.getPrice().set(index, price.toString());
            }

            userRepo.updateBuyTic(userID, portfolio, fund, history);
            user = userRepo.findByUserID(userID);
            response.setSuccess(true);
            response.setData(user);
        }
        else {
            response.setSuccess(false);
            response.setError("Current price $" + price + " is less than limit order of $" + limitOrder);
        }

        return  response;
    }

}
