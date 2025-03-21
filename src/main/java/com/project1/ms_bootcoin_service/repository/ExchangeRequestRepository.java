package com.project1.ms_bootcoin_service.repository;

import com.project1.ms_bootcoin_service.model.entity.ExchangeRequest;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequestStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRequestRepository extends ReactiveMongoRepository<ExchangeRequest, String> {
    Flux<ExchangeRequest> findAllByStatus(ExchangeRequestStatus status);

    Mono<ExchangeRequest> findByTransactionId(String transactionId);
}
