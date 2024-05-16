package com.travelog.member.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.service.MemberService;
import com.travelog.member.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
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
@CrossOrigin(origins = "*", allowedHeaders = "Authorization")
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
            if (loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getId());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());

                memberService.saveRefreshToken(loginUser.getId(), refreshToken);

                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);

                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "ID or Password incorrect");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> removeToken(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            String id = memberService.getByToken(request.getHeader("Authorization")).getId();
            memberService.deleteRefreshToken(id);
            resultMap.put("message", "SUCCESS");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody MemberDto refreshMemberDto, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String token = request.getHeader("Authorization");
        if (jwtUtil.checkToken(token) && token.equals(memberService.getToken(refreshMemberDto.getId()))) {
            String accessToken = jwtUtil.createAccessToken(refreshMemberDto.getId());

            resultMap.put("access-token", accessToken);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        // 1. token 일치 확인
        // 2.
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = memberService.getByToken(request.getHeader("Authorization")).getId();
                MemberDto memberDto = memberService.getById(id);
                memberDto.setPassword("");
                memberDto.setToken("");
                resultMap.put("info", memberDto);
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
////            String id = memberService.getByToken(request.getHeader("Authorization")).getId();
////            // 1. refresh token 유효성 검증
////            if(jwtUtil.checkToken(memberService.getById(id).getToken())){
////                // 1-2 refresh = true
////                // 1-2-1 token 재발급
////                String accessToken = jwtUtil.createAccessToken(id);
////                String refreshToken = jwtUtil.createRefreshToken(id);
////
////                memberService.saveRefreshToken(id, refreshToken);
////
////                resultMap.put("access-token", accessToken);
//////                resultMap.put("refresh-token", refreshToken);
////
////                status = HttpStatus.CREATED;
////            }
//            else{
//                // 1-1 refresh = false
//                // 1-1-1 디짐
            resultMap.put("message", "ID or Password incorrect");
            status = HttpStatus.UNAUTHORIZED;
//            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 닉네임 변경
    @PatchMapping("/updateNickname")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> nickname, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String updateNickname = nickname.get("nickname");
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                if (memberService.getByNickName(updateNickname) != null) {
                    resultMap.put("message", "중복되는 닉네임");
                    status = HttpStatus.CONFLICT;
                    return new ResponseEntity<>(resultMap, status);
                }

                String id = memberService.getByToken(request.getHeader("Authorization")).getId();
                System.out.println(updateNickname);
                memberService.updateNickname(updateNickname, id);
                resultMap.put("message", "닉네임 변경 완료");
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            resultMap.put("message", "Token 인증 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 비밀번호 변경
    @PatchMapping("/updatePassword/{password}")
    public ResponseEntity<?> updatePassword(@PathVariable("password") String password, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = memberService.getByToken(request.getHeader("Authorization")).getId();

                memberService.updatePassword(password, id);
                resultMap.put("message", "비빌번호 변경 완료");
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            resultMap.put("message", "Token 인증 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 회원탈퇴
    @DeleteMapping("/deleteMember")
    public ResponseEntity<?> deleteMember(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = memberService.getByToken(request.getHeader("Authorization")).getId();

                memberService.deleteMember(id);
                resultMap.put("message", "회원 탈퇴 완료");
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
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
        try {
            String pwd = memberService.getPassword(findPwdMemberDto.getId(), findPwdMemberDto.getEmail());
            if (pwd != null) {
                resultMap.put("pwd", pwd);
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", "비밀번호를 찾을 수 없습니다.");
                status = HttpStatus.NOT_FOUND;
            }
        } catch (Exception e) {
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
        try {
            if (memberService.getById(id) == null) {
                resultMap.put("message", "사용 가능한 아이디");
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", "중복되는 아이디");
                status = HttpStatus.CONFLICT;
            }
        } catch (Exception e) {
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
        try {
            if (memberService.getByEmail(email) == null) {
                resultMap.put("message", "사용 가능한 이메일");
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", "중복되는 이메일");
                status = HttpStatus.CONFLICT;
            }
        } catch (Exception e) {
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
        try {
            if (memberService.getByNickName(nickname) == null) {
                resultMap.put("message", "사용 가능한 닉네임");
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", "중복되는 닉네임");
                status = HttpStatus.CONFLICT;
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
