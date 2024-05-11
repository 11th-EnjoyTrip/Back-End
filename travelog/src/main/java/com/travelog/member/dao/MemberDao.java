package com.travelog.member.dao;

import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface MemberDao {
    @Select(value = "SELECT * FROM member")
    List<MemberDto> getMembers() throws SQLException;

    @Select(value = "SELECT * FROM member WHERE id = #{memberId}")
    public MemberDto memberInfo(String memberId) throws SQLException;

}
