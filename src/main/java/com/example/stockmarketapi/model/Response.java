package com.example.stockmarketapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private boolean success;
    private Object data;
    private Object error;
}
