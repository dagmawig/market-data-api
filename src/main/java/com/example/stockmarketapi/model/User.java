package com.example.stockmarketapi.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "datas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Getter
    public String userID = "";
    @Getter
    public Watchlist watchlist = new Watchlist();
    @Getter
    public double fund = 10000;
    @Getter
    public Portfolio portfolio = new Portfolio();
    @Getter
    public History history = new History();

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}