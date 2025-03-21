package com.project1.ms_bootcoin_service.business.service;

import com.project1.ms_bootcoin_service.model.ExchangeRateResponse;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateRequest;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Flux<ExchangeRateResponse> getExchangeRates();

    Mono<UpdateExchangeRateResponse> updateExchangeRate(String id, Mono<UpdateExchangeRateRequest> request);
}
