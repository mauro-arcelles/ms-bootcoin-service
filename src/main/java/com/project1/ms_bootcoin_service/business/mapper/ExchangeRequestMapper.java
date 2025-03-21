package com.project1.ms_bootcoin_service.business.mapper;

import com.project1.ms_bootcoin_service.model.CreateExchangeRequestRequest;
import com.project1.ms_bootcoin_service.model.CreateExchangeRequestResponse;
import com.project1.ms_bootcoin_service.model.GetExchangeRequestByTransactionIdResponse;
import com.project1.ms_bootcoin_service.model.GetPendingExchangeRequestsResponseInner;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequest;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequestPaymentMethod;
import com.project1.ms_bootcoin_service.model.entity.ExchangeRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class ExchangeRequestMapper {
    @Autowired
    private Clock clock;

    public ExchangeRequest getExchangeRequestEntity(CreateExchangeRequestRequest request) {
        return ExchangeRequest.builder()
            .status(ExchangeRequestStatus.PENDING)
            .amount(request.getAmount())
            .buyRate(request.getBuyRate())
            .paymentMethod(ExchangeRequestPaymentMethod.valueOf(request.getPaymentMethod()))
            .creationDate(LocalDateTime.now(clock))
            .build();
    }

    public CreateExchangeRequestResponse getCreateExchangeRequestResponse(ExchangeRequest exchangeRequest) {
        CreateExchangeRequestResponse response = new CreateExchangeRequestResponse();
        response.setId(exchangeRequest.getId());
        response.setAmount(exchangeRequest.getAmount());
        response.setPaymentMethod(exchangeRequest.getPaymentMethod().name());
        return response;
    }

    public GetPendingExchangeRequestsResponseInner getPendingExchangeRequest(ExchangeRequest exchangeRequest) {
        GetPendingExchangeRequestsResponseInner response = new GetPendingExchangeRequestsResponseInner();
        response.setId(exchangeRequest.getId());
        response.setAmount(exchangeRequest.getAmount());
        response.setBuyRate(exchangeRequest.getBuyRate());
        return response;
    }

    public GetExchangeRequestByTransactionIdResponse getExchangeRequestByTransactionIdResponse(ExchangeRequest exchangeRequest) {
        GetExchangeRequestByTransactionIdResponse response = new GetExchangeRequestByTransactionIdResponse();
        response.setId(exchangeRequest.getId());
        response.setAmount(exchangeRequest.getAmount());
        response.setBuyRate(exchangeRequest.getBuyRate());
        response.setPaymentMethod(exchangeRequest.getPaymentMethod().name());
        response.setRequestOwnerUserId(exchangeRequest.getRequestOwnerUserId());
        response.setRequestAccepterUserId(exchangeRequest.getRequestAccepterUserId());
        response.setStatus(exchangeRequest.getStatus().name());
        response.setCreationDate(exchangeRequest.getCreationDate());
        return response;
    }
}
