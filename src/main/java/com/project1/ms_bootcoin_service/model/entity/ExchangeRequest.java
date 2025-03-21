package com.project1.ms_bootcoin_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "bootcoin-exchange-requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {
    @Id
    private String id;

    private BigDecimal amount;

    private ExchangeRequestPaymentMethod paymentMethod;

    private Double buyRate;

    private String requestOwnerUserId;

    private String requestAccepterUserId;

    private String transactionId;

    private ExchangeRequestStatus status;

    private LocalDateTime creationDate;

    private String message;
}
