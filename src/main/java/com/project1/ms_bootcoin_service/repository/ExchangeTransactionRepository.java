package com.project1.ms_bootcoin_service.repository;

import com.project1.ms_bootcoin_service.model.entity.ExchangeTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeTransactionRepository extends ReactiveMongoRepository<ExchangeTransaction, String> {
}
