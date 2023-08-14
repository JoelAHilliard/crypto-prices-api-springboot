package com.nssybackend.api.model;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Price 
{
    private String timestamps[];
    private Double price_points[];
    

    public Price(String timestamps[], Double prices[]) 
                {
        this.timestamps = timestamps;
        this.price_points = prices;

        
    }

    public String[] getTimestamp() {
        return timestamps;
    }

    public void setTimestamp(String timestamp[]) {
        this.timestamps = timestamp;
    }

    public Double[] getPrice() {
        return this.price_points;
    }

    public void setPrice(Double price[]) {
        this.price_points = price;
    }

   
}