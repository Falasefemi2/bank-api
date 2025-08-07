package com.femi.bankingsource.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
}