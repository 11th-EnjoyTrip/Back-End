package com.travelog.travelog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
public class Member {
    private String id;
    private String password;
    private String name;
    private String email;
    private String favorite;
}
