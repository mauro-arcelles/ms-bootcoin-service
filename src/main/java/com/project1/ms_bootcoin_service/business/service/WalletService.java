package com.project1.ms_bootcoin_service.business.service;

import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletRequest;
import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletResponse;
import com.project1.ms_bootcoin_service.model.UpdateBootcoinWalletRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface WalletService {
    Mono<CreateBootcoinWalletResponse> createWallet(Mono<CreateBootcoinWalletRequest> request, ServerWebExchange exchange);

    Mono<CreateBootcoinWalletResponse> getWalletByUserId(String userId);

    Mono<Void> updateWallet(String walletId, Mono<UpdateBootcoinWalletRequest> updateBootcoinWalletRequest);
}
