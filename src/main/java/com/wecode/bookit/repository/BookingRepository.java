package com.wecode.bookit.repository;

import com.wecode.bookit.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUserId(Integer userId);

    List<Booking> findByRoomId(Integer roomId);

    List<Booking> findByStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
            "AND b.status = 'ACTIVE' " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime")
    List<Booking> findConflictingBookings(
            @Param("roomId") Integer roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT b FROM Booking b WHERE b.status = 'ACTIVE' " +
            "AND b.startTime >= :startDate " +
            "AND b.startTime < :endDate")
    List<Booking> findActiveBookingsBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.room " +
            "JOIN FETCH b.user " +
            "WHERE b.status = 'ACTIVE' " +
            "ORDER BY b.startTime")
    List<Booking> findAllActiveBookingsWithDetails();
}