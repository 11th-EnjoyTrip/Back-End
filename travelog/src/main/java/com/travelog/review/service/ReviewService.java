package com.travelog.review.service;

import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.ReviewLikeDto;

import java.util.List;

public interface ReviewService {
    void write(ReviewDto reviewDto) throws Exception;
    void delete(int review_id);
    void reviewLike(int like_id, String userid);
    void update(String text, int review_id);

    List<ReviewDto> getReviewsByUserid(String user_id) throws Exception;
    List<ReviewDto> getReviewsByContentId(String content_id) throws Exception;

    String getIdByReview_id(int review_id);
    String getIdByContent_id(String content_id);

}
