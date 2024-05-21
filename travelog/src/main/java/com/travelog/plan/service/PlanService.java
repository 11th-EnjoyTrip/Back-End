package com.travelog.plan.service;

import com.travelog.plan.dto.PlanLikeRequest;
import com.travelog.plan.dto.PlanRequestDto;
import com.travelog.plan.dto.PlanResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Map;

@Service
public interface PlanService {
    void insertPlan(PlanRequestDto planRequestDto,String userId) throws SQLException;
    Page<Map<String, Object>> getSharedPlanList(Pageable pageable,String userid) throws SQLException;
    Page<Map<String, Object>> getLikeTripPlans(String userId,Pageable pageable) throws SQLException;
    Page<Map<String, Object>> getMyTripPlans(String userId,Pageable pageable) throws SQLException;

    PlanResponseDto getDetailPlan(Long tripPlanId,String userId) throws SQLException;
    void deleteTripPlan(int tripPlanId,String userId) throws SQLException;
    void updatePlan(Long tripPlanId,PlanRequestDto planRequestDto) throws SQLException;
    void insertPlanLike(Long tripPlanId,String userId) throws SQLException;
    void deletePlanLike(Long tripPlanId,String userId) throws SQLException;
}
