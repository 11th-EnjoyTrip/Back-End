package com.travelog.plan.controller;

import com.travelog.member.util.JWTUtil;
import com.travelog.plan.dto.PlanRequestDto;
import com.travelog.plan.dto.PlanResponseDto;
import com.travelog.plan.exception.InvalidException;
import com.travelog.plan.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/plan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlanController {

    private final JWTUtil jwtUtil;
    private final PlanService planService;

    @Autowired
    public PlanController(JWTUtil jwtUtil, PlanService planService) {
        this.jwtUtil = jwtUtil;
        this.planService = planService;
    }

    //TODO : 여행 계획 등록 (POST)
    @PostMapping("")
    public ResponseEntity<?> insertTripPlan(@RequestBody PlanRequestDto planRequestDto){
        try {
            //TODO : 여행 계획 등록 (service)
            planService.insertPlan(planRequestDto, Integer.parseInt("1"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            return ResponseEntity.badRequest().body("유효하지 않은 관광지 입니다.");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        // 사용자 인증 로직
//        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
//            try {
//                // request token -> id
//                // 토큰에서 사용자 id 가져옴
//                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
//
//
//            } catch (Exception e) {
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

    //TODO : 여행 계획 리스트 조회 ( 공유한 리스트만 조회 ) (GET)
    @GetMapping("/shared")
    public ResponseEntity<?> getSharedTripPlans(@PageableDefault(size=6) Pageable pageable) {
        try {
            // 공유된 리스트 조회
            Page<Map<String, Object>> sharedPlans = planService.getSharedPlanList(pageable);
            return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO : 여행 계획 상세 조회(개별) (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?>getDetailPlan(@PathVariable("id") Long id){
        try {
            // 여행 계획 상세 조회
            PlanResponseDto plan = planService.getDetailPlan(id);
            return new ResponseEntity<>(plan, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO : 내가 좋아요 누른 경로 목록 조회 (GET)
    @GetMapping("/like")
    public ResponseEntity<?> getLikeTripPlans(@PageableDefault(size=6) Pageable pageable) {
        try {
            // 공유된 리스트 조회
            Page<Map<String, Object>> sharedPlans = planService.getLikeTripPlans(Integer.parseInt("1"),pageable);
            return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO : 내가 만든 경로 목록 (GET)
    @GetMapping("")
    public ResponseEntity<?> getMyTripPlans(@PageableDefault(size=6) Pageable pageable) {
        try {
            // 공유된 리스트 조회
            Page<Map<String, Object>> sharedPlans = planService.getMyTripPlans(Integer.parseInt("1"),pageable);
            return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO : 여행 계획 삭제 (삭제시, 좋아요 테이블에서 경로id로 지정된 것들 전체 삭제 ) (DELETE)
    @DeleteMapping("/{tripPlanId}")
    public ResponseEntity<Void> deleteTripPlan(@PathVariable("tripPlanId") int tripPlanId) {
        try{
            planService.deleteTripPlan(tripPlanId, Integer.parseInt("1"));
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO : 여행 상세 계획 수정 ( 서비스 로직 흐름 복잡 ) (PUT)
    @PutMapping("/{tripPlanId}")
    public ResponseEntity<?>updateDetailPlan(@PathVariable("tripPlanId") Long tripPlanId,
                                             @RequestBody PlanRequestDto planRequestDto){
        try {
            // 여행 계획 상세 수정
            planService.updatePlan(tripPlanId,planRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
