package com.example.stockmarketapi.Repo;

import com.example.stockmarketapi.model.History;
import com.example.stockmarketapi.model.Portfolio;
import com.example.stockmarketapi.model.User;
import com.example.stockmarketapi.model.Watchlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

// adds custom methods to Mongo Repository
@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("{userID: '?0'}")
    User findByUserID(String userID);

    @Query("{userID: '?0'}")
    @Update("{'$set': {'watchlist': ?1}}")
    void updateWatchlist(String userID, Object watchlist);

    @Query("{userID: '?0'}")
    @Update("{'$set': {'portfolio': ?1, 'fund': ?2, 'history': ?3}}")
    void updateBuyTic(String userID, Portfolio portfolio, Double fund, History history);
}
