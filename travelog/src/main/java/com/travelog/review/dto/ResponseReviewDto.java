package com.travelog.review.dto;

import lombok.Data;

@Data
public class ResponseReviewDto {
    private int id;
    private int review_id;
    private String review_text;
    private int content_id;
}
