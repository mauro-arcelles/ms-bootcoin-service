package com.project1.ms_bootcoin_service.repository;

import com.project1.ms_bootcoin_service.model.entity.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<Wallet, String> {
    Mono<Wallet> findByDocumentNumber(String documentNumber);

    Mono<Wallet> findByUserId(String userId);
}
