package com.project1.ms_bootcoin_service.business.service;

import com.project1.ms_bootcoin_service.model.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRequestService {
    Mono<CreateExchangeRequestResponse> createExchangeRequest(Mono<CreateExchangeRequestRequest> request, ServerWebExchange exchange);

    Flux<GetPendingExchangeRequestsResponseInner> getPendingExchangeRequests();

    Mono<Void> acceptExchangeRequest(String id, ServerWebExchange exchange);

    Mono<GetExchangeRequestByTransactionIdResponse> getExchangeRequestByTransactionId(String transactionId);

    Mono<Void> updateExchangeRequest(String id, Mono<UpdateExchangeRequestRequest> request);
}
