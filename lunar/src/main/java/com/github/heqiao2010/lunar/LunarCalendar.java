package com.github.heqiao2010.lunar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 中国农历
 * Created by heqiao on 2019/3/31.
 * @author joel
 */
public final class LunarCalendar extends LunarData implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7241031233810655166L;

    // 农历年，和公历是一样的
    private int lyear;
    // 农历月，范围1-12
    private int lmonth;
    // 农历日期
    private int ldate;
    // 是否为闰月日期
    private boolean isLeapMonth = false;
    // 农历这年闰月，如果不闰月，默认为0
    private int leapMonth = 0;
    // 公历日期
    private GregorianCalendar solar = new GregorianCalendar();

    public LunarCalendar() {
        computeBySolarDate(solar.get(Calendar.YEAR), solar.get(Calendar.MONTH), solar.get(Calendar.DATE));
    }

    /**
     * 通过农历年、月、日构造
     *
     * @param lyear       农历年
     * @param lmonth      农历月份,范围1-12
     * @param ldate       农历日
     * @param isleapMonth 是否闰月
     */
    public LunarCalendar(int lyear, int lmonth, int ldate, boolean isleapMonth) {
        computeByLunarDate(lyear, lmonth, ldate, isleapMonth);
    }

    /**
     * 通过公历构造
     *
     * @param calendar 公历日期
     */
    public LunarCalendar(Calendar calendar) {
        computeBySolarDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
    }

    /**
     * 创建LunarInfo中某一年的一列公历日历编码<br>
     * 公历日历编码：年份+月份+天，用于查询某个公历日期在某个LunarInfo列的哪一个区间<br>
     *
     * @return int[]
     */
    private int[] builderSolarCodes(int solarYear) {
        if (solarYear < MINIYEAR && solarYear > MAXYEAR) {
            throw new LunarException("Illegal solar year: " + solarYear);
        }
        int lunarIndex = solarYear - MINIYEAR;
        int solarCodes[] = new int[LuarInfo[lunarIndex].length];
        for (int i = 0; i < solarCodes.length; i++) {
            if (0 == i) { // 第一个数表示闰月，不用更改
                solarCodes[i] = LuarInfo[lunarIndex][i];
            } else if (1 == i) {
                if (LuarInfo[lunarIndex][1] > 999) {
                    // 这年农历一月一日对应的公历实际是上一年的
                    solarCodes[i] = (solarYear - 1) * 10000 + LuarInfo[lunarIndex][i];
                } else {
                    solarCodes[i] = solarYear * 10000 + LuarInfo[lunarIndex][i];
                }
            } else {
                solarCodes[i] = solarYear * 10000 + LuarInfo[lunarIndex][i];
            }
        }
        return solarCodes;
    }

    /**
     * 日期增加
     *
     * @param field
     * @param amount
     */
    public void add(int field, int amount) {
        this.getSolar().add(field, amount);
        this.computeBySolarDate(this.getSolar().get(Calendar.YEAR), this.getSolar().get(Calendar.MONTH),
                this.getSolar().get(Calendar.DATE));
    }

    /**
     * 通过给定的农历日期，计算公历日期
     *
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDate
     * @param isleapMonth
     * @return boolean
     */
    private void computeByLunarDate(final int lunarYear, final int lunarMonth, final int lunarDate, final boolean isleapMonth) {
        if (lunarYear < MINIYEAR && lunarYear > MAXYEAR) {
            throw new LunarException("LunarYear must in (" + MINIYEAR + "," + MAXYEAR + ")");
        }
        this.lyear = lunarYear;
        this.lmonth = lunarMonth;
        this.ldate = lunarDate;
        int solarMontDate = LuarInfo[lunarYear - MINIYEAR][lunarMonth];
        leapMonth = LuarInfo[lunarYear - MINIYEAR][0];
        if (leapMonth != 0 && (lunarMonth > leapMonth || (lunarMonth == leapMonth && isleapMonth))) {
            // 闰月，且当前农历月大于闰月月份，取下一个月的LunarInfo码
            // 闰月，且当前农历月等于闰月月份，并且此农历月为闰月，取下一个月的LunarInfo码
            solarMontDate = LuarInfo[lunarYear - MINIYEAR][lunarMonth + 1];
        }
        this.getSolar().set(Calendar.YEAR, lunarYear);
        this.getSolar().set(Calendar.MONTH, (solarMontDate / 100) - 1);
        this.getSolar().set(Calendar.DATE, solarMontDate % 100);
        this.add(Calendar.DATE, lunarDate - 1);
    }

    /**
     * 通过给定公历日期，计算农历日期
     *
     * @param solarYear 公历年
     * @param solarMonth 公历月，0-11
     * @param solarDate 公历日
     * @return void
     */
    private void computeBySolarDate(final int solarYear, final int solarMonth, final int solarDate) {
        if (solarYear < MINIYEAR && solarYear > MAXYEAR) {
            throw new LunarException("Illegal solar year: " + solarYear);
        }
        int solarCode = solarYear * 10000 + 100 * (1 + solarMonth) + solarDate; // 公历码
        leapMonth = LuarInfo[solarYear - MINIYEAR][0];
        int solarCodes[] = builderSolarCodes(solarYear);
        int newMonth = binSearch(solarCodes, solarCode);
        if (-1 == newMonth){
            throw new LunarException("No lunarInfo found by solarCode: " + solarCode);
        }
        int xdate = Long.valueOf(solarDateCodesDiff(solarCode, solarCodes[newMonth], Calendar.DATE)).intValue();
        if (0 == newMonth) {// 在上一年
            int preYear = solarYear - 1;
            int[] preSolarCodes = LuarInfo[preYear - MINIYEAR];
            // 取上年农历12月1号公历日期码
            int nearSolarCode = preSolarCodes[preSolarCodes.length - 1]; // 上一年12月1号
            // 下一年公历1月表示为了13月，这里做翻译，并计算出日期码
            nearSolarCode = (nearSolarCode / 100 == 13 ? preYear + 1 : preYear) * 10000
                    + (nearSolarCode / 100 == 13 ? nearSolarCode - 1200 : nearSolarCode);
            if (nearSolarCode > solarCode) {// 此公历日期在上一年农历12月1号，之前，即在上年农历11月内
                newMonth = 11;
                // 取农历11月的公历码
                nearSolarCode = preYear * 10000 + preSolarCodes[preSolarCodes.length - 2];
            } else {// 此公历日期在上一年农历12月内
                newMonth = 12;
            }
            xdate = Long.valueOf(solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
            if (xdate < 0) {
                throw new LunarException("Wrong solarCode: " + solarCode);
            }
            this.ldate = 1 + xdate;
            this.lyear = preYear;
            this.lmonth = newMonth;
            this.isLeapMonth = false; // 农历12月不可能为闰月
        } else if (solarCodes.length == newMonth + 1 && xdate >= 30) {// 在下一年(公历12月只有30天)
            newMonth = 1; // 农历肯定是1月
            // 取下一年的公历日期码
            int[] nextSolarCodes = LuarInfo[solarYear + 1 - MINIYEAR];
            // 取下一年农历1月1号公历日期码
            int nearSolarCode = solarYear * 10000 + nextSolarCodes[1]; // 下一年农历1月1号公历日期码
            xdate = Long.valueOf(solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
            if (xdate < 0) {
                throw new LunarException("Wrong solarCode: " + solarCode);
            }
            this.ldate = 1 + xdate;
            this.lyear = solarYear + 1; // 农历年到了下一年
            this.lmonth = newMonth;
            this.isLeapMonth = false; // 农历1月不可能为闰月
        } else {
            if (xdate < 0) {
                throw new LunarException("Wrong solarCode: " + solarCode);
            }
            this.ldate = 1 + xdate;
            this.lyear = solarYear;
            this.isLeapMonth = 0 != leapMonth && (leapMonth + 1 == newMonth);
            if (0 != leapMonth && leapMonth < newMonth) {
                this.lmonth = newMonth - 1;
            } else {
                this.lmonth = newMonth;
            }
        }
        this.getSolar().set(Calendar.YEAR, solarYear);
        this.getSolar().set(Calendar.MONTH, solarMonth);
        this.getSolar().set(Calendar.DATE, solarDate);
    }

    /**
     * 计算两个农历日期之差
     *
     * @param lc1
     * @param lc2
     * @param field
     */
    public static long luanrDiff(LunarCalendar lc1, LunarCalendar lc2, int field) {
        return solarDiff(lc1.getSolar(), lc2.getSolar(), field);
    }

    /**
     * 公历转农历
     *
     * @param solar
     * @return LunarCalendar
     */
    public static LunarCalendar solar2Lunar(Calendar solar) {
        LunarCalendar ret = new LunarCalendar();
        ret.computeBySolarDate(solar.get(Calendar.YEAR), solar.get(Calendar.MONTH), solar.get(Calendar.DATE));
        return ret;
    }

    /**
     * 农历转公历
     *
     * @param lunarYear
     * @param lunarMonth
     * @param LunarDate
     * @param isLeapMonth
     * @return Calendar
     */
    public static Calendar lunar2Solar(int lunarYear, int lunarMonth, int LunarDate, boolean isLeapMonth) {
        LunarCalendar ret = new LunarCalendar();
        ret.computeByLunarDate(lunarYear, lunarMonth, LunarDate, isLeapMonth);
        return ret.getSolar();
    }

    /**
     * @param field
     * @param n
     */
    public void solarAdd(int field, int n) {
        getSolar().add(field, n);
        computeBySolarDate(getSolar().get(Calendar.YEAR), getSolar().get(Calendar.MONTH), getSolar().get(Calendar.DATE));
    }

    public String getLunar(boolean showLeap) {
        if (this.lmonth < 1 || this.lmonth > 12 || this.ldate < 1
                || this.ldate > 30) {
            throw new LunarException("Wrong lunar ldate: " + lmonth + " " + ldate);
        }
        if(showLeap){
            return (this.isLeapMonth() ? "闰" : "") + this.getMonthName(this.lmonth) + "月"
                    + this.getDayName(this.ldate);
        } else {
            return this.getMonthName(this.lmonth) + "月" + this.getDayName(this.ldate);
        }
    }

    /**
     * 获取天
     *
     * @return String
     */
    public static String getDayName(int lunarDay) {
        return LunarDayName[lunarDay - 1];
    }

    /**
     * 获取农历月份
     *
     * @param lunarMonth
     * @return String
     */
    public static String getMonthName(int lunarMonth) {
        return LunarMonthName[lunarMonth - 1];
    }

    /**
     * 获取年
     */
    public static String getYearName(int lunarYear) {
        StringBuilder sb = new StringBuilder();
        sb.append(LunarYearName[lunarYear / 1000]);
        sb.append(LunarYearName[lunarYear % 1000 / 100]);
        sb.append(LunarYearName[lunarYear % 100 / 10]);
        sb.append(LunarYearName[lunarYear % 10]);
        return sb.toString();
    }

    /**
     * 判断两个整数所代表公历日期的差值<br>
     * 一年按365天计算，一个月按30天计算<br>
     *
     * @param solarCode1
     * @param solarCode2
     * @return long
     */
    public static long solarDateCodesDiff(int solarCode1, int solarCode2, int field) {
        GregorianCalendar c1 = new GregorianCalendar(solarCode1 / 10000, solarCode1 % 10000 / 100 - 1,
                solarCode1 % 10000 % 100);
        GregorianCalendar c2 = new GregorianCalendar(solarCode2 / 10000, solarCode2 % 10000 / 100 - 1,
                solarCode2 % 10000 % 100);
        return solarDiff(c1, c2, field);
    }

    /**
     * 求两个公历日期之差，field可以为年月日，时分秒<br>
     * 一年按365天计算，一个月按30天计算<br>
     *
     * @param solar1
     * @param solar2
     * @param field
     * @return long
     */
    public static long solarDiff(Calendar solar1, Calendar solar2, int field) {
        long t1 = solar1.getTimeInMillis();
        long t2 = solar2.getTimeInMillis();
        switch (field) {
            case Calendar.SECOND:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(1000));
            case Calendar.MINUTE:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(60 * 1000));
            case Calendar.HOUR:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(3600 * 1000));
            case Calendar.DATE:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(24 * 3600 * 1000));
            case Calendar.MONTH:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(30 * 24 * 3600 * 1000));
            case Calendar.YEAR:
                return (long) Math.rint(Double.valueOf(t1 - t2) / Double.valueOf(365 * 24 * 3600 * 1000));
            default:
                return -1;
        }
    }

    // getter and setter
    public int getLyear() {
        return lyear;
    }

    public void setLyear(int lyear) {
        this.lyear = lyear;
    }

    public int getLmonth() {
        return lmonth;
    }

    public void setLmonth(int lmonth) {
        this.lmonth = lmonth;
    }

    public int getLdate() {
        return ldate;
    }

    public void setLdate(int ldate) {
        this.ldate = ldate;
    }

    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public GregorianCalendar getSolar() {
        return solar;
    }

    public void setSolar(GregorianCalendar solar) {
        this.solar = solar;
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(boolean isLeapMonth) {
        this.isLeapMonth = isLeapMonth;
    }

    /**
     * 一个简单的二分查找，返回查找到的元素坐标，用于查找农历二维数组信息
     *
     * @param array 数组
     * @param n 目标
     * @return int 找到的值，未找到返回-1
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

    @Override
    public String toString() {
        if (this.lyear < MINIYEAR || this.lyear > MAXYEAR || this.lmonth < 1 || this.lmonth > 12 || this.ldate < 1
                || this.ldate > 30) {
            throw new LunarException("Wrong lunar ldate: " + lyear + " " + lmonth + " " + ldate);
        }
        return this.getYearName(this.lyear) + "年" + (this.isLeapMonth() ? "闰" : "") + this.getMonthName(this.lmonth) + "月"
                + this.getDayName(this.ldate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LunarCalendar that = (LunarCalendar) o;

        if (lyear != that.lyear) return false;
        if (lmonth != that.lmonth) return false;
        if (ldate != that.ldate) return false;
        return isLeapMonth == that.isLeapMonth;
    }

    @Override
    public int hashCode() {
        int result = lyear;
        result = 31 * result + lmonth;
        result = 31 * result + ldate;
        result = 31 * result + (isLeapMonth ? 1 : 0);
        return result;
    }

    /**
     * 返回传统天干地支年名称
     * @param y 注意这里是农历年
     *
     * @return String
     */
    public static String getTraditionalYearName(int y) {
        y = y - MINIYEAR + 36;
        return ("" + LunarGan[y % 10] + LunarZhi[y % 12] + "年");
    }

    /**
     * 获取生肖名
     *
     * @return char
     */
    public static char getAnimalYearName(int y) {
        return LunarAnimailName[(y - 4) % 12];
    }

    /**
     * 返回中国农历的全名
     *
     * @return String
     */
    public String getFullLunarName() {
        return this.toString() + " " + getTraditionalYearName(this.lyear) + " " + getAnimalYearName(this.lyear);
    }

    /**
     * 农历日期异常
     *
     * @author joel
     */
    public static class LunarException extends RuntimeException {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 3274596943314243191L;

        private String message;

        // constructor
        public LunarException(String message) {
            super(message);
            this.message = message;
        }

        public LunarException() {
            super();
        }

        public LunarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, writableStackTrace, writableStackTrace);
        }

        public LunarException(String message, Throwable t) {
            super(message, t);
            this.message = message;
        }

        public LunarException(Throwable t) {
            super(t);
        }

        // getter and setter
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}

