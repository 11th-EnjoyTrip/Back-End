package com.travelog.member.service;

import com.travelog.member.dto.MemberDto;

import java.sql.SQLException;
import java.util.List;

public interface MemberService {
    List<MemberDto> getMembers() throws Exception;
    MemberDto memberInfo(String memberId) throws SQLException;

}
