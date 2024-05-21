package com.travelog.review.dto;

import lombok.Data;

@Data
public class ReviewLikeDto {
    private int like_id;
    private int review_id;
    private String userid;
}