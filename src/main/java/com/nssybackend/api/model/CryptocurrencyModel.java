package com.nssybackend.api.model;

import com.nssybackend.api.model.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;

@Document(collection = "crypto_prices")
public class CryptocurrencyModel {
    @Id
    private String _id;
    private String symbol;
    private String name;
    private Price[] prices;

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price[] getPrices() {
        return prices;
    }
    public Price getFirstPrice(){
        return prices[0];
    }
    public void setPrices(Price[] prices) {
        this.prices = prices;
    }

    
}
