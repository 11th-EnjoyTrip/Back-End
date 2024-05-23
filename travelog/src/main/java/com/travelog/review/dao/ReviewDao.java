package com.travelog.review.dao;

import com.travelog.review.dto.MyPageReviewDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.ReviewLikeDto;
import org.apache.ibatis.annotations.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface ReviewDao {

    // Review 기본 CRUD
    @Insert(value = "INSERT INTO review (userid, content_id, nickname, review_text) values (#{userid}, #{content_id}, #{nickname}, #{review_text})")
    void write(ReviewDto reviewDto) throws Exception;

    // 수정
    // 상세 페이지 리뷰 확인
    // 로그인 O
    @Select(value = "SELECT " +
            " c.review_id, c.content_id, c.title, c.review_text, c.likes, c.update_time, c.userid, c.nickname, " +
            " CASE WHEN b.userid IS NOT NULL THEN true ELSE false END AS checkliked " +
            "FROM " +
            "(select a.title, b.review_id, b.userid, b.content_id, b.nickname, b.likes, b.update_time, b.review_text " +
            "from attraction_info as a join review as b on a.content_id = b.content_id) as c " +
            "LEFT JOIN review_like AS b " +
            "ON c.userid = b.userid AND c.review_id = b.review_id AND b.userid = '김정인' " +
            "WHERE c.content_id = 125266 " +
            "GROUP BY c.review_id " +
            "ORDER BY likes DESC;")
    List<MyPageReviewDto> getReviewsByContentId(String content_id) throws Exception;

    // 로그인 X
    @Select(value =
            "select a.title, b.review_id, b.userid, b.content_id, b.nickname, b.likes, b.update_time, b.review_text " +
                    "from attraction_info as a join review as b on a.content_id = b.content_id ;")
    List<ResponseReviewDto> getResponseReviewsByContentId(String content_id) throws Exception;

    @Update(value = "UPDATE review set review_text = #{text}, update_time = now() WHERE review_id = #{review_id} ")
    void update(String text, int review_id);

    @Update(value = "UPDATE review set likes = likes + 1 WHERE review_id = #{review_id}")
    void updateLike(int review_id);

    @Delete(value = "DELETE FROM review WHERE review_id = #{review_id}")
    void delete(int review_id);

    @Select(value = "SELECT * FROM review ORDER BY likes DESC LIMIT 5")
    List<ReviewDto> getTopReview() throws SQLException;

    // 좋아요 추가 or 삭제
    @Insert(value = "INSERT INTO review_like(review_id, userid) values (#{review_id}, #{userid})")
    void addLike(ReviewLikeDto reviewLikeDto);

    @Delete(value = " DELETE FROM review_like where review_id=#{review_id} AND userid = #{userid}")
    void deleteLike(ReviewLikeDto reviewLikeDto);

    // 작성자 아이디 찾기
    @Select(value = "SELECT userid FROM review WHERE review_id = #{review_id}")
    String getIdByReview_id(int review_id);

    // My Page에 사용될 Dao
    @Select(value = "SELECT " +
            " c.review_id, c.content_id, c.title, c.review_text, c.likes, c.update_time, c.userid, c.nickname, " +
            " CASE WHEN b.userid IS NOT NULL THEN true ELSE false END AS checkliked " +
            "FROM " +
            "(select a.title, b.review_id, b.userid, b.content_id, b.nickname, b.likes, b.update_time, b.review_text " +
            "from attraction_info as a join review as b on a.content_id = b.content_id) as c " +
            "LEFT JOIN review_like AS b " +
            "ON c.userid = b.userid AND c.review_id = b.review_id AND b.userid = #{content_id} " +
            "WHERE c.content_id = 125266 AND c.userid = #{content_id} " +
            "GROUP BY c.review_id " +
            "ORDER BY likes DESC;")
    List<MyPageReviewDto> getMyPageReviewsByContentId(@Param("content_id") String content_id);


    // 수정
    @Select(value = "SELECT " +
            " c.review_id, c.content_id, c.title, c.review_text, c.likes, c.update_time, c.userid, c.nickname, " +
            " CASE WHEN b.userid IS NOT NULL THEN true ELSE false END AS checkliked " +
            "FROM " +
            "(select a.title, b.review_id, b.userid, b.content_id, b.nickname, b.likes, b.update_time, b.review_text " +
            "from attraction_info as a join review as b on a.content_id = b.content_id) as c " +
            "LEFT JOIN review_like AS b " +
            "ON c.userid = b.userid AND c.review_id = b.review_id AND b.userid = #{userid} " +
            "WHERE c.content_id = 125266 AND c.userid = #{userid} " +
            "GROUP BY c.review_id " +
            "ORDER BY likes DESC;")
    List<MyPageReviewDto> getReviewsByUserid(@Param("userid")String userid) throws Exception;
}
