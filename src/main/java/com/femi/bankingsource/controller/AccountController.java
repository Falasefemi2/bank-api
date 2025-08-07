package com.femi.bankingsource.controller;

import com.femi.bankingsource.dto.*;
import com.femi.bankingsource.model.TransactionType;
import com.femi.bankingsource.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/{id}/pin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updatePin(@PathVariable Long id, @Valid @RequestBody PinRequest request) {
        accountService.updatePin(id, request);
        return ResponseEntity.ok("PIN updated successfully");
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(accountService.deposit(id, request));
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(accountService.withdraw(id, request));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponse> transfer(@RequestParam Long accountId, @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(accountService.transfer(accountId, request));
    }

    @GetMapping("/{id}/transactions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPPORT')")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @PathVariable Long id,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(accountService.getTransactionHistory(id, type, startDate, endDate));
    }
}