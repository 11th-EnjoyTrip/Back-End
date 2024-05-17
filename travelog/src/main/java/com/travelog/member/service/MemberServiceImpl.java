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

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberDao = memberDao;
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
    public MemberDto getByUserName(String username) throws SQLException {
        return memberDao.getByUsername(username);
    }

    @Override
    public MemberDto getByNickName(String nickname) throws SQLException {
        return memberDao.getByNickname(nickname);
    }

    @Override
    public MemberDto getByEmail(String email) throws SQLException {
        return memberDao.getByEmail(email);
    }

    @Override
    public MemberDto getByToken(String token) throws SQLException {

        return memberDao.getByToken(token);
    }

    @Override
    public HttpStatus regist(MemberDto memberDto) throws SQLException {

        if (memberDao.getById(memberDto.getUserid()) != null) {
            return HttpStatus.CONFLICT;
        }

        if (memberDao.getByEmail(memberDto.getEmail()) != null) {
            return HttpStatus.CONFLICT;
        }

        memberDao.regist(memberDto);

        return HttpStatus.CREATED;
    }

    @Override
    public MemberDto login(MemberDto memberDto) throws SQLException {
        return memberDao.login(memberDto);
    }

    @Override
    public void updateNickname(String nickname, String id) throws SQLException {
        memberDao.updateNickname(nickname, id);
    }

    @Override
    public void updatePassword(String password, String id) throws SQLException {
        memberDao.updatePassword(password, id);
    }

    @Override
    public void deleteMember(String id) throws SQLException {
        memberDao.deleteMember(id);
    }

    @Override
    public void saveRefreshToken(String id, String accessToken) throws SQLException {
        memberDao.saveRefreshToken(id, accessToken);
    }

    @Override
    public void deleteRefreshToken(String id) throws SQLException {
        memberDao.deleteToken(id);
    }

    @Override
    public String getPassword(String id, String email) throws SQLException {
        return memberDao.getPassword(id, email);
    }

    @Override
    public String getToken(String id) throws SQLException {
        return memberDao.getToken(id);
    }
}
