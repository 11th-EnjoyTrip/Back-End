package com.travelog.member.service;

import com.travelog.member.dto.MemberDto;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;

public interface MemberService {
    List<MemberDto> getMembers() throws Exception;
    MemberDto getById(String memberId) throws SQLException;
    HttpStatus regist(MemberDto memberDto) throws SQLException;
    MemberDto login(String id, String password) throws SQLException;
}
