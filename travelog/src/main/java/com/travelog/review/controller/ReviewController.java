package com.travelog.review.controller;

import com.travelog.member.service.MemberServiceImpl;
import com.travelog.member.util.JWTUtil;
import com.travelog.review.dto.MyPageReviewDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.UpdateReviewDto;
import com.travelog.review.service.ReviewService;
import com.travelog.review.service.ReviewServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
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
    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewController(ReviewService reviewService, JWTUtil jwtUtil, MemberServiceImpl memberServiceImpl, ReviewServiceImpl reviewServiceImpl) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
        this.reviewServiceImpl = reviewServiceImpl;
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
    public ResponseEntity<?> read(@PathVariable String content_id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                List<MyPageReviewDto[]> list = reviewService.getReviewsByContentId(content_id);
                result.put("message", "SUCCESS");
                result.put("reviews", list);
                status = HttpStatus.OK;

            } catch (Exception e) {
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            try {
                List<ResponseReviewDto[]> list = reviewService.getResponseReviewsByContentId(content_id);
                result.put("message", "SUCCESS");
                result.put("reviews", list);
                status = HttpStatus.OK;

            } catch (Exception e) {
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
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
                List<MyPageReviewDto[]> list = reviewService.getReviewsByUserid(userid);
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
    public ResponseEntity<?> update(@RequestBody UpdateReviewDto updateReviewDto, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                String writer = reviewService.getIdByReview_id(updateReviewDto.getReview_id());
                if (userid.equals(writer)) {
                    reviewService.update(updateReviewDto.getText(), updateReviewDto.getReview_id());
                    result.put("message", "SUCCESS");
                    status = HttpStatus.OK;
                } else {
                    result.put("message", "본인이 작성한 리뷰가 아닙니다.");
                    status = HttpStatus.BAD_REQUEST;
                }
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

    // Delete
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Map<String, Integer> review_id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        // 토큰 검증
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                String writerId = reviewService.getIdByReview_id(review_id.get("review_id"));
                if (userid.equals(writerId)) {
                    reviewService.delete(review_id.get("review_id"));
                    result.put("message", "SUCCESS");
                    status = HttpStatus.OK;
                } else {
                    result.put("message", "작성자가 아닙니다.");
                    status = HttpStatus.BAD_REQUEST;
                }
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

    // 리뷰 좋아요 추가
    @PostMapping("/addLike")
    public ResponseEntity<?> like(@RequestBody Map<String, Integer> reviewDto, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                reviewServiceImpl.addLike(reviewDto.get("review_id"), userid);
                result.put("message", "SUCCESS");
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

    // 리뷰 좋아요 삭제
    @DeleteMapping("/deleteLike")
    public ResponseEntity<?> deleteLike(@RequestBody Map<String, Integer> review_id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try{
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                reviewServiceImpl.deleteLike(review_id.get("review_id"), userid);
                result .put("message", "SUCCESS");
                status = HttpStatus.OK;
            }catch(Exception e){
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            result.put("message", "Token Error");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(result, status);
    }

    // 내가 좋아요 누른 리뷰
    @GetMapping("/liked")
    public ResponseEntity<?> liked(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try{
                String userid = jwtUtil.getUserId(request.getHeader("Authorization"));
                List<MyPageReviewDto[]> list = reviewServiceImpl.getReviewLikeByUserid(userid);
                System.out.println(list.size());
                result.put("message", "SUCCESS");
                result.put("liked reviews", list);
                status = HttpStatus.OK;

            }catch(Exception e){
                result.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            result.put("message", "Token Error");
            status = HttpStatus.UNAUTHORIZED;

        }
        return new ResponseEntity<>(result, status);
    }

    // Top 5 review
    @GetMapping("/best")
    public ResponseEntity<?> best() throws Exception {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try{
            List<ReviewDto> list = reviewServiceImpl.getTopReview();
            result.put("message", "SUCCESS");
            result.put("best reviews", list);
            status = HttpStatus.OK;
        }catch(Exception e){
            result.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(result, status);
    }
}
