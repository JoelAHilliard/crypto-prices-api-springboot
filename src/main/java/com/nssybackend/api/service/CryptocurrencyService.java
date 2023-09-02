
package com.nssybackend.api.service;
import org.springframework.beans.factory.annotation.Autowired;

import com.nssybackend.api.repository.CryptocurrencyRepository;
import org.springframework.stereotype.Service;

import com.nssybackend.api.model.*;
import com.google.gson.Gson;

import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.domain.Sort;

import org.springframework.data.mongodb.core.aggregation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Timer;
import java.util.TimerTask;
@Service
public class CryptocurrencyService {

    private String newsData = null;

    private String marketData = null;

    private String priceData = null;

    private long cacheDuration = 600000;

    private long newsCacheTimestamp = 0;
    private long marketCacheTimestamp = 0;
    private long priceCacheTimestamp = 0;

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

        if(priceData != null){
            return priceData;
        }
        //create projection and slice operator
        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol","market_cap","market_cap_rank","circulating_supply","total_supply","ath","ath_change_percentage","ath_date","atl","atl_change_percentage","atl_date","weeklyChange","dailyChange","monthlyChange")
                .and(ArrayOperators.Slice.sliceArrayOf("tenMinIntervalPrices").itemCount(-1))
                .as("tenMinIntervalPrices");
                

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation,
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "market_cap_rank"))

        );

        //add logic to get from hourly db instead of 10 minutes db

        //perform aggregation
        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices_prod", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();

        //serialize into JSON for consumption
        Gson gson = new Gson();
 
        priceData = gson.toJson(mappedResults);

        schedulePriceCacheClear();
        
        return priceData;
    }

    public String getGraphData(String ticker, String timeframe) 
    {
        int elementsPerHour = 6;
        
        int sliceCount = 6 * 24;

        //create match criteria and operation

        Criteria criteria = Criteria.where("symbol").is(ticker);

        MatchOperation matchOperation = Aggregation.match(criteria);
        
        ProjectionOperation projectionOperation;

        int over24hrs = 0;
        
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
                over24hrs = 1;
                sliceCount = elementsPerHour * 24 * 7;
                break;
            case "1month":
                over24hrs = 1;
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
                .project("name","symbol","hourIntervalPrices");
        }
        
        else 
        {
            if(over24hrs == 1) //pull from different arr
            {
                projectionOperation = Aggregation
                    .project("name","symbol")
                    .and(ArrayOperators.Slice.sliceArrayOf("hourIntervalPrices").itemCount(-sliceCount/6)) // n/6 as there are hourly entries
                    .as("hourIntervalPrices");
                    
            }
            else
            {
                projectionOperation = Aggregation
                    .project("name","symbol")
                    .and(ArrayOperators.Slice.sliceArrayOf("tenMinIntervalPrices").itemCount(-sliceCount))
                    .as("tenMinIntervalPrices");
            }
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

        if(marketData != null){
            return marketData;
        }
        ProjectionOperation projectionOperation = Aggregation
                .project("name","symbol","market_cap","market_cap_rank","circulating_supply","total_supply","weeklyChange","dailyChange","monthlyChange")
                .and(ArrayOperators.Slice.sliceArrayOf("hourIntervalPrices").itemCount(-24))
                .as("hourIntervalPrices")
                .and(ArrayOperators.Slice.sliceArrayOf("tenMinIntervalPrices").itemCount(-1)) // n/6 as there are hourly entries
                .as("tenMinIntervalPrices");

        TypedAggregation<CryptocurrencyModel> aggregation = Aggregation.newAggregation(
                CryptocurrencyModel.class,
                projectionOperation,
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "market_cap_rank"))
        );


        AggregationResults<CryptocurrencyModel> results = mongoOperations.aggregate(aggregation, "crypto_prices_prod", CryptocurrencyModel.class);
        
        List<CryptocurrencyModel> mappedResults = results.getMappedResults();
        
        Gson gson = new Gson();

        marketData = gson.toJson(mappedResults);

        scheduleMarketCacheClear();
        
        return marketData;
    }

    public String getNews()
    {

        if(newsData != null){
            return newsData;
        }
        try{
            
            URL url = new URL("http://api.mediastack.com/v1/news?access_key=eb854c0c7b9e7dcf4ed7b591862e98d1&keywords=crypto&sort=popularity");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("accept", "application/json");

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Create a BufferedReader to read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                // Read the response line by line
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Close the BufferedReader
                reader.close();

                String res = response.toString();

                newsData = res;

                // Schedule a timer to clear the cache after the cache duration
                scheduleNewsCacheClear();

                return res;
            } else {
                System.out.println("API request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        return "";
    }

    private void scheduleNewsCacheClear() {
        // Clear the cache after the cache duration
        newsCacheTimestamp = System.currentTimeMillis();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                newsData = null;
                System.out.println("NEws Cache cleared");
            }
        }, cacheDuration);
    }
    private void scheduleMarketCacheClear() {
        // Clear the cache after the cache duration
        marketCacheTimestamp = System.currentTimeMillis();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                marketData = null;
                System.out.println(" Makret Cache cleared");
            }
        }, cacheDuration);
    }
    private void schedulePriceCacheClear() {
        // Clear the cache after the cache duration
        priceCacheTimestamp = System.currentTimeMillis();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                priceData = null;
                System.out.println("Price Cache cleared");
            }
        }, cacheDuration);
    }
    
}