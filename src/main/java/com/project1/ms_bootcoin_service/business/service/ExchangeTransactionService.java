package com.project1.ms_bootcoin_service.business.service;

import com.project1.ms_bootcoin_service.model.domain.CreateTransactionRequest;
import com.project1.ms_bootcoin_service.model.domain.CreateTransactionResponse;
import reactor.core.publisher.Mono;

public interface ExchangeTransactionService {
    Mono<CreateTransactionResponse> createTransaction(Mono<CreateTransactionRequest> request);
}
