package com.project1.ms_bootcoin_service.business.mapper;

import com.project1.ms_bootcoin_service.model.domain.CreateTransactionRequest;
import com.project1.ms_bootcoin_service.model.domain.CreateTransactionResponse;
import com.project1.ms_bootcoin_service.model.entity.ExchangeTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class ExchangeTransactionMapper {
    @Autowired
    private Clock clock;

    public ExchangeTransaction getExchangeTransactionEntity(CreateTransactionRequest request) {
        return ExchangeTransaction.builder()
            .exchangeRequestId(request.getExchangeRequestId())
            .creationDate(LocalDateTime.now(clock))
            .build();
    }

    public CreateTransactionResponse getCreateTransactionResponse(ExchangeTransaction exchangeTransaction) {
        return CreateTransactionResponse.builder()
            .id(exchangeTransaction.getId())
            .exchangeRequestId(exchangeTransaction.getExchangeRequestId())
            .build();
    }
}
