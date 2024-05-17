package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class DetailPlanDto {
    private int detailPlanId;
    private int day;
    private int seqence;
    private String memo;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private float distance;
    // enum 변경 고민중
    private String transportation;

    private int contentId;
    private int tripPlanId;
}
