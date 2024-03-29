package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellTicReq {
    private String userID;
    private String ticker;
    private Integer shares;
    private Double limitOrder;
}
