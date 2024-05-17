package com.travelog.review.service;

import com.travelog.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    void write(ReviewDto reviewDto) throws Exception;
    List<ReviewDto> written(String user_id) throws Exception;
    void update(String text, String content_id);
    String getIdByContent_id(String content_id);

}
