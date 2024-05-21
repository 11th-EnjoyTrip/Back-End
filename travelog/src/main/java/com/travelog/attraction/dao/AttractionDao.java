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
            "    ai.sido_code AS sidoCode, s.sido_name AS sidoName,latitude, longitude " +
            "FROM attraction_info ai " +
            "JOIN sido s ON ai.sido_code = s.sido_code " +
            "WHERE " +
            "    ai.sido_code = IF(#{data.region} = 0, ai.sido_code, #{data.region}) " +
            "    AND FIND_IN_SET(content_type_id, #{data.category}) " +
            "    AND title LIKE CONCAT('%', #{data.keyword}, '%') " +
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String,Object>> getAttractionList(RequestList<?> requestList) throws SQLException;

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
            "title, addr1, addr2, zipcode, tel, first_image,first_image2, " +
            "ai.sido_code, s.sido_name, latitude, longitude, ad.overview " +
            "FROM attraction_info ai " +
            "JOIN sido s ON ai.sido_code = s.sido_code " +
            "JOIN attraction_description ad ON ai.content_id = ad.content_id " +
            "WHERE ai.content_id = #{id}")
    @Result(property = "contentId", column = "content_id")
    @Result(property = "contentTypeId",column = "content_type_id")
    @Result(property = "contentTypeName",column = "content_type_name")
    @Result(property = "firstImage",column = "first_image")
    @Result(property = "firstImage2",column = "first_image2")
    @Result(property = "sidoCode",column = "sido_code")
    @Result(property = "sidoName",column = "sido_name")
    AttractionDetailDto getAttractionDetail(int id) throws SQLException;

    @Select("select content_id as contentId, content_type_id as contentTypeId, title, addr1, addr2, zipcode," +
            "tel, first_image as firstImage, readcount, sido_code as sidoCode, gugun_code as gugunCode, latitude, longitude" +
            " from attraction_info where content_id = #{contentId};")
    AttractionInfoDto findAttractionById(int contentId);

}
