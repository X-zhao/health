package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);

    }

    @Override
    public void addMember(Member member) {
        String password = member.getPassword();
        if (password != null) {
            String md5 = MD5Utils.md5(password);
            member.setPassword(md5);
        }
        memberDao.add(member);
    }

    @Override
    public Result getMemberReport() throws ParseException {
        //data: res.data.data.months
        //data: res.data.data.memberCount
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList months = new ArrayList<String>();
        List<Integer> memberCount = new ArrayList<Integer>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String dateStr = dateFormat.format(calendar.getTime());
            months.add(dateStr);

            Integer count = memberDao.findMemberCountBetweenDate(dateStr + "-00", dateStr + "-31");
            memberCount.add(count);
        }
        map.put("months",months);
        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


}
