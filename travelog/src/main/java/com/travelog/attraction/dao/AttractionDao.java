package com.travelog.attraction.dao;

import com.travelog.attraction.dto.AttractionDetailDto;
import com.travelog.attraction.dto.AttractionInfoDto;
import com.travelog.util.RequestList;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttractionDao {

    @Select("SELECT " +
            "    content_id AS contentId, content_type_id AS contentTypeId, " +
            "    CASE " +
            "        WHEN content_type_id = 12 THEN '관광지' " +
            "        WHEN content_type_id = 14 THEN '문화시설' " +
            "        WHEN content_type_id = 15 THEN '축제/공연/행사' " +
            "        WHEN content_type_id = 28 THEN '레포츠' " +
            "        WHEN content_type_id = 32 THEN '숙박' " +
            "        WHEN content_type_id = 38 THEN '쇼핑' " +
            "        WHEN content_type_id = 39 THEN '음식' " +
            "        ELSE '기타' " +
            "    END AS contentTypeName, title, first_image AS firstImage, first_image2 AS firstImage2, " +
            "    ai.sido_code AS sidoCode, s.sido_name AS sidoName,latitude, longitude,likes, " +
            "(SELECT COUNT(*) > 0 FROM attraction_like al WHERE al.userid = #{userId} AND ai.content_id = al.content_id) AS isLikedAttraction "+
            "FROM attraction_info ai " +
            "JOIN sido s ON ai.sido_code = s.sido_code " +
            "WHERE " +
            "    ai.sido_code = IF(#{requestList.data.region} = 0, ai.sido_code, #{requestList.data.region}) " +
            "    AND FIND_IN_SET(content_type_id, #{requestList.data.category}) " +
            "    AND title LIKE CONCAT('%', #{requestList.data.keyword}, '%') " +
            "LIMIT #{requestList.pageable.pageSize} OFFSET #{requestList.pageable.offset}")
    List<Map<String,Object>> getAttractionList(RequestList<?> requestList,String userId) throws SQLException;

    @Select("SELECT ai.content_id, content_type_id," +
            "CASE " +
            "    WHEN content_type_id = 12 THEN '관광지' " +
            "    WHEN content_type_id = 14 THEN '문화시설' " +
            "    WHEN content_type_id = 15 THEN '축제/공연/행사' " +
            "    WHEN content_type_id = 28 THEN '레포츠' " +
            "    WHEN content_type_id = 32 THEN '숙박' " +
            "    WHEN content_type_id = 38 THEN '쇼핑' " +
            "    WHEN content_type_id = 39 THEN '음식' " +
            "    ELSE '기타' " +
            "END AS content_type_name, " +
            "title, addr1, addr2, zipcode, tel, first_image,first_image2,likes, " +
            "(SELECT COUNT(*) > 0 FROM attraction_like al WHERE al.userid = #{userId} AND ai.content_id = al.content_id) AS isLikedAttraction, "+
            "ai.sido_code, s.sido_name, latitude, longitude, ad.overview " +
            "FROM attraction_info ai " +
            "JOIN sido s ON ai.sido_code = s.sido_code " +
            "JOIN attraction_description ad ON ai.content_id = ad.content_id " +
            "WHERE ai.content_id = #{contentId}")
    @Result(property = "contentId", column = "content_id")
    @Result(property = "contentTypeId",column = "content_type_id")
    @Result(property = "contentTypeName",column = "content_type_name")
    @Result(property = "firstImage",column = "first_image")
    @Result(property = "firstImage2",column = "first_image2")
    @Result(property = "sidoCode",column = "sido_code")
    @Result(property = "sidoName",column = "sido_name")
    AttractionDetailDto getAttractionDetail(int contentId,String userId) throws SQLException;

    @Select("select content_id as contentId, content_type_id as contentTypeId, title, addr1, addr2, zipcode," +
            "tel, first_image as firstImage, readcount, sido_code as sidoCode, gugun_code as gugunCode, latitude, longitude" +
            " from attraction_info where content_id = #{contentId};")
    AttractionInfoDto findAttractionById(int contentId);

    // 관광지 좋아요 등록
    @Insert("insert into attraction_like(content_id, userid) values(#{contentId},#{userId})")
    void insertAttractionLike(Long contentId,String userId) throws SQLException;

    // 관광지 좋아요 삭제
    @Delete("DELETE FROM attraction_like WHERE content_id = #{contentId} and userid = #{userId}")
    void deleteAttractionLike(Long contentId,String userId) throws SQLException;

    @Update("UPDATE attraction_info " +
            "SET likes = likes + 1 " +
            "WHERE content_id = #{contentId};")
    void incrementAttrLikes(Long contentId) throws SQLException;

    @Update("UPDATE attraction_info " +
            "SET likes = CASE WHEN likes > 0 THEN likes - 1 ELSE 0 END " +
            "WHERE content_id = #{contentId}")
    void decrementAttrLikes(Long contentId) throws SQLException;
}
