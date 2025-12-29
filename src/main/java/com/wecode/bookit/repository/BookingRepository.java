package com.wecode.bookit.repository;

import com.wecode.bookit.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByRoomId(UUID roomId);
    Optional<Booking> findByBookingId(UUID bookingId);
    List<Booking> findByUserIdAndStatus(UUID userId, String status);
    List<Booking> findByUserIdAndMeetingDate(UUID userId, LocalDate meetingDate);
    List<Booking> findByUserIdAndMeetingDateAndStatus(UUID userId, LocalDate meetingDate, String status);
}

