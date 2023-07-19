package com.nssybackend.api;
import com.nssybackend.api.model.Price;

import org.json.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.nssybackend.api.service.CryptocurrencyService;
import com.nssybackend.api.model.CryptocurrencyModel;
import java.util.List;
import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class CryptocurrencyController {

    @Autowired
    CryptocurrencyService cryptoService;


    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

    @GetMapping("/priceData")
    public String crypto()
    {
        List<CryptocurrencyModel> priceData = cryptoService.getPriceData();
        
        Gson gson = new Gson();
        
        String json = "";

        json = gson.toJson(priceData);
        
        return json;
    }

    @GetMapping("/getGraphData")
    public String graph(@RequestParam String symbol, @RequestParam String timeframe)
    {
        List<CryptocurrencyModel> data = cryptoService.getGraphData(symbol,timeframe);
        
        Gson gson = new Gson();
        
        String json = "";

        json = gson.toJson(data);
        
        return json;
    }

    
}
