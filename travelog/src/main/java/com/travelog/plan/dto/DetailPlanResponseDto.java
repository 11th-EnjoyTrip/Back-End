package com.travelog.plan.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

//@Getter
//@NoArgsConstructor // 기본 생성자 추가
//@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
//@Builder
@Data
public class DetailPlanResponseDto {
    private int sequence;
    private String memo;
    private String title;
    private float distance;
    private int contentId;
    private double latitude;
    private double longitude;
    private Time departureTime;
    private Time arrivalTime;
    private int contentTypeId;
    private Transportation transportation;
    private String firstImage;
    private String firstImage2;
}
