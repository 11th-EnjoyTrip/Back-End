package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Builder
public class DetailPlanDto {
    private int detailPlanId;
    private int day;
    private int sequence;
    private String memo;
    private Time departureTime;
    private Time arrivalTime;
    private float distance;
    // enum
    private Transportation transportation;
    private int contentId;
    private Long tripPlanId;

    @Override
    public String toString() {
        return "DetailPlanDto{" +
                "detailPlanId=" + detailPlanId +
                ", day=" + day +
                ", sequence=" + sequence +
                ", memo='" + memo + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", distance=" + distance +
                ", transportation=" + transportation +
                ", contentId=" + contentId +
                ", tripPlanId=" + tripPlanId +
                '}';
    }
}
