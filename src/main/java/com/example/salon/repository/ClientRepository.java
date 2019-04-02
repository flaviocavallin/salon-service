package com.example.salon.repository;

import com.example.salon.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, String>, CustomClientRepository {
    //do nothing
}
