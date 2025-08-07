package com.femi.bankingsource.dto;

import com.femi.bankingsource.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
    private String targetAccountNumber;
}