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

    // RENEW MEMBERSHIP
    public Membership renewMembership(
            Long membershipId,
            RenewRequest request) {

        Membership membership =
                membershipRepository.findById(membershipId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership not found with id: "
                                                + membershipId));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with id: "
                                        + request.getPlanId()));

        LocalDate currentEndDate = membership.getEndDate();

        LocalDate newEndDate =
                calculateEndDate(
                        currentEndDate,
                        plan.getDuration()
                );

        membership.setPlan(plan);
        membership.setEndDate(newEndDate);

        return membershipRepository.save(membership);
    }

    // CHECK-IN
    public CheckIn checkIn(CheckInRequest request) {

        Membership membership =
                membershipRepository.findById(
                        request.getMembershipId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Membership not found with id: "
                                        + request.getMembershipId()));

        LocalDate today = LocalDate.now();

        // BUSINESS RULE:
        // Check-in is rejected if membership has expired.
        if (membership.getEndDate().isBefore(today)) {

            throw new BusinessException(
                    "Check-in rejected. Membership has expired."
            );
        }

        CheckIn checkIn = new CheckIn(
                membership,
                today
        );

        return checkInRepository.save(checkIn);
    }

    // MEMBERSHIPS EXPIRING IN NEXT 7 DAYS
    public List<Membership> getExpiringMemberships() {

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        return membershipRepository
                .findByEndDateBetweenOrderByEndDateAsc(
                        today,
                        sevenDaysLater
                );
    }

    // ATTENDANCE COUNT FOR CURRENT MONTH
    public long getCurrentMonthAttendance(Long memberId) {

        LocalDate today = LocalDate.now();

        LocalDate firstDay =
                today.withDayOfMonth(1);

        LocalDate lastDay =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );

        return checkInRepository
                .countByMembership_Member_IdAndCheckInDateBetween(
                        memberId,
                        firstDay,
                        lastDay
                );
    }

    private LocalDate calculateEndDate(
            LocalDate currentEndDate,
            String duration) {

        return switch (duration.toUpperCase()) {

            case "MONTHLY" ->
                    currentEndDate.plusMonths(1);

            case "QUARTERLY" ->
                    currentEndDate.plusMonths(3);

            case "YEARLY" ->
                    currentEndDate.plusYears(1);

            default ->
                    throw new IllegalArgumentException(
                            "Duration must be MONTHLY, QUARTERLY or YEARLY");
        };
    }
}