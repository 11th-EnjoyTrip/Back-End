package com.travelog.review.service;

import com.travelog.review.dto.MyPageReviewDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;

import java.sql.SQLException;
import java.util.List;

public interface ReviewService {
    void write(ReviewDto reviewDto) throws Exception;
    void delete(int review_id);
    void addLike(int like_id, String userid);
    void deleteLike(int review_id, String userid);
    void updateLike(int review_id);
    void update(String text, int review_id);
    List<ResponseReviewDto[]> getResponseReviewsByContentId(String content_id) throws Exception;
    List<MyPageReviewDto[]> getReviewsByUserid(String user_id) throws Exception;
    List<MyPageReviewDto[]> getReviewsByContentId(String content_id) throws Exception;
    List<MyPageReviewDto[]> getReviewLikeByUserid(String userid) throws Exception;
    List<ReviewDto> getTopReview() throws SQLException;

    String getIdByReview_id(int review_id);

}
