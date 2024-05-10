package com.travelog.member.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value= "member")
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @GetMapping()
    public MemberDto member() {
        MemberDto memberDto = new MemberDto("test", "test", "", "test@gamil.com", "");
        return memberDto;
    }

    @GetMapping("/all")
    public List<MemberDto> all() {
        List<MemberDto> memberDtos = new ArrayList<>();
        List<MemberDto> test = memberService.getMembers();
        for(MemberDto memberDto : test) {
            System.out.println(memberDto.toString());
        }
        memberDtos.add(new MemberDto("test", "test", "", "test@gamil.com", ""));
        return test;
    }

}
