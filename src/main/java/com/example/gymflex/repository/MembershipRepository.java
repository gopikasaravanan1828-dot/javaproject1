package com.example.gymflex.repository;

import com.example.gymflex.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MembershipRepository
        extends JpaRepository<Membership, Long> {

    List<Membership> findByEndDateBetweenOrderByEndDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );
}