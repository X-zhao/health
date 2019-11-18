package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


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

    @Override
    public Map getBusinessReportData() throws Exception {
        HashMap<String, Object> data = new HashMap<>();

        Date date = new Date();
        String date2String = DateUtils.parseDate2String(date);
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(date);

        data.put("reportDate", date2String);
        data.put("todayNewMember",memberDao.findMemberCountAfterDate(date2String));
        data.put("totalMember",memberDao.findMemberTotalCount());
        data.put("thisWeekNewMember",memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(firstDayOfWeek)));
        data.put("thisMonthNewMember",memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth())));
        data.put("todayOrderNumber",orderDao.findOrderCountAfterDate(date2String));
        data.put("todayVisitsNumber",orderDao.findVisitsCountAfterDate(date2String));
        data.put("thisWeekOrderNumber",orderDao.findOrderCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(date))));
        data.put("thisWeekVisitsNumber",orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(date))));
        data.put("thisMonthOrderNumber",orderDao.findOrderCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth())));
        data.put("thisMonthVisitsNumber",orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth())));
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        data.put("hotSetmeal",hotSetmeal);

        return data;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.add(Calendar.DAY_OF_YEAR,0);
        System.out.println("今天的日期是"+calendar.get(Calendar.DATE));
        System.out.println("今天是本周的第 天"+calendar.get(Calendar.DAY_OF_WEEK));
        int i = calendar.get(Calendar.DAY_OF_WEEK)-1;
        calendar.add(Calendar.DATE,-i);
        System.out.println("本周周一日期为:"+calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE,6);
        System.out.println("本周周日日期为:"+calendar.get(Calendar.DATE));
    }

}
