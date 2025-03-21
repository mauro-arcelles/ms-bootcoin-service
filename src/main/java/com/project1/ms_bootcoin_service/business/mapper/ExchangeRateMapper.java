package com.project1.ms_bootcoin_service.business.mapper;

import com.project1.ms_bootcoin_service.model.ExchangeRateResponse;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateRequest;
import com.project1.ms_bootcoin_service.model.UpdateExchangeRateResponse;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExchangeRateMapper {
    public UpdateExchangeRateResponse getUpdateExchangeRateResponse(ExchangeRate exchangeRate) {
        UpdateExchangeRateResponse response = new UpdateExchangeRateResponse();
        response.setBuyRate(exchangeRate.getBuyRate());
        response.setSellRate(exchangeRate.getSellRate());
        response.setCurrency(exchangeRate.getCurrency().toString());
        response.setId(exchangeRate.getId());
        return response;
    }

    public ExchangeRate getUpdateExchangeRateEntity(ExchangeRate entity, UpdateExchangeRateRequest request) {
        Optional.ofNullable(request.getBuyRate()).ifPresent(entity::setBuyRate);
        Optional.ofNullable(request.getSellRate()).ifPresent(entity::setSellRate);
        return entity;
    }

    public ExchangeRateResponse getExchangeRateResponse(ExchangeRate exchangeRate) {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBuyRate(exchangeRate.getBuyRate());
        response.setSellRate(exchangeRate.getSellRate());
        response.setCurrency(exchangeRate.getCurrency().toString());
        response.setId(exchangeRate.getId());
        return response;
    }
}
