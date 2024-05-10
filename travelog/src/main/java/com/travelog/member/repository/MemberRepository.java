package com.travelog.member.repository;

import com.travelog.member.domain.Member;

public interface MemberRepository {

    Member login(String id, String password);
}
