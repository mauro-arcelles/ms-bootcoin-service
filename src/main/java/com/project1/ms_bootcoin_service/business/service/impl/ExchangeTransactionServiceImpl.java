package com.project1.ms_bootcoin_service.business.service.impl;

import com.project1.ms_bootcoin_service.business.mapper.ExchangeTransactionMapper;
import com.project1.ms_bootcoin_service.business.service.ExchangeTransactionService;
import com.project1.ms_bootcoin_service.model.domain.CreateTransactionRequest;
import com.project1.ms_bootcoin_service.model.domain.CreateTransactionResponse;
import com.project1.ms_bootcoin_service.repository.ExchangeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ExchangeTransactionServiceImpl implements ExchangeTransactionService {
    @Autowired
    private ExchangeTransactionRepository exchangeTransactionRepository;

    @Autowired
    private ExchangeTransactionMapper exchangeTransactionMapper;

    @Override
    public Mono<CreateTransactionResponse> createTransaction(Mono<CreateTransactionRequest> request) {
        return request.map(exchangeTransactionMapper::getExchangeTransactionEntity)
            .flatMap(exchangeTransactionRepository::save)
            .map(exchangeTransactionMapper::getCreateTransactionResponse);
    }
}
