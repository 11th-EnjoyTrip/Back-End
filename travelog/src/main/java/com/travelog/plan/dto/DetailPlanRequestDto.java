package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Builder
public class DetailPlanRequestDto {
    private int sequence;
    private String memo;
    private Time departureTime;
    private Time arrivalTime;
    private float distance;
    private Transportation transportation;
    private int contentId;
}
