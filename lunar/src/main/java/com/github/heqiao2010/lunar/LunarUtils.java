package com.github.heqiao2010.lunar;

import java.util.Calendar;

public class LunarUtils {
    /**
     * 计算两个农历日期之差
     *
     * @param lc1   　农历１
     * @param lc2   　农历２
     * @param field 　计算的维度，比如按月,天等
     * @return 具体的差值
     */
    public static long luanrDiff(LunarCalendar lc1, LunarCalendar lc2, int field) {
        return solarDiff(lc1, lc2, field);
    }

    /**
     * 求两个公历日期之差，field可以为年月日，时分秒<br>
     * 一年按365天计算，一个月按30天计算<br>
     *
     * @param solar1 　历日期
     * @param solar2 　历日期
     * @param field  差值单位
     * @return 差值
     */
    public static long solarDiff(Calendar solar1, Calendar solar2, int field) {
        long t1 = solar1.getTimeInMillis();
        long t2 = solar2.getTimeInMillis();
        switch (field) {
            case Calendar.SECOND:
                return (long) Math.rint((t1 - t2) / 1000);
            case Calendar.MINUTE:
                return (long) Math.rint((t1 - t2) / (60 * 1000));
            case Calendar.HOUR:
                return (long) Math.rint((t1 - t2) / (3600 * 1000));
            case Calendar.DATE:
                return (long) Math.rint((t1 - t2) / (24 * 3600 * 1000));
            case Calendar.MONTH:
                return (long) Math.rint((t1 - t2) / (30 * 24 * 3600 * 1000));
            case Calendar.YEAR:
                return (long) Math.rint((t1 - t2) / (365 * 24 * 3600 * 1000));
            default:
                return -1;
        }
    }

    /**
     * 农历月的天数
     *
     * @param lunarYear   农历年
     * @param month       农历月
     * @param isLeapMonth 闰月
     * @return 天数，29或者30
     */
    public static long lengthOfMonth(int lunarYear, int month, boolean isLeapMonth) {
        short[] codes = LunarCodes.monthCodes(lunarYear);
        int i = isLeapMonth ? month + 1 : month;
        return LunarCodes.lengthOfMonth(lunarYear, month, codes[i]);
    }

    /**
     * 一个简单的二分查找，返回查找到的元素坐标，用于查找农历二维数组信息
     *
     * @param array 　数组
     * @param n     　待查询数字
     * @return 查到的坐标
     */
    public static int binSearch(int[] array, int n) {
        if (null == array || array.length == 0) {
            return -1;
        }
        int min = 0, max = array.length - 1;
        if (n <= array[min]) {
            return min;
        } else if (n >= array[max]) {
            return max;
        }
        while (max - min > 1) {
            int newIndex = (max + min) / 2; // 二分
            if (array[newIndex] > n) { // 取小区间
                max = newIndex;
            } else if (array[newIndex] < n) {// 取大区间
                min = newIndex;
            } else { // 相等，直接返回下标
                return newIndex;
            }
        }
        if (array[max] == n) {
            return max;
        } else if (array[min] == n) {
            return min;
        } else {
            return min; // 返回 较小的一个
        }
    }
}
