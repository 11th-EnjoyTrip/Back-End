package com.travelog.member.service;

import com.travelog.member.dao.MemberDao;
import com.travelog.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberDao memberDao;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public List<MemberDto> getMembers() throws Exception {
        return  memberDao.getMembers();
    }

    @Override
    public MemberDto getById(String memberId) throws SQLException {
        return memberDao.getById(memberId);
    }

    @Override
    public MemberDto regist(MemberDto memberDto) throws SQLException {
        return memberDao.regist(memberDto);
    }

    @Override
    public MemberDto login(String id, String password) throws SQLException {
        return memberDao.login(id, password);
    }
}
