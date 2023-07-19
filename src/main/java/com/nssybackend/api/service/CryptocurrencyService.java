
package com.nssybackend.api.service;
import org.springframework.beans.factory.annotation.Autowired;

import com.nssybackend.api.repository.CryptocurrencyRepository;

import org.springframework.stereotype.Service;

import com.nssybackend.api.model.*;
import com.google.gson.Gson;

import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.core.aggregation.*;
@Service
public class CryptocurrencyService {

    @Autowired
    MongoOperations mongoOperations;

    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    //returns all cryptos with name, symbol and the most recent price entry.
    public String getPriceData() 
    {
        //create projection and slice operator
        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol")
                .and(ArrayOperators.Slice.sliceArrayOf("prices").itemCount(-1))
                .as("prices");

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation
        );

        //perform aggregation
        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();

        //serialize into JSON for consumption
        Gson gson = new Gson();
        
        String json = "";

        json = gson.toJson(mappedResults);

        return json;
    }

    public String getGraphData(String ticker, String timeframe) 
    {
        int elementsPerHour = 6;
        
        int sliceCount = 6 * 24;

        //create match criteria and operation

        Criteria criteria = Criteria.where("symbol").is(ticker);

        MatchOperation matchOperation = Aggregation.match(criteria);
        
        ProjectionOperation projectionOperation;
        
        
        //calculte how many documents to return based off timeframe
        switch(timeframe)
        {
            case "12hr":
                sliceCount = elementsPerHour * 12;
                break;
            case "4hr":
                sliceCount = elementsPerHour * 4;
                break;
            case "1week":
                sliceCount = elementsPerHour * 24 * 7;
                break;
            case "1month":
                sliceCount = elementsPerHour * 24 * 30; // Assume 30 days in a month
                break;
            case "max":
                sliceCount = -1;
            default:
                sliceCount = elementsPerHour * 24;

        }

        if(timeframe == "max")
        {
            projectionOperation = Aggregation
                .project("name","symbol","prices");
        }
        else 
        {
            projectionOperation = Aggregation
                .project("name","symbol")
                .and(ArrayOperators.Slice.sliceArrayOf("prices").itemCount(-sliceCount))
                .as("prices");
        }
        
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                projectionOperation
        );

        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();
        
        //serialize into JSON for consumption
        Gson gson = new Gson();
        
        String json = "";

        json = gson.toJson(mappedResults);

        return json;
    }
}