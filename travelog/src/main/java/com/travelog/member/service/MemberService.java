package com.travelog.member.service;

import com.travelog.member.dto.MemberDto;
import com.travelog.member.dto.ResponseMemberDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;

public interface MemberService {

    MemberDto getMemberDtoById(String userid) throws SQLException;
    ResponseMemberDto getResponseMemberDtoById(String userid) throws SQLException;
    ResponseMemberDto login(MemberDto memberDto) throws SQLException;
    ResponseMemberDto getByUserName(String username) throws SQLException;
    ResponseMemberDto getByNickName(String nickname) throws SQLException;
    ResponseMemberDto getByEmail(String email) throws SQLException;
    ResponseMemberDto getByToken(String token) throws SQLException;

    HttpStatus regist(MemberDto memberDto) throws SQLException;

    List<ResponseReviewDto[]> getReviewLikeByUserid(String userid) throws SQLException;

    void updateNickname(String nickname, String id) throws SQLException;
    void updatePassword(String password, String id) throws SQLException;
    void deleteMember(String id) throws SQLException;
    void saveRefreshToken(String id, String accessToken) throws SQLException;
    void deleteRefreshToken(String id) throws SQLException;

    List<ResponseMemberDto> getMembers() throws Exception;
    String getPassword(String id, String email) throws SQLException;
    String getToken(String id) throws SQLException;
}
