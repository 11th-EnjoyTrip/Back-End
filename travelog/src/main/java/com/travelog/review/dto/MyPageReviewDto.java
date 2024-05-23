package com.travelog.review.dto;

import lombok.Data;

@Data
public class MyPageReviewDto {
    private int review_id;
    private int content_id;
    private String title;
    private String review_text;
    private int likes;
    private String update_time;
    private String userid;
    private String nickname;
    private int checkliked;
}
