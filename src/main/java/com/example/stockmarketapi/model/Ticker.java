package com.example.stockmarketapi.model;

public class Ticker {
    private final String ticker;
    private String price = null;
    private final String name;
    public Ticker(final String ticker, final String name) {
        this.ticker = ticker;
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "ticker='" + ticker + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
