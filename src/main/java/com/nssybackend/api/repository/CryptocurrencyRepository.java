package com.nssybackend.api.repository;

import com.nssybackend.api.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyRepository extends MongoRepository<CryptocurrencyModel, String> {

}
