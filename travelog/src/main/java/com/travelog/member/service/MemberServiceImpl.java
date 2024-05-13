package com.travelog.member.service;

import com.travelog.member.dao.MemberDao;
import com.travelog.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public HttpStatus regist(MemberDto memberDto) throws SQLException {
        if(memberDao.getById(memberDto.getId()) != null){
            System.out.println("아이디가 겹침");
            return HttpStatus.CONFLICT;
        }else{
            System.out.println("계정 생성");
            memberDao.regist(memberDto);
            return HttpStatus.CREATED;
        }
    }

    @Override
    public MemberDto login(String id, String password) throws SQLException {
        return memberDao.login(id, password);
    }
}
