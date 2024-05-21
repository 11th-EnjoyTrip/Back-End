package com.travelog.review.dao;

import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.ReviewLikeDto;
import org.apache.ibatis.annotations.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface ReviewDao {

    @Insert(value = "INSERT INTO review (userid, content_id, nickname, review_text) values (#{userid}, #{content_id}, #{nickname}, #{review_text})")
    void write(ReviewDto reviewDto) throws Exception;

    @Select(value = "SELECT * FROM review WHERE userid = #{user_id}")
    List<ReviewDto> getReviewsByUserid(String user_id) throws Exception;

    @Select(value = "SELECT * FROM review WHERE content_id = #{content_id}")
    List<ReviewDto> getReviewsByContentId(String content_id) throws Exception;

    @Update(value = "UPDATE review set review_text = #{text}, update_time = now() WHERE review_id = #{review_id} ")
    void update(String text, int review_id);

    @Select(value = "SELECT userid FROM review WHERE content_id = #{content_id}")
    String getIdByContent_id(String content_id);

    @Select(value = "SELECT userid FROM review WHERE review_id = #{review_id}")
    String getIdByReview_id(int review_id);

    @Select(value = "SELECT a.review_id, a.review_text, a.content_id " +
            "FROM review as a join review_like as b on a.review_id " +
            "WHERE a.userid = #{userid}")
    List<ResponseReviewDto> getResponseReviewsByUserid(String review_id);

    @Select(value = "SELECT a.content_id, a.review_id, a.review_text, a.content_id " +
            "FROM review as a join review_like as b ON a.review_id = b.review_id " +
            "WHERE a.userid = #{userid};")
    List<ResponseReviewDto> getReviewLikeByUserid(String userid) throws SQLException;
    @Delete(value = "DELETE FROM review WHERE review_id = #{review_id}")
    void delete(int review_id);

    @Insert(value = "INSERT INTO review_like(review_id, userid) values (#{review_id}, #{userid})")
    void addLike(ReviewLikeDto reviewLikeDto);

    @Delete(value = " DELETE FROM review_like where review_id=#{review_id} AND userid = #{userid}")
    void deleteLike(ReviewLikeDto reviewLikeDto);
}
