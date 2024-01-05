package com.example.stockmarketapi.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_data")
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data {

    @Id
    private String userID;
    private String name;

}
