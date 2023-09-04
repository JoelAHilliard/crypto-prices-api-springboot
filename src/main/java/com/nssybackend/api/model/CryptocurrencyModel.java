package com.nssybackend.api.model;

import com.nssybackend.api.model.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;

@Document(collection = "crypto_prices_prod")
public class CryptocurrencyModel {
    @Id
    private String _id;
    private String symbol;
    private String name;
    private Long market_cap;
    private Integer market_cap_rank;
    private Long circulating_supply;
    private Long total_supply;
    private Long max_supply;
    private Double ath;
    private Double ath_change_percentage;
    private String ath_date;
    private Double atl;
    private Double atl_change_percentage;
    private String atl_date;
    private Double weeklyChange;
    private Double dailyChange;
    private Double monthlyChange;
    private String[] timestamps;
    private Double[] price_points;
    private List<Object[]> tenMinIntervalPrices;
    private List<Object[]> hourIntervalPrices;
    private String svg;
    public void setSvg(String svg){
        this.svg = svg;
    }
    public String getSvg(){
        return this.svg;
    }
    public List<Object[]> getHourPrices(){
        return hourIntervalPrices;
    }
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

    
     public long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(long market_cap) {
        this.market_cap = market_cap;
    }

    public int getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public long getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(long circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public long getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(long total_supply) {
        this.total_supply = total_supply;
    }

    public long getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(long max_supply) {
        this.max_supply = max_supply;
    }

    public double getAth() {
        return ath;
    }

    public void setAth(double ath) {
        this.ath = ath;
    }

    public double getAth_change_percentage() {
        return ath_change_percentage;
    }

    public void setAth_change_percentage(double ath_change_percentage) {
        this.ath_change_percentage = ath_change_percentage;
    }

    public String getAth_date() {
        return ath_date;
    }

    public void setAth_date(String ath_date) {
        this.ath_date = ath_date;
    }

    public double getAtl() {
        return atl;
    }

    public void setAtl(double atl) {
        this.atl = atl;
    }

    public double getAtl_change_percentage() {
        return atl_change_percentage;
    }

    public void setAtl_change_percentage(double atl_change_percentage) {
        this.atl_change_percentage = atl_change_percentage;
    }

    public String getAtl_date() {
        return atl_date;
    }

    public void setAtl_date(String atl_date) {
        this.atl_date = atl_date;
    }

    
}
