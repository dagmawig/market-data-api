package com.example.stockmarketapi;

import com.example.stockmarketapi.Repo.DataRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = DataRepo.class)
public class StockMarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApiApplication.class, args);
	}

}
