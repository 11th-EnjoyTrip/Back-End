package com.travelog.member.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.dto.ResponseMemberDto;
import com.travelog.member.service.MemberService;
import com.travelog.member.service.MemberServiceImpl;
import com.travelog.member.util.JWTUtil;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@RestController
@RequestMapping(value = "member")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "refreshToken", "Content-Type"})
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final MemberServiceImpl memberServiceImpl;

    @Autowired
    public MemberController(MemberService memberService, JWTUtil jwtUtil, MemberServiceImpl memberServiceImpl) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.memberServiceImpl = memberServiceImpl;
    }

    // 회원 목록 조회
    @GetMapping("/all")
    public List<ResponseMemberDto> all() throws Exception {
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
            ResponseMemberDto loginUser = memberService.login(loginMemberDto);
            if (loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getUserid());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserid());

                memberService.saveRefreshToken(loginUser.getUserid(), refreshToken);

                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);

                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "ID or Password incorrect");
                status = HttpStatus.NOT_FOUND;
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
            String id = jwtUtil.getUserId(request.getHeader("Authorization"));
            memberService.deleteRefreshToken(id);
            resultMap.put("message", "SUCCESS");
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        // api로 받은 token
        String token = request.getHeader("refreshToken");
        // token에 담긴 id값
        String id = jwtUtil.getUserId(token);
        if (!id.equals("fail") && jwtUtil.checkToken(token) && jwtUtil.checkToken(memberService.getToken(id))) {
            String accessToken = jwtUtil.createAccessToken(id);

            resultMap.put("access-token", accessToken);
            resultMap.put("message", "success");
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = new TreeMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                // 1. 회원 정보
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                ResponseMemberDto memberDto = memberService.getResponseMemberDtoById(id);
                resultMap.put("message", "success");
                resultMap.put("info", memberDto);
                // 2. 좋아요 정보
                List<ResponseReviewDto[]> result = memberServiceImpl.getReviewLikeByUserid(id);
                resultMap.put("reviews", result);

                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            resultMap.put("message", "ID or Password incorrect");
            status = HttpStatus.UNAUTHORIZED;
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

                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
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

    // 비밀번호 일치 확인
    @PostMapping("/checkpassword")
    public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> password, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String updatePassword = password.get("password");

        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                String originalPassword = memberService.getMemberDtoById(id).getPassword();

                if(originalPassword.equals(updatePassword)) {
                    resultMap.put("message", "비밀번호 일치");
                    status = HttpStatus.OK;
                } else {
                    resultMap.put("message", "비밀번호 불일치");
                    status = HttpStatus.NOT_FOUND;
                }
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
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));

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
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));

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
            String pwd = memberService.getPassword(findPwdMemberDto.getUserid(), findPwdMemberDto.getEmail());
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
            if (memberService.getResponseMemberDtoById(id) == null) {
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
