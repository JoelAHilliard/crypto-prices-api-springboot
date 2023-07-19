package com.nssybackend.api;
import com.nssybackend.api.model.Price;

import org.json.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/crypto")
    public String crypto()
    {
        List<CryptocurrencyModel> names = cryptoService.getAllCryptocurrencyNames();
        
        Gson gson = new Gson();
        
        String json = "";
            
        json = gson.toJson(names);
        
        return json;
    }

    
}
