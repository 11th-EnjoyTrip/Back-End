package com.travelog.review.controller;

import com.travelog.member.util.JWTUtil;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "review")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "refreshToken", "Content-Type"})
public class ReviewController {
    private final ReviewService reviewService;
    private final JWTUtil jwtUtil;

    @Autowired
    public ReviewController(ReviewService reviewService, JWTUtil jwtUtil) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
    }

    // 리뷰 작성
    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody ReviewDto reviewDto, HttpServletRequest request) throws Exception {

        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // token 확인
            String token = request.getHeader("Authorization");
            if (jwtUtil.checkToken(token)) {
                // 작성
                reviewService.write(reviewDto);
                result.put("message", "SUCCESS");
                status = HttpStatus.OK;
            } else {
                result.put("message", "Token Error");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    // 관광지 리뷰 확인
    @GetMapping("/read/{content_id}")
    public ResponseEntity<?> read(@PathVariable String content_id) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {

        } catch (Exception e) {
            result.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(result, status);
    }

    // 내가 작성한 리뷰 확인
    @GetMapping("/written")
    public ResponseEntity<?> read(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {

                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                List<ReviewDto> list = reviewService.written(userid);
                result.put("message", "SUCCESS");
                result.put("data", list);
                status = HttpStatus.OK;
            } catch (Exception e) {
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            result.put("message", "Token Error");
            status = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(result, status);
    }

    // Update
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody String text, String content_id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                String writer = reviewService.getIdByContent_id(content_id);
                if(userid.equals(writer)){
                    reviewService.update(text, content_id);
                    result.put("message", "SUCCESS");
                    status = HttpStatus.OK;
                }else{
                    result.put("message", "본인이 작성한 리뷰가 아닙니다.");
                    status = HttpStatus.BAD_REQUEST;
                }
            } catch (Exception e) {
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            result.put("message", "Token Error");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(result, status);
    }
    // Delete
}
