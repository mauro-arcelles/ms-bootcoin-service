package com.project1.ms_bootcoin_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bootcoin-exchange-transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeTransaction {
    @Id
    private String id;

    private String exchangeRequestId;

    private LocalDateTime creationDate;
}
