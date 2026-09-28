package com.example.gymflex.controller;

import com.example.gymflex.dto.MemberRequest;
import com.example.gymflex.entity.Member;
import com.example.gymflex.entity.Membership;
import com.example.gymflex.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Membership registerMember(
            @Valid @RequestBody MemberRequest request) {

        return memberService.registerMember(request);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }
}