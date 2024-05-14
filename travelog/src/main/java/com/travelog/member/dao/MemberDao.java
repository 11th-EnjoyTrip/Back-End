package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {
    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;

    @Insert(value = "INSERT INTO member (id, password, name, email, location, nickname) values (#{id},#{password},#{username},#{email},#{location}, #{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "no")
    void regist(MemberDto member) throws SQLException;

    @Update(value = "UPDATE member set token = #{token} where id = #{id}")
    void saveRefreshToken(String id, String token) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{id}")
    MemberDto getById(String id) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE email = #{email}")
    MemberDto getByEmail(String email) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{id} and password = #{password}")
    MemberDto login(MemberDto member) throws SQLException;

}
