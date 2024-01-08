package com.example.stockmarketapi.service;


import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetService {

    public static  CompletableFuture<HttpResponse<String>> getPrice(String ticker, Map<String, Object> params) throws IOException, InterruptedException {

        String finalURL = "https://api.marketdata.app/v1/stocks/quotes/" + ticker + "/?format=" +
                params.get("format") + "&dateformat=" + params.get("dateformat") +
                "&symbol_lookup=" + params.get("symbol_lookup") + "&human=" + params.get("human") +
                "&token=" + params.get("token");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(finalURL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());

        return CompletableFuture.completedFuture(response);
    }

    public static ArrayList<CompletableFuture<HttpResponse<String>>> getPriceArr(ArrayList<String> pArray, Map<String, Object> params) {
            List<CompletableFuture<HttpResponse<String>>> temp = pArray.stream().map(ticker -> {
                        try {
                            return getPrice(ticker, params);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).toList();
            return new ArrayList<>(temp);
    }
}
