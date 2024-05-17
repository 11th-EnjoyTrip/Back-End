package com.travelog.review.service;

import com.travelog.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    void write(ReviewDto reviewDto) throws Exception;
    List<ReviewDto> getReviewsByUserid(String user_id) throws Exception;
    void update(String text, String content_id);
    String getIdByContent_id(String content_id);
    List<ReviewDto> getReviewsByContentId(String content_id) throws Exception;
    String getIdByReview_id(int review_id);
    void delete(int review_id);

}
