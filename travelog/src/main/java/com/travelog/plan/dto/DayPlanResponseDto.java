package com.travelog.plan.dto;

import lombok.*;

import java.util.List;

//@Getter
//@NoArgsConstructor // 기본 생성자 추가
//@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
//@Builder
@Data
public class DayPlanResponseDto {
    private int day;
    private List<DetailPlanResponseDto> detailPlanList;
}
