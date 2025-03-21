package com.project1.ms_bootcoin_service.repository;

import com.project1.ms_bootcoin_service.model.entity.ExchangeRate;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRateCurrency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, String> {
    Mono<ExchangeRate> findByCurrency(ExchangeRateCurrency currency);
}
