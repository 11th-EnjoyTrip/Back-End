package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {
    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;
    @Select(value = "INSERT INTO member (id, password, name, email, location) values (#{id},#{password},#{name},#{email},#{location})")
    void regist(MemberDto memberDto) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{memberId}")
    MemberDto getById(String memberId) throws SQLException;
    @Select(value = "SELECT * FROM member WHERE id = #{id} and password = #{password}")
    MemberDto login(String id, String password) throws SQLException;

}
