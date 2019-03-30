package com.example.salon.repository;

import com.example.salon.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
    //do nothing
}
