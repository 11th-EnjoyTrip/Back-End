package com.travelog.plan.service;

import com.travelog.plan.dto.PlanRequestDto;
import com.travelog.plan.dto.PlanResponseDto;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public interface PlanService {
    void insertPlan(PlanRequestDto planRequestDto,int userId) throws SQLException;
    Page<Map<String, Object>> getSharedPlanList(Pageable pageable) throws SQLException;
    Page<Map<String, Object>> getLikeTripPlans(int memberId,Pageable pageable) throws SQLException;
    Page<Map<String, Object>> getMyTripPlans(int memberId,Pageable pageable) throws SQLException;

    PlanResponseDto getDetailPlan(Long tripPlanId) throws SQLException;
    void deleteTripPlan(int tripPlanId,int memberId) throws SQLException;
    void updatePlan(Long tripPlanId,PlanRequestDto planRequestDto) throws SQLException;
}
