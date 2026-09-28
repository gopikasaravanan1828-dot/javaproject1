package com.example.gymflex.dto;

import jakarta.validation.constraints.NotNull;

public class CheckInRequest {

    @NotNull(message = "Membership ID is required")
    private Long membershipId;

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }
}