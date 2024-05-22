package com.travelog.review.dto;

import lombok.Data;

@Data
public class ResponseReviewDto {
    private int review_id;
    private String nickname;
    private String review_text;
    private int content_id;
    private int likes;
}
