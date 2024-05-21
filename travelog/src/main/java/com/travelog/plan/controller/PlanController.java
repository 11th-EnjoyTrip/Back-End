package com.travelog.plan.controller;

import com.travelog.member.util.JWTUtil;
import com.travelog.plan.dto.PlanLikeRequest;
import com.travelog.plan.dto.PlanRequestDto;
import com.travelog.plan.dto.PlanResponseDto;
import com.travelog.plan.exception.InvalidException;
import com.travelog.plan.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
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

    //여행 계획 등록 (POST)
    @PostMapping("")
    public ResponseEntity<?> insertTripPlan(@RequestBody PlanRequestDto planRequestDto, HttpServletRequest request){
        // 사용자 인증 로직
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                // request token -> id
                // 토큰에서 사용자 id 가져옴
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));

                //TODO : 여행 계획 등록 (service)
                planService.insertPlan(planRequestDto, id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (InvalidException e) {
                return ResponseEntity.badRequest().body("유효하지 않은 관광지 입니다.");
            } catch (HttpMessageNotReadableException e) {
                return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //여행 계획 리스트 조회 ( 공유한 리스트만 조회 ) (GET)
    @GetMapping("/shared")
    public ResponseEntity<?> getSharedTripPlans(@RequestParam String keyword,@PageableDefault(size=6) Pageable pageable,HttpServletRequest request) {

        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 공유된 리스트 조회
                Page<Map<String, Object>> sharedPlans = planService.getSharedPlanList(pageable, id,keyword);
                return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    //TODO : 여행 계획 공유 여부 변경 (PATCH)
//    @PatchMapping("/shared/{tripPlanId}")
//    public ResponseEntity<?> updateIsSharedPlan(@PathVariable("tripPlanId") Long tripPlanId, HttpServletRequest request){
//
//    }

    //여행 계획 상세 조회(개별) (GET)
    @GetMapping("/{tripPlanId}")
    public ResponseEntity<?> getDetailPlan(@PathVariable("tripPlanId") Long tripPlanId,HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 여행 계획 상세 조회
                PlanResponseDto plan = planService.getDetailPlan(tripPlanId, id);
                return new ResponseEntity<>(plan, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace(); // 스택 트레이스 출력
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //좋아요한 여행 계획 리스트 조회 (GET)
    @GetMapping("/like")
    public ResponseEntity<?> getLikeTripPlans(@PageableDefault(size=6) Pageable pageable,HttpServletRequest request) {
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 공유된 리스트 조회
                Page<Map<String, Object>> sharedPlans = planService.getLikeTripPlans(id, pageable);
                return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //내가 만든 여행 계획 리스트 조회 (GET)
    @GetMapping("")
    public ResponseEntity<?> getMyTripPlans(@PageableDefault(size=6) Pageable pageable,HttpServletRequest request) {
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 공유된 리스트 조회
                Page<Map<String, Object>> sharedPlans = planService.getMyTripPlans(id, pageable);
                return new ResponseEntity<>(sharedPlans.getContent(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //여행 계획 삭제 (DELETE)
    @DeleteMapping("/{tripPlanId}")
    public ResponseEntity<?> deleteTripPlan(@PathVariable("tripPlanId") int tripPlanId,HttpServletRequest request) {
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                planService.deleteTripPlan(tripPlanId, id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (InvalidException e) {
                return ResponseEntity.badRequest().body("해당 유저가 작성한 계획이 아닙니다.");
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //여행 상세 계획 수정 (PUT)
    @PutMapping("/{tripPlanId}")
    public ResponseEntity<?>updateDetailPlan(@PathVariable("tripPlanId") Long tripPlanId,
                                             @RequestBody PlanRequestDto planRequestDto,HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                // String id = jwtUtil.getUserId(request.getHeader("Authorization"));

                // 여행 계획 상세 수정
                planService.updatePlan(tripPlanId, planRequestDto);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //여행 계획 좋아요 등록
    @PostMapping("/like")
    public ResponseEntity<?> insertPlanLike(@RequestBody PlanLikeRequest planLikeRequest,HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 여행 계획 상세 수정
                planService.insertPlanLike(planLikeRequest.getTripPlanId(), id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //여행 계획 좋아요 삭제
    @DeleteMapping("/like")
    public ResponseEntity<?> deletePlanLike(@RequestBody PlanLikeRequest planLikeRequest,HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String id = jwtUtil.getUserId(request.getHeader("Authorization"));
                // 여행 계획 상세 수정
                planService.deletePlanLike(planLikeRequest.getTripPlanId(), id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
