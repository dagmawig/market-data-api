package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class History {
    public ArrayList<String> ticker = new ArrayList<String>();
    public ArrayList<String> price = new ArrayList<String>();
    public ArrayList<Integer> shares = new ArrayList<Integer>();
    public ArrayList<Double> value = new ArrayList<Double>();
    public ArrayList<String> limit = new ArrayList<String>();
    public ArrayList<Date> date = new ArrayList<Date>();
}
