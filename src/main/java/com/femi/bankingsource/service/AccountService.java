package com.femi.bankingsource.service;

import com.femi.bankingsource.dto.*;
import com.femi.bankingsource.model.Account;
import com.femi.bankingsource.model.Transaction;
import com.femi.bankingsource.model.TransactionType;
import com.femi.bankingsource.model.User;
import com.femi.bankingsource.repository.AccountRepository;
import com.femi.bankingsource.repository.TransactionRepository;
import com.femi.bankingsource.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse createAccount(AccountRequest accountRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Username from auth context: " + username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (accountRepository.findByAccountNumber(accountRequest.getAccountNumber()).isPresent()) {
            throw new RuntimeException("Account number already exists");
        }

        Account account = Account.builder()
                .user(user)
                .accountNumber(accountRequest.getAccountNumber())
                .balance(BigDecimal.ZERO)
                .pin(passwordEncoder.encode("0000")) // default pin
                .build();
        account = accountRepository.save(account);
        return mapToAccountResponse(account);
    }

    public void updatePin(Long accountId, PinRequest request) {
        Account account = getUserAccount(accountId);
        account.setPin(passwordEncoder.encode(request.getPin()));
        accountRepository.save(account);
    }


    @Transactional
    public TransactionResponse deposit(Long accountId, TransactionRequest request) {
        Account account = getUserAccount(accountId);
        validatePin(account, request.getPin());

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .description(request.getDescription())
                .build();
        transaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, TransactionRequest request) {
        Account account = getUserAccount(accountId);
        validatePin(account, request.getPin());

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .description(request.getDescription())
                .build();
        transaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse transfer(Long accountId, TransactionRequest request) {
        Account sourceAccount = getUserAccount(accountId);
        validatePin(sourceAccount, request.getPin());

        Account targetAccount = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        targetAccount.setBalance(targetAccount.getBalance().add(request.getAmount()));
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = Transaction.builder()
                .account(sourceAccount)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .description(request.getDescription())
                .targetAccount(targetAccount)
                .build();
        transaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(transaction);
    }

    public List<TransactionResponse> getTransactionHistory(Long accountId, TransactionType type,
                                                           LocalDateTime startDate, LocalDateTime endDate) {
        Account account = getUserAccount(accountId);
        List<Transaction> transactions = transactionRepository.findByAccountAndFilters(account, type, startDate, endDate);
        return transactions.stream().map(this::mapToTransactionResponse).collect(Collectors.toList());
    }

    private Account getUserAccount(Long accountId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!account.getUser().getUsername().equals(username) &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") ||
                                auth.getAuthority().equals("ROLE_SUPPORT"))) {
            throw new RuntimeException("Unauthorized access to account");
        }
        return account;
    }

    private void validatePin(Account account, String pin) {
        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new RuntimeException("Invalid PIN");
        }
    }

    private AccountResponse mapToAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        return response;
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setTimestamp(transaction.getTimestamp());
        response.setDescription(transaction.getDescription());
        response.setTargetAccountNumber(transaction.getTargetAccount() != null ?
                transaction.getTargetAccount().getAccountNumber() : null);
        return response;
    }
}