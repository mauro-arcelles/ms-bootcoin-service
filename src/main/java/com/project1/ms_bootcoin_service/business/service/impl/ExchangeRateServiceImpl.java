package com.project1.ms_bootcoin_service.business.service.impl;

import com.project1.ms_bootcoin_service.business.mapper.ExchangeRateMapper;
import com.project1.ms_bootcoin_service.business.service.ExchangeRateService;
import com.project1.ms_bootcoin_service.exception.BadRequestException;
import com.project1.ms_bootcoin_service.model.ExchangeRateResponse;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateRequest;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateResponse;
import com.project1.ms_bootcoin_service.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Override
    public Flux<ExchangeRateResponse> getExchangeRates() {
        return exchangeRateRepository.findAll()
            .map(exchangeRateMapper::getExchangeRateResponse);
    }

    @Override
    public Mono<UpdateExchangeRateResponse> updateExchangeRate(String id, Mono<UpdateExchangeRateRequest> request) {
        return request.flatMap(req ->
                exchangeRateRepository.findById(id)
                    .map(exchangeRate -> exchangeRateMapper.getUpdateExchangeRateEntity(exchangeRate, req))
            )
            .switchIfEmpty(Mono.error(new BadRequestException("EXCHANGE RATE not found with id: " + id)))
            .flatMap(exchangeRateRepository::save)
            .map(exchangeRateMapper::getUpdateExchangeRateResponse);
    }
}
