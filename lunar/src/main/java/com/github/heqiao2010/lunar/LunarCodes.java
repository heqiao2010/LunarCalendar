package com.github.heqiao2010.lunar;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static com.github.heqiao2010.lunar.LunarData.LUNAR_INFO;

/**
 * 农历/公历对照代码工具方法集合
 */
public class LunarCodes {

    /**
     * 农历年的代码数组
     * @param year 农历年
     * @return 代码数组
     */
    public static short[] monthCodes(int year) {
        return LUNAR_INFO[year - LunarData.MINI_YEAR];
    }

    /**
     * 从代码中获取年份，大于12时表示下一年
     * @param code 代码
     * @return 0/1 今年或者下一年
     */
    public static int codeYear(int code) {
        return code / 100 > 12 ? 1 : 0;
    }

    /**
     * 从代码中获取农历月初一在公历中的月份
     * @param code 代码
     * @return 公历月份
     */
    public static int codeMonth(int code) {
        int m = code / 100;
        if (m > 12) m -= 12;
        return m;
    }

    /**
     * 从代码中获取农历日在公历中的日 (day of month)
     * @param code 代码
     * @return 公历日
     */
    public static int codeDay(int code) {
        return code % 100;
    }

    /**
     * 求一个农历月的天数
     * @param lunarYear 农历年
     * @param month 农历月
     * @param code 农历月有日期，Mdd 表示
     * @return 月的天数
     */
    public static long lengthOfMonth(int lunarYear, int month, short code) {
        short md2;
        short[] starts = monthCodes(lunarYear);
        int y2 = lunarYear;
        if (month + 1 < starts.length && starts[month] == code) {
            md2 = starts[month + 1];
        } else if (month + 2 < starts.length && starts[month + 1] == code) {
            md2 = starts[month + 2];
        } else if (lunarYear - LunarData.MINI_YEAR + 1 < LUNAR_INFO.length) {
            md2 = monthCodes(lunarYear + 1)[1];
            y2 ++;
        } else {
            throw new IllegalArgumentException("lunar date out of range");
        }

        int y1 = lunarYear + codeYear(code);
        int m1 = codeMonth(code);
        int d1 = codeDay(code);

        y2 += codeYear(md2);
        int m2 = codeMonth(md2);
        int d2 = codeDay(md2);

        Calendar c1 = Calendar.getInstance();
        c1.set(y1, m1 - 1, d1);
        Calendar c2 = Calendar.getInstance();
        c2.set(y2, m2 - 1, d2);

        return LunarUtils.solarDiff(c2, c1, Calendar.DATE);
    }

    /**
     * 根据农历年和 LUNAR_INFO 中的下标来确定月份和闰月
     *
     * @param year  农历年
     * @param index LUNAR_INFO 月份数组中的下标
     * @return 月, 闰月
     */
    public static Map.Entry<Integer, Boolean> month(int year, int index) {
        short[] a = monthCodes(year);
        int i = index;
        if (index == -1) {
            i = a.length - 1;
        }
        boolean isLeap = a[0] > 0 && a[0] + 1 == i;
        int month = isLeap || a[0] > 0 && a[0] < i ? i - 1 : i;
        return new AbstractMap.SimpleImmutableEntry(month, isLeap);
    }

    /**
     * 计算月份 Mdd 代码在数组中的位置
     *
     * @param year        农历年
     * @param month       农历月
     * @param isLeapMonth 闰月
     * @return 月所在的下标
     */
    public static int monthIndex(int year, int month, boolean isLeapMonth) {
        short[] a = monthCodes(year);
        if (a[0] > 0 && a[0] < month || a[0] == month && isLeapMonth) {
            return month + 1;
        }
        return month;
    }

    /**
     * 判断两个整数所代表公历日期的差值<br>
     * 一年按365天计算，一个月按30天计算<br>
     *
     * @param solarCode1 　农历日期代码
     * @param solarCode2 　农历日期代码
     * @param field      　差值单位
     * @return 差值
     */
    public static long solarDateCodesDiff(int solarCode1, int solarCode2, int field) {
        GregorianCalendar c1 = new GregorianCalendar(solarCode1 / 10000, solarCode1 % 10000 / 100 - 1,
                solarCode1 % 10000 % 100);
        GregorianCalendar c2 = new GregorianCalendar(solarCode2 / 10000, solarCode2 % 10000 / 100 - 1,
                solarCode2 % 10000 % 100);
        return LunarUtils.solarDiff(c1, c2, field);
    }

    /**
     * 农历月的代码
     * @param lunarYear 农历月
     * @param lunarMonth 农历月
     * @param isLeapMonth 闰月
     * @return 代码
     */
    public static short lunarMonthCode(int lunarYear, int lunarMonth, boolean isLeapMonth) {
        short[] codes = monthCodes(lunarYear);
        int index = lunarMonth;
        if (codes[0] > 0 && codes[0] < lunarMonth || codes[0] == lunarMonth && isLeapMonth) {
            index++;
        }
        return codes[index];
    }
}
