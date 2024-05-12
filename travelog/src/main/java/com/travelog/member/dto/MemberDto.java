package com.travelog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private int no;
    private String id;
    private String password;
    private String name;
    private String email;
}
