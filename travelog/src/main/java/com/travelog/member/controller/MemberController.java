package com.travelog.member.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.service.MemberService;
import com.travelog.member.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

    // 회원 목록 조회
    @GetMapping("/all")
    public List<MemberDto> all() throws Exception {
        return memberService.getMembers();
    }

    // 회원 가입
    @PostMapping("/regist")
    @Transactional
    public ResponseEntity<?> regist(@RequestBody MemberDto registMemberDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
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

    // 로그인
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
    // 로그아웃
    @GetMapping("/logout/{id}")
    public ResponseEntity<?> removeToken(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            memberService.deleteRefreshToken(id);
            resultMap.put("message", "SUCCESS");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }
    // 회원 정보 조회
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getInfo(@PathVariable String id ,HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        if(jwtUtil.checkToken(request.getHeader("Authorization"))){
            try{
                MemberDto memberDto = memberService.getById(id);
                resultMap.put("info", memberDto);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", "ID or Password incorrect");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    // 닉네임 변경
    @GetMapping("/updateNickname/{nickname}")
    public ResponseEntity<?> updateEmail(@PathVariable("nickname") String nickname ,HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if(jwtUtil.checkToken(request.getHeader("Authorization"))){
            try{
                if(memberService.getByNickName(nickname) !=null){
                    resultMap.put("message", "중복되는 닉네임");
                    status = HttpStatus.CONFLICT;
                    return new ResponseEntity<>(resultMap, status);
                }
                StringTokenizer st = new StringTokenizer(request.getHeader("Authorization"));
                String id = memberService.getByToken(st.nextToken()).getId();

                memberService.updateNickname(nickname, id);
                resultMap.put("message", "닉네임 변경 완료");
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", "Token 인증 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    // 비밀번호 변경
    @GetMapping("/updatePassword/{password}")
    public ResponseEntity<?> updatePassword(@PathVariable("password") String password ,HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if(jwtUtil.checkToken(request.getHeader("Authorization"))){
            try{
                StringTokenizer st = new StringTokenizer(request.getHeader("Authorization"));
                String id = memberService.getByToken(st.nextToken()).getId();

                memberService.updatePassword(password, id);
                resultMap.put("message", "비빌번호 변경 완료");
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", "Token 인증 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 회원탈퇴
    @RequestMapping("/deleteMember")
    public ResponseEntity<?> deleteMember(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if(jwtUtil.checkToken(request.getHeader("Authorization"))){
            try{
                StringTokenizer st = new StringTokenizer(request.getHeader("Authorization"));
                String id = memberService.getByToken(st.nextToken()).getId();

                memberService.deleteMember(id);
                resultMap.put("message", "회원 탈퇴 완료");
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", "Token 인증 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 비밀번호 찾기
    @PostMapping("/findPwd")
    public ResponseEntity<?> findPwd(@RequestBody MemberDto findPwdMemberDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try{
            String pwd = memberService.getPassword(findPwdMemberDto.getId(), findPwdMemberDto.getEmail());
            if(pwd != null){
                resultMap.put("pwd", pwd);
                status = HttpStatus.FOUND;
            }else{
                resultMap.put("message", "비밀번호를 찾을 수 없습니다.");
                status = HttpStatus.NOT_FOUND;
            }
        }catch (Exception e){
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 아이디 중복 확인
    @GetMapping("/checkId/{id}")
    public ResponseEntity<?> checkId(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try{
            if(memberService.getById(id) == null){
                resultMap.put("message", "사용 가능한 아이디");
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", "중복되는 아이디");
                status = HttpStatus.CONFLICT;
            }
        }catch (Exception e){
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 이메일 중복 확인
    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try{
            if(memberService.getByEmail(email) == null){
                resultMap.put("message", "사용 가능한 이메일");
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", "중복되는 이메일");
                status = HttpStatus.CONFLICT;
            }
        }catch (Exception e){
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 닉네임 중복 확인
    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable("nickname") String nickname) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try{
            if(memberService.getByNickName(nickname) == null){
                resultMap.put("message", "사용 가능한 닉네임");
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", "중복되는 닉네임");
                status = HttpStatus.CONFLICT;
            }
        }catch (Exception e){
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
