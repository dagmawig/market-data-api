package com.example.stockmarketapi.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Document(collection = "datas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public String userID = "";
    public Watchlist watchlist = new Watchlist();
    public double fund = 10000;
    public Portfolio portfolio = new Portfolio();
    public History history = new History();

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}