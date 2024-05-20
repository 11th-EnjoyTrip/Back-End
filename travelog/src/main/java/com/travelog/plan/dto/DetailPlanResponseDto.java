package com.travelog.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Builder
public class DetailPlanResponseDto {
    private String memo;
    private String title;
    private float distance;
    private double latitude;
    private int sequence;
    private int contentId;
    private double longitude;
    private String firstImage;
    private Timestamp arrivalTime;
    private String firstImage2;
    private int contentTypeId;
    private Timestamp departureTime;
    private int transportation;

}
