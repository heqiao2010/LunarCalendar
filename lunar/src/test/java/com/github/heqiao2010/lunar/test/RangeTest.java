package com.github.heqiao2010.lunar.test;

import com.github.heqiao2010.lunar.LunarCalendar;
import org.junit.Test;

import java.util.Calendar;

public class RangeTest {

    @Test(expected = IllegalArgumentException.class)
    public void rangeTest() {
        // 2021年没有腊月三十
        LunarCalendar lunar = new LunarCalendar(2021, 12, 30, false);
        System.out.println(lunar);
        Calendar today = LunarCalendar.lunar2Solar(lunar.getLunarYear(), lunar.getLunarMonth(), lunar.getDayOfLunarMonth(), lunar.isLeapMonth());
        System.out.println(lunar.getFullLunarName() + " <====> " + today.getTime());
    }
}
