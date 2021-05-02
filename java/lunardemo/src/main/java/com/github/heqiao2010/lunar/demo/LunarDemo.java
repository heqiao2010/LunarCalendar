package com.github.heqiao2010.lunar.demo;

import com.github.heqiao2010.lunar.LunarCalendar;

import java.util.Calendar;

public class LunarDemo {
    public static void main(String[] args) {
        // 公历转农历：
        Calendar today = Calendar.getInstance();
        LunarCalendar lunar = LunarCalendar.solar2Lunar(today);
        System.out.println(today.getTime() + " <====> " + lunar.getFullLunarName());

        // 农历转公历：
        LunarCalendar lunar1 = new LunarCalendar();
        Calendar today1 = LunarCalendar.lunar2Solar(lunar.getLyear(), lunar.getLmonth(), lunar.getLdate(),
                lunar.isLeapMonth());
        System.out.println(lunar1.getFullLunarName() + " <====> " + today1.getTime());
    }
}
