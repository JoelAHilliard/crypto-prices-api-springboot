package com.nssybackend.api;

import com.nssybackend.api.model.Price;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.nssybackend.api.service.CryptocurrencyService;
import com.nssybackend.api.model.CryptocurrencyModel;



@RestController
public class CryptocurrencyController {

    @Autowired
    CryptocurrencyService cryptoService;


    @GetMapping("/")
    public String index() {
        return "Nssy";
    }

    @GetMapping("/priceData")
    public String crypto()
    {
        return cryptoService.getPriceData();
    }
    @CrossOrigin
    @GetMapping("/getGraphData")
    public String graph(@RequestParam String symbol, @RequestParam String timeframe)
    {
        return cryptoService.getGraphData(symbol,timeframe);
    }    
}
