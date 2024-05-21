package com.travelog.plan.dao;

import com.travelog.plan.dto.DetailPlanDto;
import com.travelog.plan.dto.PlanLikeRequest;
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
    @Insert("insert into trip_plan (start_date, end_date, title, intro, is_shared, userid)" +
            " values (#{startDate},#{endDate},#{title},#{intro},#{isShared},#{userid});")
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

    // selectDetailPlan(여행 상세 계획 조회)
    @Select("SELECT " +
            "    tp.trip_plan_id AS tripPlanId, " +
            "DATE_FORMAT(tp.start_date, '%Y-%m-%d') AS startDate, " +
            "DATE_FORMAT(tp.end_date,'%Y-%m-%d') AS endDate, " +
            "    tp.title, " +
            "    tp.intro, " +
            "    tp.likes, " +
            "    DATE_FORMAT(tp.updated_at, '%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "    tp.userid, " +
            "    m.nickname, " +
            "    m.username, " +
            "    (SELECT COUNT(*) > 0 FROM plan_like pl WHERE pl.userid = #{userId} AND tp.trip_plan_id = pl.trip_plan_id) AS isLikedPlan, " +
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
            "                        'title', ai.title, " +
            "                        'distance', dp.distance, " +
            "                        'contentId', ai.content_id, " +
            "                        'latitude', ai.latitude, " +
            "                        'longitude', ai.longitude, " +
            "                        'departureTime', DATE_FORMAT(departure_time,'%H:%i:%s'), " +
            "                        'arrivalTime', DATE_FORMAT(dp.arrival_time,'%H:%i:%s'), " +
            "                        'contentTypeId', ai.content_type_id, " +
            "                        'transportation', " +
            "                                    CASE " +
            "                                        WHEN dp.transportation = 1 THEN 'CAR' " +
            "                                        WHEN dp.transportation = 2 THEN 'WALK' " +
            "                                        ELSE 'PT' " +
            "                                    END, " +
            "                        'firstImage', ai.first_image, " +
            "                        'firstImage2', ai.first_image2 " +
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
            "    member m ON tp.userid = m.userid  " +
            "WHERE  " +
            "    tp.trip_plan_id = #{tripPlanId} " +
            "GROUP BY  " +
            "    tp.trip_plan_id")
    PlanResponseDto getDetailPlan(@Param("tripPlanId") Long tripPlanId, @Param("userId") String userId) throws SQLException;

    //insert Detail Plan(여행 세부 계획 저장)
    @Insert("insert into detail_plan (trip_plan_id,day, sequence, memo, departure_time, arrival_time, distance, transportation, content_id)" +
            " values (#{tripPlanId},#{day}, #{sequence}, #{memo}, #{departureTime}, #{arrivalTime}, #{distance}, #{transportation}, #{contentId} ) ")
    void insertDetailPlan(DetailPlanDto detailPlanDto) throws SQLException;

    @Select("SELECT " +
            "tp.trip_plan_id AS tripPlanId, " +
            "DATE_FORMAT(tp.start_date, '%Y-%m-%d') AS startDate, " +
            "DATE_FORMAT(tp.end_date, '%Y-%m-%d') AS endDate, " +
            "tp.title, " +
            "tp.intro, " +
            "tp.likes, " +
            "DATE_FORMAT(tp.updated_at,'%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "tp.userid, " +
            "m.nickname, " +
            "m.username, " +
            "EXISTS(SELECT 1 FROM plan_like pl WHERE pl.userid = #{data.userId} AND tp.trip_plan_id = pl.trip_plan_id) AS isLikedPlan, " +
            "JSON_ARRAYAGG( " +
            "JSON_OBJECT( " +
            "'contentId', dp.content_id, " +
            "'title', ai.title, " +
            "'firstImage', ai.first_image, " +
            "'first_image2',ai.first_image2 "+
            ")) AS contents " +
            "FROM " +
            "trip_plan tp " +
            "JOIN " +
            "member m ON tp.userid = m.userid " +
            "JOIN " +
            "detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "attraction_info ai ON dp.content_id = ai.content_id " +
            "WHERE tp.is_shared = true " +
            "AND tp.title LIKE CONCAT('%', #{data.keyword}, '%') " +
            "GROUP BY " +
            "tp.trip_plan_id " +
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String,Object>> getSharedPlanList(RequestList<?> requestList) throws SQLException;

    @Select("SELECT " +
            "tp.trip_plan_id AS tripPlanId, " +
            "DATE_FORMAT(tp.start_date, '%Y-%m-%d') AS startDate, " +
            "DATE_FORMAT(tp.end_date, '%Y-%m-%d') AS endDate, " +
            "tp.title, " +
            "tp.intro, " +
            "tp.likes, " +
            "DATE_FORMAT(tp.updated_at,'%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "tp.userid, " +
            "m.nickname, " +
            "m.username, " +
            "EXISTS(SELECT 1 FROM plan_like pl WHERE pl.userid = #{data} AND tp.trip_plan_id = pl.trip_plan_id) AS isLikedPlan, " +
            "JSON_ARRAYAGG( " +
            "JSON_OBJECT( " +
            "'contentId', dp.content_id, " +
            "'title', ai.title, " +
            "'firstImage', ai.first_image, " +
            "'first_image2', ai.first_image2 " +
            ")) AS contents " +
            "FROM " +
            "trip_plan tp " +
            "JOIN " +
            "member m ON tp.userid = m.userid " +
            "JOIN " +
            "detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "attraction_info ai ON dp.content_id = ai.content_id " +
            "JOIN " +
            "plan_like pl ON tp.trip_plan_id = pl.trip_plan_id " +
            "WHERE tp.is_shared = true AND pl.userid = #{data} " +
            "GROUP BY tp.trip_plan_id " +
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String, Object>> getLikePlanList(RequestList<?> requestList) throws SQLException;

    @Select("SELECT " +
            "    tp.trip_plan_id AS tripPlanId, " +
            "DATE_FORMAT(tp.start_date, '%Y-%m-%d') AS startDate, " +
            "DATE_FORMAT(tp.end_date, '%Y-%m-%d') AS endDate, " +
            "    tp.title, " +
            "    tp.intro, " +
            "    tp.likes, " +
            "    DATE_FORMAT(tp.updated_at,'%Y-%m-%d %H:%i:%s') AS updatedAt, " +
            "    tp.userid, " +
            "    m.nickname, " +
            "    m.username, " +
            "EXISTS(SELECT 1 FROM plan_like pl WHERE pl.userid = #{data} AND tp.trip_plan_id = pl.trip_plan_id) AS isLikedPlan, " +
            "    JSON_ARRAYAGG( " +
            "        JSON_OBJECT( " +
            "            'contentId', dp.content_id, " +
            "            'title', ai.title, " +
            "            'firstImage', ai.first_image, " +
            "            'first_image2', ai.first_image2 " +
            "        ) " +
            "    ) AS contents " +
            "FROM " +
            "    trip_plan tp " +
            "JOIN " +
            "    member m ON tp.userid = m.userid " +
            "JOIN " +
            "    detail_plan dp ON tp.trip_plan_id = dp.trip_plan_id " +
            "JOIN " +
            "    attraction_info ai ON dp.content_id = ai.content_id " +
            "WHERE " +
            "    tp.userid = #{data} " +
            "GROUP BY " +
            "    tp.trip_plan_id " +
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String, Object>> getMyPlanList(RequestList<?> requestList) throws SQLException;

    // 여행 계획 삭제
    @Delete("DELETE FROM trip_plan WHERE trip_plan_id = #{tripPlanId} AND userid=#{userId}")
    void deleteTripPlan(int tripPlanId,String userId) throws SQLException;

    // 해당 tripPlan이 userId가 작성한 것이 맞는지 확인
    @Select("SELECT COUNT(*) > 0 FROM trip_plan WHERE trip_plan_id = #{tripPlanId} AND userid = #{userId}")
    boolean isUserTripPlan(int tripPlanId, String userId);

    // 상세 계획 전체 삭제
    @Delete("DELETE FROM detail_plan WHERE trip_plan_id = #{tripPlanId}")
    void deleteDetailPlan(Long tripPlanId) throws SQLException;

    // 여행 계획 좋아요 등록
    @Insert("insert into plan_like(trip_plan_id, userid) values(#{tripPlanId},#{userId})")
    void insertPlanLike(Long tripPlanId,String userId) throws SQLException;

    // 여행 계획 좋아요 삭제
    @Delete("DELETE FROM plan_like WHERE trip_plan_id = #{tripPlanId} and userid = #{userId}")
    void deletePlanLike(Long tripPlanId,String userId) throws SQLException;

    @Update("UPDATE trip_plan " +
            "SET likes = likes + 1 " +
            "WHERE trip_plan_id = #{tripPlanId};")
    void incrementLikes(Long tripPlanId) throws SQLException;

    @Update("UPDATE trip_plan " +
            "SET likes = CASE WHEN likes > 0 THEN likes - 1 ELSE 0 END " +
            "WHERE trip_plan_id = #{tripPlanId}")
    void decrementLikes(Long tripPlanId) throws SQLException;
}
