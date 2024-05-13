package com.travelog.member.service;

import com.travelog.member.dao.MemberDao;
import com.travelog.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberDao = memberDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<MemberDto> getMembers() throws Exception {
        return memberDao.getMembers();
    }

    @Override
    public MemberDto getById(String memberId) throws SQLException {
        return memberDao.getById(memberId);
    }

    @Override
    public HttpStatus regist(MemberDto memberDto) throws SQLException {

        if (memberDao.getById(memberDto.getId()) != null) {
            System.out.println("아이디가 겹침");
            return HttpStatus.CONFLICT;
        }

        if (memberDao.getByEmail(memberDto.getEmail()) != null) {
            System.out.println("이메일이 겹침");
            return HttpStatus.CONFLICT;
        }


        memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        memberDao.regist(memberDto);
        return HttpStatus.CREATED;
    }

    @Override
    public MemberDto login(String id, String password) throws SQLException {
        return memberDao.login(id, password);
    }
}
