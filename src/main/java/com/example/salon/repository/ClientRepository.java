package com.example.salon.repository;

import com.example.salon.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends MongoRepository<Client, UUID>, CustomClientRepository {
    //do nothing
}
