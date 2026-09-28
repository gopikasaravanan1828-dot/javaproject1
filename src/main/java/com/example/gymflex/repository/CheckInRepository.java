package com.example.gymflex.repository;

import com.example.gymflex.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CheckInRepository
        extends JpaRepository<CheckIn, Long> {

    long countByMembership_Member_IdAndCheckInDateBetween(
            Long memberId,
            LocalDate startDate,
            LocalDate endDate
    );
}