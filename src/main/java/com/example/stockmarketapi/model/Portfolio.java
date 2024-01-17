package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Portfolio {
    public ArrayList<String> ticker = new ArrayList<String>();
    public ArrayList<String> price = new ArrayList<String>();
    public ArrayList<Integer> shares = new ArrayList<Integer>();
    public ArrayList<String> averageC = new ArrayList<String>();
}
