package com.travelog.travelog.controller;

import com.travelog.travelog.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value= "member")
public class MemberController {
    @GetMapping()
    public Member member() {
        return new Member("test","test","","test@gamil.com","");
    }

    @GetMapping("/all")
    public List<Member> all() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("test","test","","test@gamil.com",""));
        members.add(new Member("test","test","","test@gamil.com",""));
        members.add(new Member("test","test","","test@gamil.com",""));
        members.add(new Member("test","test","","test@gamil.com",""));
        return members;
    }
}
