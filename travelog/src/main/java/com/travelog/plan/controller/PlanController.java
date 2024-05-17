package com.travelog.plan.controller;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlanController {

    private final JWTUtil jwtUtil;

    @Autowired
    public PlanController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //TODO : 여행 계획 등록 (POST)
    @PostMapping("")
    public ResponseEntity<?> insertTripPlan(HttpServletRequest request){

        // 사용자 인증 로직
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                // request token -> id
                // 토큰에서 사용자 id 가져옴
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                //TODO : 여행 계획 등록 (service)

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //TODO : 여행 계획 리스트 조회 ( 공유한 리스트만 조회 ) (GET)

    //TODO : 여행 계획 상세 조회(개별) (GET)

    //TODO : 여행 상세 계획 수정 ( 서비스 로직 흐름 복잡 ) (PUT)

    //TODO : 여행 계획 삭제 (삭제시, 좋아요 테이블에서 경로id로 지정된 것들 전체 삭제 ) (DELETE)

    //TODO : 내가 좋아요 누른 경로 목록 (GET)

    //TODO : 내가 만든 경로 목록 (GET)

    //TODO : 여행 경로 좋아요 추가 (경로id 가져오면서 유효한지 확인) (POST)

    //TODO : 여행 경로 좋아요 삭제 (DELETE)
}
