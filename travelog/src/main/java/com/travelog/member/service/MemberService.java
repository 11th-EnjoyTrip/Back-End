package com.travelog.member.service;

import com.travelog.member.dto.MemberDto;

import java.util.List;

public interface MemberService {
    List<MemberDto> getMembers();
}
