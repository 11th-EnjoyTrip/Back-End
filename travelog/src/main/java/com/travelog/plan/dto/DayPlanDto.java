package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DayPlanDto {
    private int day;
    private List<DetailPlanRequestDto> detailPlanList ;
}
