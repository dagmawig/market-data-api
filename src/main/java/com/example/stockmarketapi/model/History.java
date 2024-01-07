package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Date;

public class History {
    @Getter
    @Setter
    public ArrayList<String> ticker = new ArrayList<String>();
    @Getter
    @Setter
    public ArrayList<String> price = new ArrayList<String>();
    @Getter
    @Setter
    public ArrayList<Integer> shares = new ArrayList<Integer>();
    @Getter
    @Setter
    public ArrayList<Double> value = new ArrayList<Double>();
    @Getter
    @Setter
    public ArrayList<String> limit = new ArrayList<String>();
    @Getter
    @Setter
    public ArrayList<Date> date = new ArrayList<Date>();
}
