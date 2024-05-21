package com.travelog.plan.service;

import com.travelog.attraction.dto.AttractionInfoDto;
import com.travelog.attraction.service.AttractionService;
import com.travelog.plan.dao.PlanDao;
import com.travelog.plan.dto.*;
import com.travelog.plan.exception.InvalidException;
import com.travelog.plan.exception.WrongTransportationException;
import com.travelog.util.RequestList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Service
public class PlanServiceImpl implements PlanService {

    private final PlanDao planDao;
    private final AttractionService attractionService;

    @Autowired
    public PlanServiceImpl(PlanDao planDao, AttractionService attractionService) {
        this.planDao = planDao;
        this.attractionService = attractionService;
    }

    // #1 여행 계획 저장
    @Override
    @Transactional
    public void insertPlan(PlanRequestDto planRequestDto, String userId) throws SQLException {

        // TODO#1 : TripPlan 저장, TripPlan 저장한 tripPlanId 가져오기
        long tripPlanId = insertTripPlan(planRequestDto, userId);

        // TODO#2 : DetailPlan 저장
        insertDetailPlan(planRequestDto.getDayPlanList(),tripPlanId);
    }

    // #2 공유된 여행 계획 리스트만 조회
    @Override
    @Transactional
    public Page<Map<String, Object>> getSharedPlanList(Pageable pageable,String userid,String keyword) throws SQLException {

        //빌더 패턴으로 data,pageable 파라미터에 데이터 주입
        RequestList<?> requestList = RequestList.builder()
                .data(SharedPlanRequestDto.builder()
                        .userId(userid).keyword(keyword).build())
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getSharedPlanList(requestList);

        return new PageImpl<>(content);
    }


    // #3 여행 계획 상세 조회
    @Override
    public PlanResponseDto getDetailPlan(Long tripPlanId,String userId) throws SQLException {
        return planDao.getDetailPlan(tripPlanId,userId);
    }


    @Override
    @Transactional
    public Page<Map<String, Object>> getLikeTripPlans(String userId,Pageable pageable) throws SQLException {

        RequestList<?> requestList = RequestList.builder()
                .data(userId)
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getLikePlanList(requestList);

        return new PageImpl<>(content);
    }

    @Override
    @Transactional
    public Page<Map<String, Object>> getMyTripPlans(String userId, Pageable pageable) throws SQLException {
        RequestList<?> requestList = RequestList.builder()
                .data(userId)
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = planDao.getMyPlanList(requestList);

        return new PageImpl<>(content);
    }


    @Override
    @Transactional
    public void deleteTripPlan(int tripPlanId,String userId) throws SQLException {

        // TODO : 해당 tripPlan이 userId가 작성한 것이 맞는지 확인
        verifyUserTripPlan(tripPlanId,userId);

        // TODO : 여행 계획 삭제
        planDao.deleteTripPlan(tripPlanId,userId);

    }

    @Override
    @Transactional
    public void updatePlan(Long tripPlanId, PlanRequestDto planRequestDto) throws SQLException {
        //TODO#1 : planRequestDto의 TripPlan 부분 수정
        updateTripPlan(tripPlanId,planRequestDto);

        //TODO#2 : 해당 day detailPlan 전체 삭제
        deleteDetailPlan(tripPlanId);

        //TODO#3 : 해당 day들 detailPlan에 삽입
        insertDetailPlan(planRequestDto.getDayPlanList(),tripPlanId);
    }

    @Override
    @Transactional
    public void insertPlanLike(Long tripPlanId,String userId) throws SQLException {
        // TODO#1 : 여행 계획 좋아요 삽입
        planDao.insertPlanLike(tripPlanId,userId);

        // TODO#2 : 해당 여행 계획에 likes+1
        planDao.incrementLikes(tripPlanId);
    }

    @Override
    public void deletePlanLike(Long tripPlanId, String userId) throws SQLException {
        // TODO#1 : 여행 계획 좋아요 삭제
        planDao.deletePlanLike(tripPlanId,userId);

        // TODO#2 : 해당 여행 계획에 likes-1
        planDao.decrementLikes(tripPlanId);
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

    // 상세 계획 전체 삭제
    @Transactional
    public void deleteDetailPlan(Long tripPlanId) throws SQLException{
        planDao.deleteDetailPlan(tripPlanId);
    }

    // TripPlan 저장
    @Transactional
    public Long insertTripPlan(PlanRequestDto planRequestDto, String userId) throws SQLException {

        TripPlanDto tripPlanDto = TripPlanDto.builder()
                .title(planRequestDto.getTitle())
                .intro(planRequestDto.getIntro())
                .isShared(planRequestDto.isShared())
                .startDate(planRequestDto.getStartDate())
                .endDate(planRequestDto.getEndDate())
                .userid(userId)
                .build();

        planDao.insertTripPlan(tripPlanDto);

        // TripPlan 저장한 tripPlanId 가져오기
        return tripPlanDto.getTripPlanId();
    }

    // DetailPlan 저장
    @Transactional
    public void insertDetailPlan(List<DayPlanDto> dayPlanDtoList, Long tripPlanId) {

        dayPlanDtoList.forEach(dayPlan -> {
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

    // 해당 tripPlan이 userId가 작성한 것이 맞는지 확인
    @Transactional
    public void verifyUserTripPlan(int tripPlanId,String userId) throws InvalidException {
        Boolean isUserTripPlan = planDao.isUserTripPlan(tripPlanId,userId);
        if(!isUserTripPlan) throw new InvalidException("해당 유저가 작성한 것이 아닙니다. : "+tripPlanId);
    }
}
