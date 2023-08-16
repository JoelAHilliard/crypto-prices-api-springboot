package com.nssybackend.api.model;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
@Document
public class Price
{
    private Double price;
    private Date date;
    

    public Price(Double price, Date date) {
        this.price = price;
        this.date = date;
    }
}