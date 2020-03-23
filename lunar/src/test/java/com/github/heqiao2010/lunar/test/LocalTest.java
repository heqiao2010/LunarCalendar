package com.github.heqiao2010.lunar.test;

import com.github.heqiao2010.lunar.LunarCalendar;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Calendar;

/**
 * Created by heqiao on 2019/3/31.
 */
public class LocalTest {
    /**
     * 打印MINI_YEAR-01-31到MAX_YEAR-12-31所有的农历,并输出到txt中
     */
    @Test
    public void localTest1(){
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // start
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, LunarCalendar.MINI_YEAR);
        start.set(Calendar.MONTH, 1);
        start.set(Calendar.DATE, 12);
        // end
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, LunarCalendar.MAX_YEAR);
        end.set(Calendar.MONTH, 11);
        end.set(Calendar.DATE, 31);
        FileOutputStream out = null;
        PrintStream p = null;
        try {
            File testFile = new File("./resources/solar2lunar.txt");
            if(!testFile.exists()){
                testFile.createNewFile();
            }
            out = new FileOutputStream(testFile);
            p = new PrintStream(out);
            Calendar t = start;
            while(t.before(end) || t.equals(end)) {
                LunarCalendar lunar = LunarCalendar.solar2Lunar(t);
                System.out.println(df.format(t.getTime()) + " <====> " + lunar.getFullLunarName());
                p.println(df.format(t.getTime()) + " <====> " + lunar.getFullLunarName());
                t.add(Calendar.DATE, 1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到solar2lunar.txt文件，或者文件创建失败.");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("未找到solar2lunar.txt文件，或者文件创建失败.");
            e.printStackTrace();
        }finally {
            try {
                out.close();
                p.close();
            } catch (IOException e) {
                System.out.println("关闭流出错。");
                e.printStackTrace();
            }
        }
    }

    @Test
    public void localTest2(){
        final java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

        int lunarDay = 9;
        int lunarMonth = 12;
        int lunarYear = 1048;
        Assert.assertEquals("初九", LunarCalendar.getDayName(lunarDay));
        Assert.assertEquals('腊', LunarCalendar.getMonthName(lunarMonth));
        Assert.assertEquals("一〇四八", LunarCalendar.getYearName(lunarYear));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2019);
        c.set(Calendar.MONTH, 2);
        c.set(Calendar.DATE, 31);
        LunarCalendar lunar = new LunarCalendar(c);
        System.out.println(df.format(c.getTime()) + " -> " + lunar);
        Assert.assertEquals("二〇一九年二月廿五", lunar.toString());

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        System.out.println(df.format(c1.getTime()));
        c1.add(Calendar.MONTH, 10);
        System.out.println(df.format(c1.getTime()));
        // int[] arr1900 = { 8, 131, 301, 331, 429, 528, 627, 726, 825, 924, 1023, 1122, 1222, 1320 };
        // System.out.println(LunarCalendar.binSearch(arr1900, 1121));

        c1.set(Calendar.YEAR, 1991);
        c1.set(Calendar.MONTH, 3);
        c1.set(Calendar.DATE, 1);


        c2.set(Calendar.YEAR, 1991);
        c2.set(Calendar.MONTH, 2);
        c2.set(Calendar.DATE, 1);

        System.out.println(LunarCalendar.solarDiff(c1, c2, Calendar.DATE));
        System.out.println(df.format(c1.getTime()));
        System.out.println(df.format(c2.getTime()));
    }

    @Test
    public void localTest3(){
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();

        c1.set(Calendar.YEAR, 2046);
        c1.set(Calendar.MONTH, 01);
        c1.set(Calendar.DATE, 06);
        LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
        System.out.println();
        System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
    }

    @Test
    public void localTest4(){
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();

        c1.set(Calendar.YEAR, 2020);
        c1.set(Calendar.MONTH, 02);
        c1.set(Calendar.DATE, 23);
        LunarCalendar lunar = LunarCalendar.solar2Lunar(c1);
        System.out.println("Solar：" + df.format(lunar.getTime()) + " Lunar：" + lunar);
        lunar.add(Calendar.DATE, 1);
        System.out.println();
        System.out.println("Solar：" + df.format(lunar.getTime()) + " Lunar：" + lunar);
    }
}
