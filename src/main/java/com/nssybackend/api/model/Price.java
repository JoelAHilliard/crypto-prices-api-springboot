package com.nssybackend.api.model;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Price 
{
    private String timestamp;
    private Double price;
    

    public Price(String timestamp, double price) 
                {
        this.timestamp = timestamp;
        this.price = price;

        
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   
}