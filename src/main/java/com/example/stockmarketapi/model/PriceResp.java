package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class PriceResp {
    private ArrayList<String> Symbol = new ArrayList<String>();
    private ArrayList<Double> Ask = new ArrayList<Double>();
    private ArrayList<Double> Bid = new ArrayList<Double>();
    private ArrayList<Double> Mid = new ArrayList<Double>();
    private ArrayList<Double> Last = new ArrayList<Double>();
    private ArrayList<String> Date = new ArrayList<String>();
}
