package com.travelog.travelog.repository;

import com.travelog.travelog.domain.Member;

public interface MemberRepository {

    Member login(String id, String password);
}
