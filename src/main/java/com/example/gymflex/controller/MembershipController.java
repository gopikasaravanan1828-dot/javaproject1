package com.example.gymflex.controller;

import com.example.gymflex.dto.CheckInRequest;
import com.example.gymflex.dto.RenewRequest;
import com.example.gymflex.entity.CheckIn;
import com.example.gymflex.entity.Membership;
import com.example.gymflex.service.MembershipService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PutMapping("/{id}/renew")
    public Membership renewMembership(
            @PathVariable Long id,
            @Valid @RequestBody RenewRequest request) {

        return membershipService.renewMembership(id, request);
    }

    @PostMapping("/check-in")
    public CheckIn checkIn(
            @Valid @RequestBody CheckInRequest request) {

        return membershipService.checkIn(request);
    }

    @GetMapping("/expiring")
    public List<Membership> getExpiringMemberships() {
        return membershipService.getExpiringMemberships();
    }

    @GetMapping("/attendance/{memberId}")
    public long getCurrentMonthAttendance(
            @PathVariable Long memberId) {

        return membershipService.getCurrentMonthAttendance(memberId);
    }
}