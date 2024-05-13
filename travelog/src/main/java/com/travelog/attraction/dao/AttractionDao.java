package com.travelog.attraction.dao;

import com.travelog.attraction.dto.AttractionDetailDto;
import com.travelog.util.RequestList;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttractionDao {

    @Select("SELECT" +
            "    content_id,content_type_id," +
            "   CASE " +
            "        WHEN content_type_id = 12 THEN '관광지'" +
            "        WHEN content_type_id = 14 THEN '문화시설'" +
            "        WHEN content_type_id = 15 THEN '축제/공연/행사'" +
            "        WHEN content_type_id = 28 THEN '레포츠'" +
            "        WHEN content_type_id = 32 THEN '숙박'" +
            "        WHEN content_type_id = 38 THEN '쇼핑'" +
            "        WHEN content_type_id = 39 THEN '음식'" +
            "        ELSE '기타'" +
            "    END AS content_type_name,title,first_image,ai.sido_code,s.sido_name,latitude,longitude " +
            "FROM attraction_info ai JOIN sido s ON ai.sido_code = s.sido_code " +
            "WHERE" +
            "    ai.sido_code = IF(#{data.region} = 0,ai.sido_code,#{data.region})" +
            "    AND  FIND_IN_SET(content_type_id, #{data.category})" +
            "    AND title LIKE CONCAT('%', #{data.keyword}, '%')"+
            "LIMIT #{pageable.pageSize} OFFSET #{pageable.offset}")
    List<Map<String,Object>> getAttractionList(RequestList<?> requestList) throws SQLException;

    @Select("select ai.content_id, content_type_id," +
            "case " +
            "when content_type_id = 12 then '관광지'" +
            "    when content_type_id = 14 then '문화시설'" +
            "    when content_type_id = 15 then '축제/공연/행사'" +
            "    when content_type_id = 28 then '레포츠'" +
            "    when content_type_id = 32 then '숙박'" +
            "    when content_type_id = 38 then '쇼핑'" +
            "    when content_type_id = 39 then '음식'" +
            "else '기타' end as content_type_name" +
            ",title,addr1,addr2,zipcode,tel,first_image," +
            "ai.sido_code,s.sido_name,latitude,longitude,ad.overview" +
            "from attraction_info ai join sido s on ai.sido_code = s.sido_code" +
            "join attraction_description ad on ai.content_id = ad.content_id" +
            "where ai.content_id = #{id}")
    AttractionDetailDto getAttractionDetail(int id) throws SQLException;

    // " OFFSET #{pageable.offset} ROWS FETCH NEXT #{pageable.pageSize} ROWS ONLY")
    // Select ArticleList
//    @Select(value = "select * from board order by id")
//    @Results(id = "list", value = {
//            @Result(property = "id", column = "id"),
//            @Result(property = "writer", column = "writer"),
//            @Result(property = "title", column = "title"),
//            @Result(property = "content", column = "content"),
//            @Result(property = "view_cnt", column = "view_cnt"),
//            @Result(property = "reg_data", column = "reg_data"),
//    })
//    List<BoardDto> listArticle() throws SQLException;

}
