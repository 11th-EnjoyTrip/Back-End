package com.travelog.member.service;

import com.travelog.member.dao.MemberDao;
import com.travelog.member.dto.MemberDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private SqlSession sqlSession;
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
    public MemberDto memberInfo(String memberId) throws SQLException {
        return memberDao.memberInfo(memberId);
    }
}
