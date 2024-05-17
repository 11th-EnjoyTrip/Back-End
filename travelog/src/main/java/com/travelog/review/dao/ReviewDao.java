package com.travelog.review.dao;

import com.travelog.review.dto.ReviewDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReviewDao {

    @Insert(value = "INSERT INTO review (userid, content_id, nickname, review_text) values (#{userid}, #{content_id}, #{nickname}, #{review_text})")
    void write(ReviewDto reviewDto) throws Exception;

    @Select(value = "SELECT * FROM review WHERE userid = #{user_id}")
    List<ReviewDto> written(String user_id) throws Exception;

    @Update(value = "UPDATE review set review_text = #{text} WHERE content_id = #{content_id} ")
    void update(String text, String content_id);

    @Select(value = "SELECT userid FROM review WHERE content_id = #{content_id}")
    String getIdByContent_id(String content_id);
}
