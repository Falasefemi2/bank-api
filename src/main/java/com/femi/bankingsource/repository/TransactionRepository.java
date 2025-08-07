package com.femi.bankingsource.repository;

import com.femi.bankingsource.model.Account;
import com.femi.bankingsource.model.Transaction;
import com.femi.bankingsource.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);

    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:startDate IS NULL OR t.timestamp >= :startDate) AND " +
            "(:endDate IS NULL OR t.timestamp <= :endDate)")
    List<Transaction> findByAccountAndFilters(
            @Param("account") Account account,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}