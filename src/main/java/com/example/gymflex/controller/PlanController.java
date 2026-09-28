package com.example.gymflex.controller;

import com.example.gymflex.entity.Plan;
import com.example.gymflex.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plan createPlan(
            @Valid @RequestBody Plan plan) {

        return planService.createPlan(plan);
    }

    @GetMapping
    public List<Plan> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/{id}")
    public Plan getPlan(@PathVariable Long id) {
        return planService.getPlan(id);
    }

    @PutMapping("/{id}")
    public Plan updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody Plan plan) {

        return planService.updatePlan(id, plan);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
    }
}