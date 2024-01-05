package com.example.stockmarketapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "datas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userID;
    private Object watchlist;
    private long fund = 10000;
    private Object portfolio;
    private Object history;

    public String getUserID() {
        return userID;
    }

    public Object getWatchlist() {
        return watchlist;
    }

    public long getFund() {
        return fund;
    }

    public Object getPortfolio() {
        return portfolio;
    }

    public Object getHistory() {
        return history;
    }
}
