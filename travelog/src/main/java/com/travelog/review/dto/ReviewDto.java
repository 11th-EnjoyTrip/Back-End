package com.travelog.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDto {
    private int review_id;
    private String userid;
    private int content_id;
    private String nickname;
    private int likes;
    private String time;
    private String review_text;
}
