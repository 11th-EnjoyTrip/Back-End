package com.travelog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResponseMemberDto implements Serializable {

    private String userid;
    private String username;
    private String email;
    private String location;
    private String nickname;

}
