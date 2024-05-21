package com.travelog.plan.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.travelog.plan.handler.JsonTypeHandler;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Getter
//@NoArgsConstructor // 기본 생성자 추가
//@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
//@Builder
@Data
public class PlanResponseDto {
    private Long tripPlanId;
    private Date startDate;
    private Date endDate;
    private String title;
    private String intro;
    private int likes;
    private Timestamp updatedAt;
    private String userid;
    private String nickname;
    private String username;
    private boolean isLikedPlan;
    private String dayPlanList;
    // private List<DayPlanResponseDto> dayPlanList;

    @Override
    public String toString() {
        return "PlanResponseDto{" +
                "tripPlanId=" + tripPlanId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", likes=" + likes +
                ", updatedAt=" + updatedAt +
                ", userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", isLikedPlan=" + isLikedPlan +
                ", dayPlanList=" + dayPlanList +
                '}';
    }
}
