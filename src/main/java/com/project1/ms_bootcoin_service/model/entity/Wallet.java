package com.project1.ms_bootcoin_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "bootcoin-wallets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    private String id;

    private String documentNumber;

    private String phoneNumber;

    private String email;

    private WalletStatus status;

    private LocalDateTime creationDate;

    private String userId;

    private BigDecimal balance;
}
