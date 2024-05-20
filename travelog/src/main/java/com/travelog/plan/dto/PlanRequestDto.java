package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
public class PlanRequestDto {
    private String title;
    private String intro;
    private boolean isShared;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<DayPlanDto> dayPlanList;
}
