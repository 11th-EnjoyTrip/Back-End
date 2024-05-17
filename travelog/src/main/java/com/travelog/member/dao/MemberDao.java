package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
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

    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;
    @Select(value = "SELECT password FROM member WHERE userid = #{userid} and email = #{email}")
    String getPassword(String userid, String email) throws SQLException;
    @Select(value = "SELECT token FROM member WHERE userid = #{userid}")
    String getToken(String userid) throws SQLException;

    @Select(value = "SELECT * FROM member WHERE userid = #{userid}")
    MemberDto getById(String userid) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE username=#{username}")
    MemberDto getByUsername(String username) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE nickname = #{nickname}")
    MemberDto getByNickname(String nickname) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE email = #{email}")
    MemberDto getByEmail(String email) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE token = #{token}")
    MemberDto getByToken(String token) throws SQLException;

    @Select(value = "SELECT * FROM member WHERE userid = #{userid} and password = #{password}")
    MemberDto login(MemberDto member) throws SQLException;


}
