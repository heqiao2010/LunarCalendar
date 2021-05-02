package com.github.heqiao2010.lunar.test;

import com.github.heqiao2010.lunar.LunarCalendar;
import com.github.heqiao2010.lunar.LunarCodes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;

public class AddByLunarTest {
    @Test
    public void addByLunarMonth1() {
        final LunarCalendar lunar = new LunarCalendar(2020, 5, 20, false);
        Assert.assertEquals("二〇二〇年五月二十", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 3);
        Assert.assertEquals("二〇二〇年八月二十", lunar.toString());
    }

    /**
     * 闰月
     */
    @Test
    public void addByLunarMonth11() {
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2020)));
        final LunarCalendar lunar = new LunarCalendar(2020, 4, 20, false);
        Assert.assertEquals("二〇二〇年四月二十", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 1);
        Assert.assertEquals("二〇二〇年闰四月二十", lunar.toString());
    }

    /**
     * 闰月
     */
    @Test
    public void addByLunarMonth12() {
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2020)));
        final LunarCalendar lunar = new LunarCalendar(2020, 4, 20, true);
        Assert.assertEquals("二〇二〇年闰四月二十", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, -1);
        Assert.assertEquals("二〇二〇年四月二十", lunar.toString());
    }

    /**
     * 2021年腊月没有三十
     */
    @Test
    public void addByLunarMonth2() {
        final LunarCalendar lunar = new LunarCalendar(2021, 11, 30, false);
        Assert.assertEquals("二〇二一年冬月三十", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 1);
        Assert.assertEquals("二〇二一年腊月廿九", lunar.toString());
    }

    @Test
    public void addByLunarMonth3() {
        final LunarCalendar lunar = new LunarCalendar(2019, 12, 30, false);
        Assert.assertEquals("二〇一九年腊月三十", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 1);
        Assert.assertEquals("二〇二〇年正月廿九", lunar.toString());
    }

    @Test
    public void addByLunarMonth4() {
        final LunarCalendar lunar = new LunarCalendar(2020, 1, 1, false);
        Assert.assertEquals("二〇二〇年正月初一", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 5);
        Assert.assertEquals("二〇二〇年五月初一", lunar.toString());
    }

    @Test
    public void addByLunarMonth5() {
        final LunarCalendar lunar = new LunarCalendar(2020, 5, 1, false);
        Assert.assertEquals("二〇二〇年五月初一", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, -5);
        Assert.assertEquals("二〇二〇年正月初一", lunar.toString());
    }

    @Test
    public void addByLunarMonth6() {
        final LunarCalendar lunar = new LunarCalendar(2020, 5, 1, false);
        Assert.assertEquals("二〇二〇年五月初一", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, -6);
        Assert.assertEquals("二〇一九年腊月初一", lunar.toString());
    }

    @Test
    public void addByLunarMonth7() {
        final LunarCalendar lunar = new LunarCalendar(2020, 6, 1, false);
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2020)));
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2019)));
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2018)));

        Assert.assertEquals("二〇二〇年六月初一", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, -19);
        Assert.assertEquals("二〇一八年腊月初一", lunar.toString());
    }

    @Test
    public void addByLunarMonth8() {
        final LunarCalendar lunar = new LunarCalendar(2018, 12, 1, false);
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2018)));
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2019)));
        System.out.println(Arrays.toString(LunarCodes.monthCodes(2020)));

        Assert.assertEquals("二〇一八年腊月初一", lunar.toString());
        lunar.addByLunar(Calendar.MONTH, 19);
        Assert.assertEquals("二〇二〇年六月初一", lunar.toString());
    }

    @Test
    public void addByLunarYear1() {
        final LunarCalendar lunar = new LunarCalendar(2020, 12, 30, false);
        Assert.assertEquals("二〇二〇年腊月三十", lunar.toString());
        lunar.addByLunar(Calendar.YEAR, 1);
        Assert.assertEquals("二〇二一年腊月廿九", lunar.toString());
    }

    @Test
    public void addByLunarYear2() {
        final LunarCalendar lunar = new LunarCalendar(2020, 12, 30, false);
        Assert.assertEquals("二〇二〇年腊月三十", lunar.toString());
        lunar.addByLunar(Calendar.YEAR, -1);
        Assert.assertEquals("二〇一九年腊月三十", lunar.toString());
    }
}
