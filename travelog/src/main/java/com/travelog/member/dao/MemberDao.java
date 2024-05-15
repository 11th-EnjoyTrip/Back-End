package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {


    @Insert(value = "INSERT INTO member (id, password, username, email, location, nickname, role, token) values (#{id},#{password},#{username},#{email},#{location}, #{nickname}, '사용자', #{token})")
    @Options(useGeneratedKeys = true, keyProperty = "no")
    void regist(MemberDto member) throws SQLException;

    @Update(value = "UPDATE member set token = #{token} where id = #{id}")
    void saveRefreshToken(String id, String token) throws SQLException;
    @Update(value = "UPDATE member set nickname = #{nickname} where id = #{id}")
    void updateNickname(String nickname, String id) throws SQLException;
    @Update(value = "UPDATE member set password = #{password} where id = #{id}")
    void updatePassword(String password, String id) throws SQLException;
    @Update(value = "Update member set token = null where id = #{id} ")
    void deleteToken(String id) throws SQLException;
    @Delete(value = "DELETE FROM member where id = #{id}")
    void deleteMember(String id) throws SQLException;
    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{id}")
    MemberDto getById(String id) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE username=#{username}")
    MemberDto getByUsername(String username) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE nickname = #{nickname}")
    MemberDto getByNickname(String nickname) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE email = #{email}")
    MemberDto getByEmail(String email) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE token = #{token}")
    MemberDto getByToken(String token) throws SQLException;

    @Select(value = "SELECT * FROM member WHERE id = #{id} and password = #{password}")
    MemberDto login(MemberDto member) throws SQLException;
    @Select(value = "SELECT password FROM member WHERE id = #{id} and email = #{email}")
    String getPassword(String id, String email) throws SQLException;

}
