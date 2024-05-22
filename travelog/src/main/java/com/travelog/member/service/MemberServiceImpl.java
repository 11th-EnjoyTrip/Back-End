package com.travelog.member.service;

import com.travelog.member.dao.MemberDao;
import com.travelog.member.dto.MemberDto;
import com.travelog.member.dto.ResponseMemberDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberDao = memberDao;
    }

    @Override
    public List<ResponseMemberDto> getMembers() throws Exception {
        return memberDao.getMembers();
    }

    @Override
    public MemberDto getMemberDtoById(String userid) throws SQLException {
        return memberDao.getMemberDtoById(userid);
    }

    @Override
    public ResponseMemberDto getResponseMemberDtoById(String userid) throws SQLException {
        return memberDao.getResponseMemberDtoById(userid);
    }

    @Override
    public ResponseMemberDto getByUserName(String username) throws SQLException {
        return memberDao.getByUsername(username);
    }

    @Override
    public ResponseMemberDto getByNickName(String nickname) throws SQLException {
        return memberDao.getByNickname(nickname);
    }

    @Override
    public ResponseMemberDto getByEmail(String email) throws SQLException {
        return memberDao.getByEmail(email);
    }

    @Override
    public ResponseMemberDto getByToken(String token) throws SQLException {

        return memberDao.getByToken(token);
    }

    @Override
    public HttpStatus regist(MemberDto memberDto) throws SQLException {

        if (memberDao.getMemberDtoById(memberDto.getUserid()) != null) {
            return HttpStatus.CONFLICT;
        }

        if (memberDao.getByEmail(memberDto.getEmail()) != null) {
            return HttpStatus.CONFLICT;
        }

        memberDao.regist(memberDto);

        return HttpStatus.CREATED;
    }

    @Override
    public List<ResponseReviewDto[]> getReviewLikeByUserid(String userid) throws SQLException {

        List<ResponseReviewDto[]> result = new ArrayList<>();
        List<ResponseReviewDto> reviews = memberDao.getReviewLikeByUserid(userid);
        System.out.println("결과로 받아온 리뷰 개수 : " + reviews.size());
        int listIdx = 0;
        int idx = 0;

        result.add(new ResponseReviewDto[10]);
        for (ResponseReviewDto review : reviews) {
            result.get(listIdx)[idx] = review;
            if(++idx == 10) {
                idx = 0;
                result.add(new ResponseReviewDto[10]);
                listIdx++;
            }
        }
        System.out.println(result);
        return result;
    }

    @Override
    public ResponseMemberDto login(MemberDto memberDto) throws SQLException {

        // 1. id String id = service.getId();
                            // throws validateID;
        // 핸들러

        // 2. pwd
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
