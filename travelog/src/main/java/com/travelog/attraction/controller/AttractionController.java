package com.travelog.attraction.controller;

import com.travelog.attraction.dto.AttractionLikeRequest;
import com.travelog.attraction.dto.AttractionRequestDto;
import com.travelog.attraction.service.AttractionService;
import com.travelog.member.util.JWTUtil;
import com.travelog.plan.dto.PlanLikeRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/attraction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AttractionController {

    private final AttractionService attractionService;
    private final JWTUtil jwtUtil;

    public AttractionController(AttractionService attractionService, JWTUtil jwtUtil) {
        this.attractionService = attractionService;
        this.jwtUtil = jwtUtil;
    }


    // 관광지 카테고리,지역,키워드별 조회(페이지네이션)
    @GetMapping("")
    public ResponseEntity<?> getAttractionList(@ModelAttribute AttractionRequestDto attractionRequestDto, @PageableDefault(size=20) Pageable pageable, HttpServletRequest request) {
        // # request에 토큰 값 있는지 체크(로그인한 회원 인지 비회원이지 확인)
        // 비회원인 경우
        if(request.getHeader("Authorization") == null) {
            try{
                return ResponseEntity.ok(attractionService.getAttractionList(attractionRequestDto, pageable,null).getContent()) ;
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
            }
        }else{ // 회원일 경우
            if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
                try{
                    String id=  jwtUtil.getUserId(request.getHeader("Authorization"));
                    return ResponseEntity.ok(attractionService.getAttractionList(attractionRequestDto, pageable,id).getContent()) ;
                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
                }
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    // 관광지 상세 조회
    @GetMapping("/{contentId}")
    public ResponseEntity<?> getAttractionDetail(@PathVariable("contentId") int contentId, HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            try{
                return ResponseEntity.ok(attractionService.getAttractionDetail(contentId,null));
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
            }
        }else{ // 회원일 경우
            if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
                try{
                    String id=  jwtUtil.getUserId(request.getHeader("Authorization"));
                    return ResponseEntity.ok(attractionService.getAttractionDetail(contentId,id));
                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
                }
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    //관광지 좋아요 등록
    @PostMapping("/like")
    public ResponseEntity<?> insertAttractionLike(@RequestBody AttractionLikeRequest attractionLikeRequest, HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try{
                String id=jwtUtil.getUserId(request.getHeader("Authorization"));
                attractionService.insertAttractionLike(attractionLikeRequest.getContentId(), id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //관광지 좋아요 삭제
    @DeleteMapping("/like")
    public ResponseEntity<?> deleteAttractionLike(@RequestBody AttractionLikeRequest attractionLikeRequest,HttpServletRequest request){
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try{
                String id=jwtUtil.getUserId(request.getHeader("Authorization"));
                attractionService.deleteAttractionLike(attractionLikeRequest.getContentId(), id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
