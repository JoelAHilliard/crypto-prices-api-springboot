package com.nssybackend.api.model;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Price {
        private String timestamp;
        private Double price;
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

        public Price(String timestamp, double price, long market_cap, int market_cap_rank,
                 long circulating_supply, Long total_supply, Long max_supply, double ath,
                 double ath_change_percentage, String ath_date, double atl,
                 double atl_change_percentage, String atl_date) {
        this.timestamp = timestamp;
        this.price = price;
  
        this.market_cap = market_cap;
        
        this.market_cap_rank = market_cap_rank;
        this.circulating_supply = circulating_supply;
        
        this.total_supply = total_supply != null ? total_supply : 0L;
        this.max_supply = max_supply;
        this.ath = ath;
        this.ath_change_percentage = ath_change_percentage;
        this.ath_date = ath_date;
        this.atl = atl;
        this.atl_change_percentage = atl_change_percentage;
        this.atl_date = atl_date;
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