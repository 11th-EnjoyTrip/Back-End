package com.travelog.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewLikeDto {
    private int like_id;
    private int review_id;
    private String userid;

    public ReviewLikeDto(int review_id, String userid){
        this.review_id = review_id;
        this.userid = userid;
    }

}