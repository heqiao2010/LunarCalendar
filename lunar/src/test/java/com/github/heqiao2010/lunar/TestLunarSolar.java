package com.github.heqiao2010.lunar;

import com.github.heqiao2010.lunar.LunarCalendar;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class TestLunarSolar {
    private static List<String> testCaseList;
    private static Map<String, Integer> monthMap;
    private static Map<String, Integer> dayMap;

    @Before
    public void setUp() throws IOException {
        monthMap = new HashMap<>();
        monthMap.put("正月", 1);
        monthMap.put("二月", 2);
        monthMap.put("三月", 3);
        monthMap.put("四月", 4);
        monthMap.put("五月", 5);
        monthMap.put("六月", 6);
        monthMap.put("七月", 7);
        monthMap.put("八月", 8);
        monthMap.put("九月", 9);
        monthMap.put("十月", 10);
        monthMap.put("十一月", 11);
        monthMap.put("十二月", 12);
        monthMap.put("閏正月", 1);
        monthMap.put("閏二月", 2);
        monthMap.put("閏三月", 3);
        monthMap.put("閏四月", 4);
        monthMap.put("閏五月", 5);
        monthMap.put("閏六月", 6);
        monthMap.put("閏七月", 7);
        monthMap.put("閏八月", 8);
        monthMap.put("閏九月", 9);
        monthMap.put("閏十月", 10);
        monthMap.put("閏十一月", 11);
        monthMap.put("閏十二月", 12);

        dayMap = new HashMap<>();
        dayMap.put("正月", 1);
        dayMap.put("二月", 1);
        dayMap.put("三月", 1);
        dayMap.put("四月", 1);
        dayMap.put("五月", 1);
        dayMap.put("六月", 1);
        dayMap.put("七月", 1);
        dayMap.put("八月", 1);
        dayMap.put("九月", 1);
        dayMap.put("十月", 1);
        dayMap.put("十一月", 1);
        dayMap.put("十二月", 1);
        dayMap.put("閏正月", 1);
        dayMap.put("閏二月", 1);
        dayMap.put("閏三月", 1);
        dayMap.put("閏四月", 1);
        dayMap.put("閏五月", 1);
        dayMap.put("閏六月", 1);
        dayMap.put("閏七月", 1);
        dayMap.put("閏八月", 1);
        dayMap.put("閏九月", 1);
        dayMap.put("閏十月", 1);
        dayMap.put("閏十一月", 1);
        dayMap.put("閏十二月", 1);
        dayMap.put("初二", 2);
        dayMap.put("初三", 3);
        dayMap.put("初四", 4);
        dayMap.put("初五", 5);
        dayMap.put("初六", 6);
        dayMap.put("初七", 7);
        dayMap.put("初八", 8);
        dayMap.put("初九", 9);
        dayMap.put("初十", 10);
        dayMap.put("十一", 11);
        dayMap.put("十二", 12);
        dayMap.put("十三", 13);
        dayMap.put("十四", 14);
        dayMap.put("十五", 15);
        dayMap.put("十六", 16);
        dayMap.put("十七", 17);
        dayMap.put("十八", 18);
        dayMap.put("十九", 19);
        dayMap.put("二十", 20);
        dayMap.put("廿一", 21);
        dayMap.put("廿二", 22);
        dayMap.put("廿三", 23);
        dayMap.put("廿四", 24);
        dayMap.put("廿五", 25);
        dayMap.put("廿六", 26);
        dayMap.put("廿七", 27);
        dayMap.put("廿八", 28);
        dayMap.put("廿九", 29);
        dayMap.put("三十", 30);

        testCaseList = new ArrayList<>();
        buildTestCase();
    }

    /**
     * 农历日期信息
     */
    public class LunarDateInfo {
        private int year;   // 农历年份
        private int month;  // 农历月份
        private int day;    // 农历day-of-month
        private int leap;   // 是否闰月

        public LunarDateInfo(int year, int month, int day, int leap) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.leap = leap;
        }

        @Override
        public String toString() {
            return year + "-" + month + "-" + day + "\t" + leap;
        }
    }

    /**
     * 校验阳历转农历
     * @param solar         阳历日期
     * @param lunar         农历日期
     * @param isLeapMonth   当前农历是否是闰月
     * @return              true-符合预期/false-不符合预期
     */
    private boolean validSolar2Lunar(String solar, String lunar, String isLeapMonth) {
        String[] solaritems = solar.trim().split("-");
        String[] lunaritems = lunar.trim().split("-");

        int syear = Integer.valueOf(solaritems[0]);
        int smonth = Integer.valueOf(solaritems[1]);
        int sday = Integer.valueOf(solaritems[2]);

        int lyear = Integer.valueOf(lunaritems[0]);
        int lmonth = Integer.valueOf(lunaritems[1]);
        int lday = Integer.valueOf(lunaritems[2]);

        boolean leap = StringUtils.equalsIgnoreCase(isLeapMonth, "1") ? true: false;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, syear);
        calendar.set(Calendar.MONTH, smonth - 1);
        calendar.set(Calendar.DATE, sday);

        LunarCalendar lunarDate = LunarCalendar.solar2Lunar(calendar);
        int year = lunarDate.getLunarYear();
        int month = lunarDate.getLunarMonth();
        int day = lunarDate.getDayOfLunarMonth();
        boolean leapMonth = lunarDate.isLeapMonth();

        return year == lyear && month == lmonth && day == lday && leap == leapMonth;
    }

    /**
     * 校验农历转阳历
     * @param solar         阳历日期
     * @param lunar         农历日期
     * @param isLeapMonth   当前农历是否是闰月
     * @return              true-符合预期/false-不符合预期
     */
    private boolean validLunar2Solar(String solar, String lunar, String isLeapMonth) {
        String[] solaritems = solar.trim().split("-");
        String[] lunaritems = lunar.trim().split("-");


        int syear = Integer.valueOf(solaritems[0]);
        int smonth = Integer.valueOf(solaritems[1]);
        int sday = Integer.valueOf(solaritems[2]);

        int lyear = Integer.valueOf(lunaritems[0]);
        int lmonth = Integer.valueOf(lunaritems[1]);
        int lday = Integer.valueOf(lunaritems[2]);

        boolean leap = StringUtils.equalsIgnoreCase(isLeapMonth, "1") ? true: false;

        Calendar calendar = LunarCalendar.lunar2Solar(lyear, lmonth, lday, leap);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year == syear && month == smonth && day == sday;
    }

    /**
     * 加载香港天文台每年的农历阳历对照表
     * @param fileName  文件路径
     * @param dateInfo  上一个文件农历最后一天
     * @return          当前文件最后一天的农历信息
     * @throws IOException
     */
    private LunarDateInfo loadPerFile(String fileName, LunarDateInfo dateInfo) throws IOException {
        File file = new File(fileName);
        InputStream is = this.getClass().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        boolean skip = true;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (StringUtils.isBlank(line)) continue; // 过滤空行
            if (line.startsWith("公曆日期")) skip = false; // 开始加载标识
            if (skip) continue; // 去除首行
            if (!(line.startsWith("1") || line.startsWith("2"))) continue; // 过滤非数据行

            String[] items = line.split(" ");
            List<String> itemList = new ArrayList<>();
            for (String item : items) {
                item = item.trim();
                if (StringUtils.isNotBlank(item)) {
                    itemList.add(item);
                }
            }

            dateInfo = parseMonthDay(dateInfo.year, dateInfo.month, itemList.get(1), dateInfo.leap);

            String solar = itemList.get(0).replaceAll("年|月", "-").replace("日", "");
            testCaseList.add(solar + "\t" + dateInfo.toString());
        }

        return dateInfo;
    }

    /**
     * 解析农历日期信息
     * @param year      年份
     * @param month     月份
     * @param day       day-of-month
     * @param leap      是否闰月
     * @return          LunarDateInfo
     */
    private LunarDateInfo parseMonthDay(int year, int month, String day, int leap) {
        int currDay = dayMap.get(day);
        int currYear = year;
        int currMonth = month;

        if (currDay == 1) {
            currMonth = monthMap.get(day);
            leap = day.startsWith("閏") ? 1 : 0;
        }

        if (currMonth == 1 && currDay == 1) currYear += 1;

        return new LunarDateInfo(currYear, currMonth, currDay, leap);
    }

    /**
     * 构造测试用例
     * @throws IOException
     */
    private void buildTestCase() throws IOException {
        LunarDateInfo dateInfo = new LunarDateInfo(1900, 11, 0, 0);

        for (int index = 1901; index < 2101; ++ index) {
            dateInfo = loadPerFile("/T" + index + "c.txt", dateInfo);
        }
    }

    @Test
    public void testSolar2Lunar() {
        for (String testCase : testCaseList) {
            String[] testItems = testCase.split("\t");
            String solar = testItems[0];
            String lunar = testItems[1];
            String leap = testItems[2];

            Assert.assertTrue(validSolar2Lunar(solar, lunar, leap));
        }
    }

    @Test
    public void testLunar2Solar() {
        for (String testCase : testCaseList) {
            String[] testItems = testCase.split("\t");
            String solar = testItems[0];
            String lunar = testItems[1];
            String leap = testItems[2];

            Assert.assertTrue(validLunar2Solar(solar, lunar, leap));
        }
    }

    @Test
    public void testCaseCount() {
        Assert.assertEquals(73049, testCaseList.size());
    }
}
