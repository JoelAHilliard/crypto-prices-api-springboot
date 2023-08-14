
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
                .project("name","symbol","market_cap","market_cap_rank","circulating_supply","total_supply","ath","ath_change_percentage","ath_date","atl","atl_change_percentage","atl_date","weeklyChange","dailyChange","monthlyChange")
                .and(ArrayOperators.Slice.sliceArrayOf("price_points").itemCount(-1))
                .as("price_points")
                .and(ArrayOperators.Slice.sliceArrayOf("timestamps").itemCount(-1))
                .as("timestamps");

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation
        );

        //perform aggregation
        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices_prod", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();

        //serialize into JSON for consumption
        Gson gson = new Gson();
        
        return gson.toJson(mappedResults);
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
            case "15m":
                sliceCount = 6;
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
                break;
            default:
                sliceCount = elementsPerHour * 24;

        }

        if(sliceCount == -1)
        {
            projectionOperation = Aggregation
                .project("name","symbol","price_points","timestamps");
        }
        
        else 
        {

            projectionOperation = Aggregation
                .project("name","symbol")
                .and(ArrayOperators.Slice.sliceArrayOf("price_points").itemCount(-sliceCount))
                .as("price_points")
                .and(ArrayOperators.Slice.sliceArrayOf("timestamps").itemCount(-sliceCount))
                .as("timestamps");
        }
        
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                projectionOperation
        );

        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices_prod", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();
        
        //serialize into JSON for consumption
        Gson gson = new Gson();

        return gson.toJson(mappedResults);
    }

    public String getMarketData()
    {

        int sliceCount = 24 * 7;

        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol","market_cap","market_cap_rank","circulating_supply","total_supply","weeklyChange","dailyChange","monthlyChange")
                .and(ArrayOperators.Slice.sliceArrayOf("price_points").itemCount(-sliceCount))
                .as("price_points")
                .and(ArrayOperators.Slice.sliceArrayOf("timestamps").itemCount(-sliceCount))
                .as("timestamps");

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation
        );
        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices_prod", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();
        
        Gson gson = new Gson();
        
        return gson.toJson(mappedResults);
    }
}