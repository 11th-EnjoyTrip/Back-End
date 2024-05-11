package com.travelog.member.controller;

import com.sun.net.httpserver.Authenticator;
import com.travelog.member.dto.MemberDto;
import com.travelog.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value= "member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/all")
    public List<MemberDto> all() throws Exception {
        return memberService.getMembers();
    }
    @GetMapping("/info/{memberId}")
    public ResponseEntity<Map<String, Object>> getinfo(@PathVariable String memberId) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        try{
            MemberDto memberDto =memberService.memberInfo(memberId);
            resultMap.put("memberInfo", memberDto);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        }catch(Exception e){

            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
