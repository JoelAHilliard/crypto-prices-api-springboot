
package com.nssybackend.api.service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.nssybackend.api.repository.CryptocurrencyRepository;

import org.springframework.stereotype.Service;

import com.nssybackend.api.model.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.core.aggregation.*;
@Service
public class CryptocurrencyService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoOperations mongoOperations;


    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    public List<CryptocurrencyModel> getAllCryptocurrencies() {
        return cryptocurrencyRepository.findAll();
    }

    public CryptocurrencyModel getSingleCryptocurrency() 
    {
        Optional<CryptocurrencyModel> optionalCryptocurrency = cryptocurrencyRepository.findById("6430ac5b99eee7a6b77b8861");
        return optionalCryptocurrency.orElse(null);
    }

    //returns all cryptos with name, symbol and the most recent price entry.
    public List<CryptocurrencyModel> getPriceData() 
    {
        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol")
                .and(ArrayOperators.Slice.sliceArrayOf("prices").itemCount(-1))
                .as("prices");

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation
        );

        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();

        return mappedResults;
    }

    public List<CryptocurrencyModel> getGraphData(String ticker, String timeframe) 
    {

        //create match criteria and operation
        Criteria criteria = Criteria.where("symbol").is(ticker);

        MatchOperation matchOperation = Aggregation.match(criteria);
        
        //return latest item count
        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol")
                .and(ArrayOperators.Slice.sliceArrayOf("prices").itemCount(-50))
                .as("prices");
                

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                projectionOperation
        );

        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();

        return mappedResults;
    }
}