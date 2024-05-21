package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class SharedPlanRequestDto {
    private String userId;
    private String keyword;
}
