package com.project1.ms_bootcoin_service.business.service.impl;

import com.project1.ms_bootcoin_service.business.mapper.ExchangeRequestMapper;
import com.project1.ms_bootcoin_service.business.service.ExchangeRequestService;
import com.project1.ms_bootcoin_service.business.service.ExchangeTransactionService;
import com.project1.ms_bootcoin_service.config.CustomObjectMapper;
import com.project1.ms_bootcoin_service.config.auth.SecurityUtils;
import com.project1.ms_bootcoin_service.exception.BadRequestException;
import com.project1.ms_bootcoin_service.model.*;
import com.project1.ms_bootcoin_service.model.domain.CreateTransactionRequest;
import com.project1.ms_bootcoin_service.model.domain.BootcoinTransactionRequest;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRateCurrency;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequest;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequestStatus;
import com.project1.ms_bootcoin_service.repository.ExchangeRateRepository;
import com.project1.ms_bootcoin_service.repository.ExchangeRequestRepository;
import com.project1.ms_bootcoin_service.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ExchangeRequestServiceImpl implements ExchangeRequestService {

    @Value("${application.config.kafka.topic-name}")
    private String kafkaTopicName;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ExchangeRequestMapper exchangeRequestMapper;

    @Autowired
    private ExchangeTransactionService exchangeTransactionService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Override
    public Mono<CreateExchangeRequestResponse> createExchangeRequest(Mono<CreateExchangeRequestRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req ->
            securityUtils.getUserIdFromToken(exchange)
                .flatMap(userId ->
                    walletRepository.findByUserId(userId)
                        .switchIfEmpty(Mono.error(new BadRequestException("Cannot create EXCHANGE REQUEST without a WALLET. Please create a WALLET first.")))
                        .flatMap(wallet ->
                            exchangeRateRepository.findByCurrency(ExchangeRateCurrency.PEN)
                                .flatMap(exchangeRate -> {
                                    if (req.getBuyRate() < exchangeRate.getBuyRate()) {
                                        return Mono.error(new BadRequestException("Buy rate cannot be less than buy rate: " + exchangeRate.getBuyRate()));
                                    }
                                    if (req.getBuyRate() > exchangeRate.getSellRate()) {
                                        return Mono.error(new BadRequestException("Buy rate cannot be greater than sell rate: " + exchangeRate.getSellRate()));
                                    }
                                    return Mono.just(exchangeRate);
                                }))
                        .map(wallet -> {
                            ExchangeRequest entity = exchangeRequestMapper.getExchangeRequestEntity(req);
                            entity.setRequestOwnerUserId(userId);
                            return entity;
                        })
                        .flatMap(exchangeRequestRepository::save)
                        .map(exchangeRequestMapper::getCreateExchangeRequestResponse)
                )
        );
    }

    @Override
    public Flux<GetPendingExchangeRequestsResponseInner> getPendingExchangeRequests() {
        return exchangeRequestRepository.findAllByStatus(ExchangeRequestStatus.PENDING)
            .map(exchangeRequestMapper::getPendingExchangeRequest);
    }

    @Override
    public Mono<Void> acceptExchangeRequest(String id, ServerWebExchange exchange) {
        return exchangeRequestRepository.findById(id)
            .switchIfEmpty(Mono.error(new BadRequestException("EXCHANGE REQUEST not found")))
            .flatMap(exchangeRequest -> {
                if (exchangeRequest.getStatus() != ExchangeRequestStatus.PENDING) {
                    return Mono.error(new BadRequestException("EXCHANGE REQUEST is not pending"));
                }

                return securityUtils.getUserIdFromToken(exchange)
                    .flatMap(userId -> {
                        if (exchangeRequest.getRequestOwnerUserId().equals(userId)) {
                            return Mono.error(new BadRequestException("Cannot accept your own EXCHANGE REQUEST"));
                        }

                        log.info("Finding bootcoin wallet for user: {}", userId);

                        return walletRepository.findByUserId(userId)
                            .switchIfEmpty(Mono.error(new BadRequestException("Bootcoin wallet not found for user + " + userId)))
                            .flatMap(w -> {
                                log.info("Bootcoin wallet found: {}", w);
                                if (w.getBalance().compareTo(exchangeRequest.getAmount()) < 0) {
                                    log.info("Insufficient balance in bootcoin wallet to accept exchange request: {}", exchangeRequest);
                                    return Mono.error(new BadRequestException("Insufficient balance in bootcoin wallet to accept exchange request"));
                                }
                                log.info("Sufficient balance in bootcoin wallet to accept exchange request: {}", exchangeRequest);
                                return Mono.just(w);
                            })
                            .flatMap(w -> {
                                CreateTransactionRequest exchangeTransaction = CreateTransactionRequest.builder()
                                    .exchangeRequestId(exchangeRequest.getId())
                                    .build();

                                log.info("Creating exchange transaction: {}", exchangeTransaction);

                                return exchangeTransactionService.createTransaction(Mono.just(exchangeTransaction))
                                    .doOnSuccess(ctr -> log.info("Exchange transaction created: {}", ctr))
                                    .map(transaction -> {
                                        exchangeRequest.setTransactionId(transaction.getId());
                                        exchangeRequest.setRequestAccepterUserId(userId);
                                        exchangeRequest.setStatus(ExchangeRequestStatus.ACCEPTED);
                                        return exchangeRequest;
                                    });
                            });
                    });
            })
            .flatMap(exchangeRequestRepository::save)
            .flatMap(exchangeRequest -> {
                log.info("Sending message to Kafka for exchange request: {}", exchangeRequest);
                try {
                    BootcoinTransactionRequest transactionRequest = new BootcoinTransactionRequest();
                    transactionRequest.setId(exchangeRequest.getTransactionId());
                    String message = objectMapper.objectToString(transactionRequest);
                    kafkaTemplate.send(kafkaTopicName, message).get(3, TimeUnit.SECONDS);
                    log.info("Sent message to Kafka for exchange request: {}", exchangeRequest);
                    return Mono.just(exchangeRequest);
                } catch (Exception e) {
                    log.error("Error sending message to Kafka", e);
                    return Mono.error(new BadRequestException("Error sending message to Kafka"));
                }
            })
            .doOnError(throwable -> log.error("There was an error when acceptinExchangeRequest", throwable))
            .doOnSuccess(e -> log.info("Exchange request accepted: {}", e))
            .then();
    }

    @Override
    public Mono<GetExchangeRequestByTransactionIdResponse> getExchangeRequestByTransactionId(String transactionId) {
        return exchangeRequestRepository.findByTransactionId(transactionId)
            .switchIfEmpty(Mono.error(new BadRequestException("EXCHANGE REQUEST not found for transaction ID: " + transactionId)))
            .map(exchangeRequestMapper::getExchangeRequestByTransactionIdResponse);
    }

    @Override
    public Mono<Void> updateExchangeRequest(String id, Mono<UpdateExchangeRequestRequest> request) {
        return request.flatMap(req -> {
            log.info("Updating exchange request: {}", req);
            return exchangeRequestRepository.findById(id)
                .switchIfEmpty(Mono.error(new BadRequestException("EXCHANGE REQUEST not found")))
                .flatMap(exchangeRequest -> {
                    Optional.ofNullable(req.getStatus()).ifPresent(e -> exchangeRequest.setStatus(ExchangeRequestStatus.valueOf(e)));
                    Optional.ofNullable(req.getMessage()).ifPresent(exchangeRequest::setMessage);
                    return exchangeRequestRepository.save(exchangeRequest);
                })
                .doOnSuccess(e -> log.info("Exchange request updated: {}", e))
                .then();
        });
    }
}
