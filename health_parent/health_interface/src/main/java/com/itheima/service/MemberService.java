package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Member;

import java.text.ParseException;

public interface MemberService {
    Member findByTelephone(String telephone);

    void addMember(Member member);

    Result getMemberReport() throws ParseException;
}
