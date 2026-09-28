package com.example.gymflex.dto;

import jakarta.validation.constraints.NotNull;

public class RenewRequest {

    @NotNull(message = "Plan ID is required")
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}