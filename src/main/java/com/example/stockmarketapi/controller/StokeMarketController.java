package com.example.stockmarketapi.controller;

import com.example.stockmarketapi.Repo.DataRepo;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class StokeMarketController {

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

    @PostMapping("/ticker")
    public String createTicker(@RequestBody final Ticker ticker) {
        System.out.println(ticker.toString());
        return ticker.toString();
    }

    @GetMapping("/price/{ticker}")
    public String getPrice(@PathVariable String ticker) throws InterruptedException, IOException, ExecutionException {

        params.put("token", token);
        params.put("format", format);
        params.put("dateformat", dateformat);
        params.put("symbol_lookup", symbol_lookup);
        params.put("human", human);

        CompletableFuture<HttpResponse<String>> resp = GetService.getPrice(ticker, params);

        return resp.get().body();
    }

    @Autowired
    DataRepo dataRepo;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/addUser")
    public void addUser(@RequestBody Data data) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        Object user = userRepo.findAll().get(0);
        String userJSONString = new Gson().toJson(user);
        User userObj = gson.fromJson(userJSONString, User.class);
        System.out.println(userObj.getHistory().getDate().get(0));
    }

    @PostMapping("/loadData")
    public Object loadData(@RequestBody UserID data) {
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
            resp.setData(user);
            System.out.println("EXISTING USER: " + user.toString());
        }

        return resp;
    }

}
