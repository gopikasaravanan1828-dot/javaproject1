package com.example.gymflex.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "check_ins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    private LocalDate checkInDate;

    public CheckIn() {
    }

    public CheckIn(Membership membership, LocalDate checkInDate) {
        this.membership = membership;
        this.checkInDate = checkInDate;
    }

    public Long getId() {
        return id;
    }

    public Membership getMembership() {
        return membership;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
}