package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Watchlist {
    @Getter
    @Setter
    public ArrayList<String> ticker = new ArrayList<String>();
    @Getter
    @Setter
    public ArrayList<String> price = new ArrayList<String>();
}
