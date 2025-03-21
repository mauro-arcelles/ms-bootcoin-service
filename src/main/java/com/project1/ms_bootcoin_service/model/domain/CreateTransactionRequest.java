package com.project1.ms_bootcoin_service.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTransactionRequest {
    private String exchangeRequestId;
}
