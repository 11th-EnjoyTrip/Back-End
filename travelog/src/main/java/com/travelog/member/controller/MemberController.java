package com.travelog.member.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.service.MemberService;
import com.travelog.member.util.JWTUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "member")
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    @Autowired
    public MemberController(MemberService memberService, JWTUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    public List<MemberDto> all() throws Exception {
        return memberService.getMembers();
    }

    @PostMapping("/regist")
    @Transactional
    public ResponseEntity<?> regist(@RequestBody MemberDto registMemberDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
//        status = memberService.regist(registMemberDto);
        try {
            status = memberService.regist(registMemberDto);
            if (status == HttpStatus.CREATED) {
                resultMap.put("message", "success");
            } else if (status == HttpStatus.CONFLICT) {
                resultMap.put("message", "duplicate");
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody MemberDto loginMemberDto, HttpSession session)throws Exception{
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status;
//        try{
//            MemberDto memberDto = memberService.login(loginMemberDto.getId(), loginMemberDto.getPassword());
//            if(memberDto == null){
//                throw new Exception("로그인 실패");
//            }
//            session.setAttribute("id",memberDto);
//            System.out.println(session.getId());
//            resultMap.put("message", "success");
//            status = HttpStatus.OK;
//        }catch(Exception e){
//            resultMap.put("message", e.getMessage());
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(resultMap, status);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto loginMemberDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            MemberDto loginUser = memberService.login(loginMemberDto);
            if(loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getId());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());

                memberService.saveRefreshToken(loginUser.getId(), refreshToken);

                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);

                status = HttpStatus.CREATED;
            }else{
                resultMap.put("message", "ID or Password incorrect");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/info/{memberId}")
    public ResponseEntity<?> getinfo(@PathVariable String memberId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            MemberDto memberDto = memberService.getById(memberId);
            resultMap.put("memberInfo", memberDto);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
