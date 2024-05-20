package com.travelog.plan.service;

import com.travelog.attraction.dto.AttractionInfoDto;
import com.travelog.attraction.service.AttractionService;
import com.travelog.likes.dto.LikeType;
import com.travelog.likes.dto.LikesRequestDto;
import com.travelog.likes.service.LikesService;
import com.travelog.plan.dao.PlanDao;
import com.travelog.plan.dto.*;
import com.travelog.plan.exception.InvalidException;
import com.travelog.plan.exception.WrongTransportationException;
import com.travelog.util.RequestList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Service
public class PlanServiceImpl implements PlanService {

    private final PlanDao planDao;
    private final AttractionService attractionService;
    private final LikesService likesService;

    @Autowired
    public PlanServiceImpl(PlanDao planDao, AttractionService attractionService, LikesService likesService) {
        this.planDao = planDao;
        this.attractionService = attractionService;
        this.likesService = likesService;
    }

    // 여행 계획 DB 저장
    @Override
    @Transactional
    public void insertPlan(PlanRequestDto planRequestDto, int userId) throws SQLException {

        // TODO#1 : TripPlan 저장, TripPlan 저장한 tripPlanId 가져오기
        long tripPlanId = insertTripPlan(planRequestDto, userId);

        // TODO#2 : DetailPlan 저장
        insertDetailPlan(planRequestDto.getDayPlanList(),tripPlanId);
    }


    // 공유된 여행 계획 리스트만 조회
    @Override
    @Transactional
    public Page<Map<String, Object>> getSharedPlanList(Pageable pageable) throws SQLException {

        //빌더 패턴으로 data,pageable 파라미터에 데이터 주입
        RequestList<?> requestList = RequestList.builder()
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getSharedPlanList(requestList);

        return new PageImpl<>(content);
    }

    @Override
    @Transactional
    public Page<Map<String, Object>> getLikeTripPlans(int memberId,Pageable pageable) throws SQLException {

        RequestList<?> requestList = RequestList.builder()
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getLikePlanList(memberId,requestList);

        return new PageImpl<>(content);
    }

    @Override
    @Transactional
    public Page<Map<String, Object>> getMyTripPlans(int memberId, Pageable pageable) throws SQLException {
        RequestList<?> requestList = RequestList.builder()
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getMyPlanList(memberId,requestList);

        return new PageImpl<>(content);
    }

    @Override
    public PlanResponseDto getDetailPlan(Long tripPlanId) throws SQLException {
        return planDao.getDetailPlan(tripPlanId);
    }

    @Override
    @Transactional
    public void deleteTripPlan(int tripPlanId,int memberId) throws SQLException {

        // TODO : 해당 계획 좋아요 누른 것들 모두 삭제
        LikesRequestDto likesRequestDto = LikesRequestDto.builder()
                .likeType(LikeType.PLAN)
                .likeTypeId(tripPlanId)
                .build();
        likesService.deleteLikes(likesRequestDto);

        // TODO : 여행 계획 삭제
        planDao.deleteTripPlan(tripPlanId,memberId);

    }

    @Override
    @Transactional
    public void updatePlan(Long tripPlanId, PlanRequestDto planRequestDto) throws SQLException {
        //TODO#1 : planRequestDto의 TripPlan 부분 수정
        updateTripPlan(tripPlanId,planRequestDto);

        //TODO#2 : dayPlanList 반복문으로 돌면서 동일한 day 가 있는 경우
        // ->  detailPlanList 업데이트
        // - 동일한 day 가 없는 경우 -> 해당 day detailPlan 전체 삭제
        updateDetailPlan(tripPlanId,planRequestDto.getDayPlanList());
    }

    // 여행 계획 수정
    @Transactional
    public void updateTripPlan(Long tripPlanId,PlanRequestDto planRequestDto) throws SQLException{

        TripPlanDto tripPlanDto = TripPlanDto.builder()
                .title(planRequestDto.getTitle())
                .intro(planRequestDto.getIntro())
                .isShared(planRequestDto.isShared())
                .startDate(planRequestDto.getStartDate())
                .endDate(planRequestDto.getEndDate())
                .build();

        planDao.updateTripPlan(tripPlanId,tripPlanDto);
    }

