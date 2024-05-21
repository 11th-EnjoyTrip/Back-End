package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.dto.ResponseMemberDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {


    @Insert(value = "INSERT INTO member (userid, password, username, email, location, nickname, role, token) values (#{userid},#{password},#{username},#{email},#{location}, #{nickname}, '사용자', #{token})")
    @Options(useGeneratedKeys = true, keyProperty = "no")
    void regist(MemberDto member) throws SQLException;

    @Update(value = "UPDATE member set token = #{token} where userid = #{userid}")
    void saveRefreshToken(String userid, String token) throws SQLException;

    @Update(value = "UPDATE member set nickname = #{nickname} where userid = #{userid}")
    void updateNickname(String nickname, String userid) throws SQLException;

    @Update(value = "UPDATE member set password = #{password} where userid = #{userid}")
    void updatePassword(String password, String userid) throws SQLException;

    @Update(value = "Update member set token = null where userid = #{userid} ")
    void deleteToken(String userid) throws SQLException;

    @Delete(value = "DELETE FROM member where userid = #{userid}")
    void deleteMember(String userid) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member")
    List<ResponseMemberDto> getMembers() throws SQLException;

    @Select(value = "SELECT password FROM member WHERE userid = #{userid} and email = #{email}")
    String getPassword(String userid, String email) throws SQLException;

    @Select(value = "SELECT token FROM member WHERE userid = #{userid}")
    String getToken(String userid) throws SQLException;

    @Select(value = "SELECT * FROM member WHERE userid = #{userid}")
    MemberDto getMemberDtoById(String userid) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE userid = #{userid}")
    ResponseMemberDto getResponseMemberDtoById(String userid) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE username=#{username}")
    ResponseMemberDto getByUsername(String username) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE nickname = #{nickname}")
    ResponseMemberDto getByNickname(String nickname) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE email = #{email}")
    ResponseMemberDto getByEmail(String email) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE token = #{token}")
    ResponseMemberDto getByToken(String token) throws SQLException;

    @Select(value = "SELECT a.content_id, a.review_id, a.review_text, a.content_id " +
            "FROM review as a join review_like as b ON a.review_id = b.review_id " +
            "WHERE a.userid = #{userid};")
    List<ResponseReviewDto> getReviewLikeByUserid(String userid) throws SQLException;

    @Select(value = "SELECT userid, username, email, location, nickname FROM member WHERE userid = #{userid} and password = #{password}")
    ResponseMemberDto login(MemberDto member) throws SQLException;

}
