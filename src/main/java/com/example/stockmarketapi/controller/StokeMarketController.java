package com.example.stockmarketapi.controller;

import com.example.stockmarketapi.Repo.DataRepo;
import com.example.stockmarketapi.Repo.UserRepo;
import com.example.stockmarketapi.model.Data;
import com.example.stockmarketapi.model.Ticker;
import com.example.stockmarketapi.service.GetService;
import com.google.gson.Gson;
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

    final Map<String, Object> params  = new HashMap<>();

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
//        Data ddd = new Data();
//        ddd.setName("Dagggg");
//        ddd.setUserID("ABABABABABA");
        //dataRepo.save(data);
        //System.out.println(new Gson().toJson(dataRepo.findAll()));
        //Gson historyJSON = new Gson().toJson(userRepo.findAll().get(0).getHistory());
        System.out.println(userRepo.findAll().get(0).getHistory());
    }

}
