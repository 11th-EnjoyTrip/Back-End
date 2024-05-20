package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class PlanResponseDto {
    private Long tripPlanId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String title;
    private String intro;
    private int likes;
    private Timestamp updatedAt;
    private int memberId;
    private String nickname;
    private String username;
    private boolean isLikedPlan;
    private List<DayPlanResponseDto> dayPlanList;
}
