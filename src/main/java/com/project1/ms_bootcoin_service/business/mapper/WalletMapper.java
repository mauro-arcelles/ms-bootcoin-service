package com.project1.ms_bootcoin_service.business.mapper;

import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletRequest;
import com.project1.ms_bootcoin_service.model.CreateBootcoinWalletResponse;
import com.project1.ms_bootcoin_service.model.entity.Wallet;
import com.project1.ms_bootcoin_service.model.entity.WalletStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class WalletMapper {

    @Autowired
    private Clock clock;

    public CreateBootcoinWalletResponse getCreateWalletResponse(Wallet wallet) {
        CreateBootcoinWalletResponse response = new CreateBootcoinWalletResponse();
        response.setId(wallet.getId());
        response.setDocumentNumber(wallet.getDocumentNumber());
        response.setEmail(wallet.getEmail());
        response.setPhone(wallet.getPhoneNumber());
        response.balance(wallet.getBalance());
        response.creationDate(wallet.getCreationDate());
        response.status(wallet.getStatus().toString());
        return response;
    }

    public Wallet getCreateWalletEntity(CreateBootcoinWalletRequest createWalletRequest) {
        return Wallet.builder()
            .email(createWalletRequest.getEmail())
            .phoneNumber(createWalletRequest.getPhone())
            .documentNumber(createWalletRequest.getDocumentNumber())
            .balance(BigDecimal.ZERO)
            .status(WalletStatus.ACTIVE)
            .creationDate(LocalDateTime.now(clock))
            .build();
    }
}
