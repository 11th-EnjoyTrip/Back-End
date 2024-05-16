package com.travelog.member.service;

import com.travelog.member.dto.MemberDto;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;

public interface MemberService {

    MemberDto getById(String memberId) throws SQLException;
    MemberDto getByUserName(String username) throws SQLException;
    MemberDto getByNickName(String nickname) throws SQLException;
    MemberDto getByEmail(String email) throws SQLException;
    MemberDto getByToken(String token) throws SQLException;

    HttpStatus regist(MemberDto memberDto) throws SQLException;
    MemberDto login(MemberDto memberDto) throws SQLException;

    void updateNickname(String nickname, String id) throws SQLException;
    void updatePassword(String password, String id) throws SQLException;
    void deleteMember(String id) throws SQLException;

    void saveRefreshToken(String id, String accessToken) throws SQLException;
    void deleteRefreshToken(String id) throws SQLException;

    List<MemberDto> getMembers() throws Exception;
    String getPassword(String id, String email) throws SQLException;
    String getToken(String id) throws SQLException;
}
