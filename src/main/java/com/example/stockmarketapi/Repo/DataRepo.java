package com.example.stockmarketapi.Repo;

import com.example.stockmarketapi.model.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepo extends MongoRepository<Data, String> {

}
