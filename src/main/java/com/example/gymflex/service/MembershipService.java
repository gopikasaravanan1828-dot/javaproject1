package com.example.gymflex.service;

import com.example.gymflex.dto.CheckInRequest;
import com.example.gymflex.dto.RenewRequest;
import com.example.gymflex.entity.CheckIn;
import com.example.gymflex.entity.Membership;
import com.example.gymflex.entity.Plan;
import com.example.gymflex.exception.BusinessException;
import com.example.gymflex.exception.ResourceNotFoundException;
import com.example.gymflex.repository.CheckInRepository;
import com.example.gymflex.repository.MembershipRepository;
import com.example.gymflex.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PlanRepository planRepository;
    private final CheckInRepository checkInRepository;

    public MembershipService(
            MembershipRepository membershipRepository,
            PlanRepository planRepository,
            CheckInRepository checkInRepository) {

        this.membershipRepository = membershipRepository;
        this.planRepository = planRepository;
        this.checkInRepository = checkInRepository;
    }

    public Membership renewMembership(
            Long membershipId,
            RenewRequest request) {

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Membership not found with ID: " + membershipId));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with ID: " + request.getPlanId()));

        LocalDate oldEndDate = membership.getEndDate();

        LocalDate newEndDate =
                calculateEndDate(oldEndDate, plan.getDuration());

        membership.setPlan(plan);
        membership.setEndDate(newEndDate);

        return membershipRepository.save(membership);
    }

    public CheckIn checkIn(CheckInRequest request) {

        Membership membership =
                membershipRepository.findById(request.getMembershipId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership not found with ID: "
                                                + request.getMembershipId()));

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        if (membership.getEndDate().isBefore(today)) {
            throw new BusinessException(
                    "Check-in rejected. Membership has expired.");
        }

        CheckIn checkIn = new CheckIn(
                membership,
                today
        );

        return checkInRepository.save(checkIn);
    }

    public List<Membership> getExpiringMemberships() {

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        LocalDate sevenDaysLater = today.plusDays(7);

        return membershipRepository
                .findByEndDateBetweenOrderByEndDateAsc(
                        today,
                        sevenDaysLater
                );
    }

    public long getCurrentMonthAttendance(Long memberId) {

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        LocalDate firstDay =
                today.withDayOfMonth(1);

        LocalDate lastDay =
                today.withDayOfMonth(today.lengthOfMonth());

        return checkInRepository
                .countByMembership_Member_IdAndCheckInDateBetween(
                        memberId,
                        firstDay,
                        lastDay
                );
    }

    private LocalDate calculateEndDate(
            LocalDate startDate,
            String duration) {

        switch (duration.toUpperCase()) {

            case "MONTHLY":
                return startDate.plusMonths(1);

            case "QUARTERLY":
                return startDate.plusMonths(3);

            case "YEARLY":
                return startDate.plusYears(1);

            default:
                throw new IllegalArgumentException(
                        "Invalid plan duration: " + duration);
        }
    }
}