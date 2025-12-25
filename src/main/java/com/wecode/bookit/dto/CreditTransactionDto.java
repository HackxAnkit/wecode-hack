package com.wecode.bookit.dto;

import com.wecode.bookit.entity.CreditTransaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransactionDto {
    private UUID transactionId;
    private UUID userId;
    private UUID bookingId;
    private Integer amount;
    private TransactionType transactionType;
    private String description;
    private LocalDateTime createdAt;
}

