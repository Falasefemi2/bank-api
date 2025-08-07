package com.femi.bankingsource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PinRequest {
    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 4, message = "PIN must be 4 digits")
    private String pin;
}