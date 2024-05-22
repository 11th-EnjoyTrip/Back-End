package com.travelog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto implements Serializable {
    private int id;
    private String userid;
    private String password;
    private String username;
    private String email;
    private String location;
    private String nickname;
    private String role;
    private String token;
}
