package com.travelog.plan.dao;

import com.travelog.plan.dto.DetailPlanDto;
import com.travelog.plan.dto.PlanRequestDto;
import com.travelog.plan.dto.PlanResponseDto;
import com.travelog.plan.dto.TripPlanDto;
import com.travelog.util.RequestList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface PlanDao {

    //insert Plan(여행 계획 저장)
    @Insert("insert into trip_plan (start_date, end_date, title, intro, is_shared, member_id)" +
            " values (#{startDate},#{endDate},#{title},#{intro},#{isShared},#{memberId});")
    @Options(useGeneratedKeys = true, keyProperty = "tripPlanId", keyColumn = "trip_plan_id")
    void insertTripPlan(TripPlanDto tripPlanDto) throws SQLException;

    //updateTripPlan(여행 계획 수정)
    @Update("UPDATE trip_plan " +
            "SET start_date = #{tripPlanDto.startDate}, " +
            "    end_date = #{tripPlanDto.endDate}, " +
            "    title = #{tripPlanDto.title}, " +
            "    intro = #{tripPlanDto.intro}, " +
            "    is_shared = #{tripPlanDto.isShared} " +
            "WHERE trip_plan_id = #{tripPlanId}")
    void updateTripPlan(Long tripPlanId,TripPlanDto tripPlanDto) throws SQLException;


    //insert Detail Plan(여행 세부 계획 저장)
    @Insert("insert into detail_plan (trip_plan_id,day, sequence, memo, departure_time, arrival_time, distance, transportation, content_id)" +
            " values (#{tripPlanId},#{day}, #{sequence}, #{memo}, #{departureTime}, #{arrivalTime}, #{distance}, #{transportation}, #{contentId} ) ")
    void insertDetailPlan(DetailPlanDto detailPlanDto) throws SQLException;

    @Select("SELECT " +
            "tp.trip_plan_id AS tripPlanId, " +
            "tp.start_date AS startDate, " +
            "tp.end_date AS endDate, " +
            "tp.title, " +
            "tp.intro, " +
            "tp.likes, " +
            "DATE_FORMAT(CONVERT_TZ(tp.updated_at, '+00:00', '+09:00'), '%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "tp.member_id AS memberId, " +
            "m.nickname, " +
            "m.username, " +
            "(SELECT COUNT(*) > 0 FROM likes WHERE member_id = m.id AND like_type = 2 AND like_type_id = dp.trip_plan_id) AS isLikedPlan, " +
            "JSON_ARRAYAGG( " +
            "JSON_OBJECT( " +
            "'contentId', dp.content_id, " +
            "'title', ai.title, " +
            "'firstImage', ai.first_image " +
            ")) AS contents " +
            "FROM " +
            "trip_plan tp " +
            "JOIN " +
            "member m ON tp.member_id = m.id " +
            "JOIN " +
            "detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "attraction_info ai ON dp.content_id = ai.content_id " +
            "GROUP BY " +
            "tp.trip_plan_id " +
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String,Object>> getSharedPlanList(RequestList<?> requestList) throws SQLException;

    @Select("SELECT " +
            "tp.trip_plan_id AS tripPlanId, " +
            "tp.start_date AS startDate, " +
            "tp.end_date AS endDate, " +
            "tp.title, " +
            "tp.intro, " +
            "tp.likes, " +
            "DATE_FORMAT(CONVERT_TZ(tp.updated_at, '+00:00', '+09:00'), '%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "tp.member_id AS memberId, " +
            "m.nickname, " +
            "m.username, " +
            "JSON_ARRAYAGG( " +
            "JSON_OBJECT( " +
            "'contentId', dp.content_id, " +
            "'title', ai.title, " +
            "'firstImage', ai.first_image " +
            ")) AS contents " +
            "FROM " +
            "trip_plan tp " +
            "JOIN " +
            "member m ON tp.member_id = m.id " +
            "JOIN " +
            "detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "attraction_info ai ON dp.content_id = ai.content_id " +
            "JOIN " +
            "likes l ON l.like_type = 2 AND l.like_type_id = tp.trip_plan_id AND l.member_id = m.id " +
            "WHERE " +
            "l.member_id = #{memberId} AND l.like_type = 1 " +
            "GROUP BY " +
            "tp.trip_plan_id " +
            "LIMIT #{requestList.pageable.pageSize} OFFSET #{requestList.pageable.offset}")
    List<Map<String, Object>> getLikePlanList(@Param("memberId") int memberId,RequestList<?> requestList) throws SQLException;

    @Select("SELECT " +
            "    tp.trip_plan_id AS tripPlanId, " +
            "    tp.start_date AS startDate, " +
            "    tp.end_date AS endDate, " +
            "    tp.title, " +
            "    tp.intro, " +
            "    tp.likes, " +
            "    DATE_FORMAT(CONVERT_TZ(tp.updated_at, '+00:00', '+09:00'), '%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "    tp.member_id AS memberId, " +
            "    m.nickname, " +
            "    m.username, " +
            "    JSON_ARRAYAGG( " +
            "        JSON_OBJECT( " +
            "            'contentId', dp.content_id, " +
            "            'title', ai.title, " +
            "            'firstImage', ai.first_image " +
            "        ) " +
            "    ) AS contents " +
            "FROM " +
            "    trip_plan tp " +
            "JOIN " +
            "    member m ON tp.member_id = m.id " +
            "JOIN " +
            "    detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "    attraction_info ai ON dp.content_id = ai.content_id " +
            "WHERE " +
            "    tp.member_id = #{memberId} " +
            "GROUP BY " +
            "    tp.trip_plan_id " +
            "LIMIT #{requestList.pageable.pageSize} OFFSET #{requestList.pageable.offset}")
    List<Map<String, Object>> getMyPlanList(@Param("memberId") int memberId,RequestList<?> requestList) throws SQLException;

    // 여행 계획 삭제
    @Delete("DELETE FROM trip_plan WHERE trip_plan_id = #{tripPlanId} AND member_id=#{memberId}")
    void deleteTripPlan(int tripPlanId,int memberId);

    @Select("SELECT " +
            "    tp.trip_plan_id AS tripPlanId, " +
            "    tp.start_date AS startDate, " +
            "    tp.end_date AS endDate, " +
            "    tp.title, " +
            "    tp.intro, " +
            "    tp.likes, " +
            "    DATE_FORMAT(CONVERT_TZ(tp.updated_at, '+00:00', '+09:00'), '%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "    tp.member_id AS memberId, " +
            "    m.nickname, " +
            "    m.username, " +
            "    (SELECT COUNT(*) > 0 " +
            "     FROM likes " +
            "     WHERE member_id = m.id AND like_type = 1 AND like_type_id = tp.trip_plan_id) AS isLikedPlan, " +
            "    ( " +
            "        SELECT JSON_ARRAYAGG( " +
            "                    JSON_OBJECT( " +
            "                        'day', inner_dp.day, " +
            "                        'detailPlanList', inner_dp.detailPlanList " +
            "                    ) " +
            "               ) " +
            "        FROM ( " +
            "            SELECT  " +
            "                dp.day, " +
            "                JSON_ARRAYAGG( " +
            "                    JSON_OBJECT( " +
            "                        'sequence', dp.sequence, " +
            "                        'memo', dp.memo, " +
            "                        'transportation', dp.transportation, " +
            "                        'distance', dp.distance, " +
            "                        'departureTime', dp.departure_time, " +
            "                        'arrivalTime', dp.arrival_time, " +
            "                        'contentId', ai.content_id, " +
            "                        'title', ai.title, " +
            "                        'firstImage', ai.first_image, " +
            "                        'firstImage2', ai.first_image2, " +
            "                        'contentTypeId', ai.content_type_id, " +
            "                        'latitude', ai.latitude, " +
            "                        'longitude', ai.longitude " +
            "                    ) " +
            "                ) AS detailPlanList " +
            "            FROM " +
            "                detail_plan dp " +
            "            JOIN " +
            "                attraction_info ai ON dp.content_id = ai.content_id " +
            "            WHERE  " +
            "                dp.trip_plan_id = tp.trip_plan_id " +
            "            GROUP BY  " +
            "                dp.day " +
            "            ORDER BY  " +
            "                dp.day " +
            "        ) AS inner_dp " +
            "    ) AS dayPlanList " +
            "FROM  " +
            "    trip_plan tp  " +
            "JOIN  " +
            "    member m ON tp.member_id = m.id  " +
            "WHERE  " +
            "    tp.trip_plan_id = #{tripPlanId} " +
            "GROUP BY  " +
            "    tp.trip_plan_id")
    PlanResponseDto getDetailPlan(Long tripPlanId) throws SQLException;
}
