package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class TripPlanDto {
    private Long tripPlanId;
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
    private String intro;
    private boolean isShared;
    private int likes;

    private Long memberId;
}
