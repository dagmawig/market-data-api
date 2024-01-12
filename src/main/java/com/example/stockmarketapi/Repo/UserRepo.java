package com.example.stockmarketapi.Repo;

import com.example.stockmarketapi.model.User;
import com.example.stockmarketapi.model.Watchlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("{userID: '?0'}")
    User findByUserID(String userID);

    @Query("{userID: '?0'}")
    @Update("{'$set': {'watchlist': ?1}}")
    void updateWatchlist(String userID, Object watchlist);


}
