package com.example.stockmarketapi.Repo;

import com.example.stockmarketapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("{userID: '?0'}")
    User findByUserID(String userID);

}
