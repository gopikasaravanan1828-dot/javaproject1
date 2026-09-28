package com.example.gymflex.service;

import com.example.gymflex.dto.MemberRequest;
import com.example.gymflex.entity.Member;
import com.example.gymflex.entity.Membership;
import com.example.gymflex.entity.Plan;
import com.example.gymflex.exception.ResourceNotFoundException;
import com.example.gymflex.repository.MemberRepository;
import com.example.gymflex.repository.MembershipRepository;
import com.example.gymflex.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final MembershipRepository membershipRepository;

    public MemberService(
            MemberRepository memberRepository,
            PlanRepository planRepository,
            MembershipRepository membershipRepository) {

        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
    }

    public Membership registerMember(MemberRequest request) {

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with ID: " + request.getPlanId()));

        Member member = new Member(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        member = memberRepository.save(member);

        LocalDate startDate = LocalDate.now(ZoneId.systemDefault());
        LocalDate endDate = calculateEndDate(startDate, plan.getDuration());

        Membership membership = new Membership(
                member,
                plan,
                startDate,
                endDate
        );

        return membershipRepository.save(membership);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
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