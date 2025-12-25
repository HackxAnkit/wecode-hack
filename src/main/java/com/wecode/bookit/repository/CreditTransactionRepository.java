package com.wecode.bookit.repository;

import com.wecode.bookit.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Integer> {

    List<CreditTransaction> findByUserId(Integer userId);

    List<CreditTransaction> findByBookingId(Integer bookingId);

    List<CreditTransaction> findByTransactionType(CreditTransaction.TransactionType transactionType);

    @Query("SELECT ct FROM CreditTransaction ct " +
            "WHERE ct.user.userId = :userId " +
            "AND ct.createdAt >= :startDate " +
            "AND ct.createdAt < :endDate " +
            "ORDER BY ct.createdAt DESC")
    List<CreditTransaction> findUserTransactionsBetween(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}