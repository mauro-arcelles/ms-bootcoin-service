package com.project1.ms_bootcoin_service.business.service.impl;

import com.project1.ms_bootcoin_service.business.mapper.WalletMapper;
import com.project1.ms_bootcoin_service.business.service.WalletService;
import com.project1.ms_bootcoin_service.config.auth.SecurityUtils;
import com.project1.ms_bootcoin_service.exception.BadRequestException;
import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletRequest;
import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletResponse;
import com.project1.ms_bootcoin_service.model.UpdateBootcoinWalletRequest;
import com.project1.ms_bootcoin_service.model.entity.WalletStatus;
import com.project1.ms_bootcoin_service.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Mono<CreateBootcoinWalletResponse> createWallet(Mono<CreateBootcoinWalletRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req ->
                securityUtils.getUserIdFromToken(exchange)
                    .flatMap(userId ->
                        walletRepository.findByUserId(userId)
                            .flatMap(existingWallet -> {
                                if (existingWallet != null) {
                                    return Mono.error(new BadRequestException("USER already has a wallet"));
                                }
                                return walletRepository.findByDocumentNumber(req.getDocumentNumber())
                                    .flatMap(wallet -> Mono.error(new BadRequestException("WALLET already exists for document number: "
                                        + req.getDocumentNumber())))
                                    .then(Mono.just(req));
                            })
                            .then(Mono.just(req))
                    )
            )
            .flatMap(req -> Mono.just(walletMapper.getCreateWalletEntity(req)))
            .flatMap(wallet ->
                securityUtils.getUserIdFromToken(exchange)
                    .map(userId -> {
                        wallet.setUserId(userId);
                        return wallet;
                    })
            )
            .flatMap(walletRepository::save)
            .map(walletMapper::getCreateWalletResponse)
            .doOnSubscribe(e -> log.info("Creating wallet"))
            .doOnSuccess(e -> log.info("Wallet created"))
            .doOnError(e -> log.error("Error creating wallet", e));
    }

    @Override
    public Mono<CreateBootcoinWalletResponse> getWalletByUserId(String userId) {
        return walletRepository.findByUserId(userId)
            .switchIfEmpty(Mono.error(new BadRequestException("WALLET does not exist for user id: " + userId)))
            .map(walletMapper::getCreateWalletResponse);
    }

    @Override
    public Mono<Void> updateWallet(String walletId, Mono<UpdateBootcoinWalletRequest> updateBootcoinWalletRequest) {
        return walletRepository.findById(walletId)
            .switchIfEmpty(Mono.error(new BadRequestException("WALLET does not exist for wallet id: " + walletId)))
            .flatMap(wallet -> updateBootcoinWalletRequest
                .map(req -> {
                    Optional.ofNullable(req.getStatus()).ifPresent(status -> wallet.setStatus(WalletStatus.valueOf(status)));
                    Optional.ofNullable(req.getBalance()).ifPresent(wallet::setBalance);
                    return wallet;
                })
            )
            .flatMap(walletRepository::save)
            .then();
    }
}