    // 상세 계획 수정
    public void updateDetailPlan(Long tripPlanId,List<DayPlanDto> dayPlanDtoList) throws SQLException{
        //TODO#2 : dayPlanList 반복문으로 돌면서 동일한 day 가 있는 경우
        // ->  detailPlanList 업데이트
        // - 동일한 day 가 없는 경우 -> 해당 day detailPlan 전체 삭제

        for (DayPlanDto dayPlanDto : dayPlanDtoList) {
            int day = dayPlanDto.getDay();
            List<DetailPlanRequestDto> detailPlanDtoList = dayPlanDto.getDetailPlanList();

            // 동일한 day가 있는지 확인합니다.
//            if (isDayExists(tripPlanId, day)) {
//                // 동일한 day가 있으면 해당 세부 계획들을 업데이트합니다.
//                updateDetailPlans(tripPlanId, day, detailPlanDtoList);
//            } else {
//                // 동일한 day가 없으면 해당 날짜의 세부 계획들을 전체 삭제합니다.
//                deleteDetailPlans(tripPlanId, day);
//            }
        }
    }

    // TripPlan 저장
    @Transactional
    public Long insertTripPlan(PlanRequestDto planRequestDto, int userId) throws SQLException {

        TripPlanDto tripPlanDto = TripPlanDto.builder()
                .title(planRequestDto.getTitle())
                .intro(planRequestDto.getIntro())
                .isShared(planRequestDto.isShared())
                .startDate(planRequestDto.getStartDate())
                .endDate(planRequestDto.getEndDate())
                .memberId(userId)
                .build();

        planDao.insertTripPlan(tripPlanDto);

        // TripPlan 저장한 tripPlanId 가져오기
        return tripPlanDto.getTripPlanId();
    }

    // DetailPlan 저장
    @Transactional
    public void insertDetailPlan(List<DayPlanDto> dayPlanDtoList, Long tripPlanId) {

        dayPlanDtoList.forEach(dayPlan -> {
            System.out.println(dayPlan);
            dayPlan.getDetailPlanList().forEach(detailPlan -> {

                // contentId가 유효한지 확인
                checkContentId(detailPlan.getContentId());

                // 유효한 이동수단인지 확인
                checkTransportation(detailPlan.getTransportation().getTransportation());

                // 세부 여행 계획 저장
                DetailPlanDto detailPlanDto = DetailPlanDto.builder()
                        .tripPlanId(tripPlanId)
                        .day(dayPlan.getDay())
                        .sequence(detailPlan.getSequence())
                        .memo(detailPlan.getMemo())
                        .departureTime(detailPlan.getDepartureTime())
                        .arrivalTime(detailPlan.getArrivalTime())
                        .distance(detailPlan.getDistance())
                        .transportation(detailPlan.getTransportation())
                        .contentId(detailPlan.getContentId())
                        .build();
                System.out.println(detailPlanDto);
                try {
                    planDao.insertDetailPlan(detailPlanDto);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    // contentId가 유효한지 확인
    public void checkContentId(int contentId) throws InvalidException {
        AttractionInfoDto attractionInfo = attractionService.findAttractionById(contentId);
        if (attractionInfo == null) {
            throw new InvalidException("유효하지 않은 관광지 : "+contentId);
        }
    }

    // 이동수단이 유효한지 확인
    public void checkTransportation(int transportValue) throws WrongTransportationException {
        try {
            Transportation transportation = Transportation.getTransportation(transportValue);
            System.out.println("이동수단: " + transportation.getTransportation());
        } catch (IllegalArgumentException e) {
            throw new WrongTransportationException("유효하지 않은 이동수단 : " + transportValue);
        }
    }
}
