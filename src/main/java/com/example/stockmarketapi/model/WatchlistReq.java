package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchlistReq {
    private String userID;
    private Watchlist watchlist;
}
