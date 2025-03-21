package com.project1.ms_bootcoin_service.expose.web;

import com.project1.ms_bootcoin_service.api.BootcoinApiDelegate;
import com.project1.ms_bootcoin_service.business.service.ExchangeRateService;
import com.project1.ms_bootcoin_service.business.service.ExchangeRequestService;
import com.project1.ms_bootcoin_service.business.service.WalletService;
import com.project1.ms_bootcoin_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BootcoinApiDelegateImpl implements BootcoinApiDelegate {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    @Autowired
    private WalletService walletService;

    @Override
    public Mono<ResponseEntity<CreateBootcoinWalletResponse>> createBootcoinWallet(Mono<CreateBootcoinWalletRequest> createBootcoinWalletRequest,
                                                                                   ServerWebExchange exchange) {
        return walletService.createWallet(createBootcoinWalletRequest, exchange)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBootcoinWallet(String walletId, Mono<UpdateBootcoinWalletRequest> updateBootcoinWalletRequest,
                                                           ServerWebExchange exchange) {
        return walletService.updateWallet(walletId, updateBootcoinWalletRequest)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> acceptExchangeRequest(String requestId, ServerWebExchange exchange) {
        return exchangeRequestService.acceptExchangeRequest(requestId, exchange)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Override
    public Mono<ResponseEntity<CreateExchangeRequestResponse>> createExchangeRequest(Mono<CreateExchangeRequestRequest> createExchangeRequestRequest,
                                                                                     ServerWebExchange exchange) {
        return exchangeRequestService.createExchangeRequest(createExchangeRequestRequest, exchange)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> updateExchangeRequest(String requestId, Mono<UpdateExchangeRequestRequest> updateExchangeRequestRequest,
                                                            ServerWebExchange exchange) {
        return exchangeRequestService.updateExchangeRequest(requestId, updateExchangeRequestRequest)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<ExchangeRateResponse>>> getExchangeRates(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(exchangeRateService.getExchangeRates()));
    }

    @Override
    public Mono<ResponseEntity<Flux<GetPendingExchangeRequestsResponseInner>>> getPendingExchangeRequests(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(exchangeRequestService.getPendingExchangeRequests()));
    }

    @Override
    public Mono<ResponseEntity<UpdateExchangeRateResponse>> updateExchangeRates(String id, Mono<UpdateExchangeRateRequest> updateExchangeRateRequest,
                                                                                ServerWebExchange exchange) {
        return exchangeRateService.updateExchangeRate(id, updateExchangeRateRequest)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<GetExchangeRequestByTransactionIdResponse>> getExchangeRequestByTransactionId(String transactionId, ServerWebExchange exchange) {
        return exchangeRequestService.getExchangeRequestByTransactionId(transactionId).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreateBootcoinWalletResponse>> getWalletByUserId(String userId, ServerWebExchange exchange) {
        return walletService.getWalletByUserId(userId).map(ResponseEntity::ok);
    }
}
