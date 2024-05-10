package com.travelog.member.dto;

import lombok.Data;

@Data
public class MemberDto {
    private String id;
    private String password;
    private String name;
    private String email;
    private String favorite;
}
