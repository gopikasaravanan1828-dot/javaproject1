package com.example.gymflex.service;

import com.example.gymflex.entity.Plan;
import com.example.gymflex.exception.ResourceNotFoundException;
import com.example.gymflex.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan getPlan(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with id: " + id));
    }

    public Plan updatePlan(Long id, Plan updatedPlan) {

        Plan plan = getPlan(id);

        plan.setName(updatedPlan.getName());
        plan.setDuration(updatedPlan.getDuration());
        plan.setPrice(updatedPlan.getPrice());

        return planRepository.save(plan);
    }

    public void deletePlan(Long id) {

        Plan plan = getPlan(id);

        planRepository.delete(plan);
    }
}