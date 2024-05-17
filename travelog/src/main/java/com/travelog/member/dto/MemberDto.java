package com.travelog.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MemberDto implements Serializable {
    private int no;
    private String userid;
    private String password;
    private String username;
    private String email;
    private String location;
    private String nickname;
    private String role;
    private String token;
}
