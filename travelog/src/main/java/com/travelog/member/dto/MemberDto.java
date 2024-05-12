package com.travelog.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MemberDto implements Serializable {
    private int no;
    private String id;
    private String password;
    private String name;
    private String email;
    private String location;
}
