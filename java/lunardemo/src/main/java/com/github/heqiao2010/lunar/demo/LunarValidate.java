package com.github.heqiao2010.lunar.demo;

import com.github.heqiao2010.lunar.LunarCalendar;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Calendar;

/**
 * 1901-2100年数据验证
 * 通过香港天文台提供的农历对照表数据进行验证：https://www.hko.gov.hk/tc/gts/time/conversion1_text.htm
 */
public class LunarValidate {
    public static void main(String args[]) {
        final int START = 1901;
        final int END = 2100;
        for (int year=START; year<=END; year++){
            String uri = "http://www.hko.gov.hk/tc/gts/time/calendar/text/files/T" + year + "c.txt";
            HttpGet get = new HttpGet(uri);
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                client = HttpClients.createDefault();
                // 发送请求
                response = client.execute(get);
                String content = EntityUtils.toString(response.getEntity(), "Big5");
                boolean pass = validateGanZhiAndAnimal(year, content) && validateLunarMonth(year, content);
                if (pass) {
                    System.out.println(year + " passed!");
                } else {
                    System.out.println(year + " not passe!");
                    return;
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                try{
                    if(null != client){
                        client.close();
                    }
                    if(null != response){
                        response.close();
                    }
                } catch (IOException iex) {
                    //ignore
                }
            }
        }
    }

    /**
     * 校验农历月份
     */
    private static boolean validateLunarMonth(int year, String content){
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy年MM月dd");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DATE, 1);

        LunarCalendar lunar = LunarCalendar.solar2Lunar(date);
        String[] lines = content.split("\n");

        int startLine = year == 2058 ? 0 : 3;
        for (int i=startLine; i<lines.length && lunar.getSolar().get(Calendar.YEAR)==year; i++) {
            String lunarMoth1;
            String fullLunarName = lunar.getFullLunarName();
            if (lunar.isLeapMonth()){
                lunarMoth1 = "閏" + LunarCalendar.getMonthName(lunar.getLmonth()) + "月";
            } else {
                lunarMoth1 = LunarCalendar.getMonthName(lunar.getLmonth()) + "月";
            }
            String lunarDate1 = LunarCalendar.getDayName(lunar.getLdate());
            String lunarDate2 = parseLunarDate(lines[i]);
            if (lunarDate2.endsWith("月")) {
                // 月份
                if (isMonthEquals(lunarMoth1, lunarDate2)) {
                    System.out.println(df.format(lunar.getSolar().getTime()) + " ==> " + fullLunarName + " passed!");
                } else {
                    System.out.println(df.format(lunar.getSolar().getTime()) + " not pass, line: " + lines[i]
                            + " date: " + lunarDate2 + " month: " + lunarMoth1);
                    return false;
                }
            } else {
                // 农历日期
                if (lunarDate1.equals(lunarDate2)) {
                    System.out.println(df.format(lunar.getSolar().getTime()) + " ==> " + fullLunarName + " passed!");
                } else {
                    System.out.println(df.format(lunar.getSolar().getTime()) + " not pass, line: " + lines[i]
                            + " date: " + lunarDate2);
                    return false;
                }
            }
            lunar.add(Calendar.DATE, 1);
        }
        return true;
    }

    /**
     * 校验天干地支以及生肖
     */
    private static boolean validateGanZhiAndAnimal(int year, String content) {
        // 由于公历年初可能还是农历的上一年，所以这里用6月1日来做转换，避免转化的农历还是上一年
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 5);
        date.set(Calendar.DATE, 1);

        LunarCalendar lunar = LunarCalendar.solar2Lunar(date);
        String[] lines = content.split("\n");

        String header = lines[0];
        String lunarYear = header.substring(0, 4);
        String ganZhi = getGanZhi(year, header);
        String animalName = getAnimal(year, header);
        System.out.println(year + " -> " + lunarYear + " " + ganZhi + " " + animalName);

        if (lunarYear.equals(String.valueOf(lunar.getLyear()))
                && (ganZhi + "年").equals(LunarCalendar.getTraditionalYearName(lunar.getLyear()))
                && isAnimalEquals(animalName, String.valueOf(LunarCalendar.getAnimalYearName(lunar.getLyear())))) {
            return true;
        }
        System.out.println(lunar.getFullLunarName());
        return false;
    }

    private static String getGanZhi(int year, String header){
        // 2058年没有干支数据
        if (year == 2058){
            return "戊寅";
        }
        return header.substring(5, 7);
    }

    private static String getAnimal(int year, String header){
        // 2058年没有生肖数据
        if (year == 2058){
            return "虎";
        }
        return year>=2011 ? header.substring(11, 12) : header.substring(9, 10);
    }

    private static String parseLunarDate(String line){
        if (null == line || line.length() < 19){
            System.out.println("unparsed line: " + line);
            return null;
        }
        return line.substring(15, 25).trim();
    }

    private static boolean isMonthEquals(String month1, String month2) {
        if ("閏十一月".equals(month1) || "閏冬月".equals(month1)){
            return "閏十一月".equals(month2) || "閏冬月".equals(month2);
        }
        if ("冬月".equals(month1) || "十一月".equals(month1)){
            return "冬月".equals(month2) || "十一月".equals(month2);
        }
        if ("閏腊月".equals(month1) || "閏十二月".equals(month1)){
            return "閏腊月".equals(month2) || "閏十二月".equals(month2);
        }
        if ("腊月".equals(month1) || "十二月".equals(month1)){
            return "腊月".equals(month2) || "十二月".equals(month2);
        }
        return month1.equals(month2);
    }

    private static boolean isAnimalEquals(String animal1, String animal2) {
        // 香港天文台数据，2018之后生效由犬改为狗
        if ("狗".equals(animal1) || "犬".equals(animal1)){
            return "狗".equals(animal2) || "犬".equals(animal2);
        }
        return animal1.equals(animal2);
    }
}