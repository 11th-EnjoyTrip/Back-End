package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {
    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;

    @Insert(value = "INSERT INTO member (id, password, name, email, location, nickname) values (#{id},#{password},#{name},#{email},#{location}, #{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "no")
    void regist(MemberDto memberDto) throws SQLException;

    @Select(value = "SELECT * FROM member WHERE id = #{memberId}")
    MemberDto getById(String memberId) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE email = #{email}")
    MemberDto getByEmail(String email) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{id} and password = #{password}")
    MemberDto login(String id, String password) throws SQLException;

}
