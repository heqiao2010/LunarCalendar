package com.github.heqiao2010.lunar;

import java.util.*;

/**
 * 中国农历
 * Created by joel on 2019/3/31.
 *
 * @author joel
 */
public class LunarCalendar extends GregorianCalendar {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7241031233810655166L;

    // ------------------------ 农历相关成员变量 --------------------------------

    // 农历年，和公历可能不一样
    private int lunarYear;
    // 农历月(范围1-12和公历不一样)
    private int lunarMonth;
    // 农历日期
    private int dayOfLunarMonth;
    // 是否为闰月日期
    private boolean isLeapMonth = false;
    // 农历这年闰月，如果不闰月，默认为0
    private int leapMonth = 0;

    // ------------------------ 构造方法 --------------------------------

    public LunarCalendar() {
        super();
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    public LunarCalendar(TimeZone zone) {
        super(zone);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    public LunarCalendar(Locale aLocale) {
        super(aLocale);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    public LunarCalendar(TimeZone zone, Locale aLocale) {
        super(zone, aLocale);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    /**
     * 通过公历年、月、日构造
     * @param year 公历年
     * @param month 公历月
     * @param dayOfMonth 公历日
     */
    public LunarCalendar(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    public LunarCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        super(year, month, dayOfMonth, hourOfDay, minute);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    public LunarCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    /**
     * 通过农历年、月、日构造
     *
     * @param lunarYear       农历年
     * @param lunarMonth      农历月份,范围1-12
     * @param dayOfLunarMonth       农历日
     * @param isLeapMonth 是否闰月
     */
    public LunarCalendar(int lunarYear, int lunarMonth, int dayOfLunarMonth, boolean isLeapMonth) {
        computeByLunarDate(lunarYear, lunarMonth, dayOfLunarMonth, isLeapMonth);
    }

    /**
     * 通过公历构造
     *
     * @param calendar 　公历日期
     */
    public LunarCalendar(Calendar calendar) {
        super(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    // ------------------------ 静态方法 --------------------------------

    /**
     * 公历转农历
     *
     * @param solar 　公历日期
     * @return 农历日期
     */
    public static LunarCalendar solar2Lunar(Calendar solar) {
        return new LunarCalendar(solar);
    }

    /**
     * 农历转公历
     *
     * @param lunarYear   　农历年
     * @param lunarMonth  　农历月，从１开始
     * @param LunarDate   　农历日
     * @param isLeapMonth 　是否润月
     * @return 公历日期
     */
    public static Calendar lunar2Solar(int lunarYear, int lunarMonth, int LunarDate, boolean isLeapMonth) {
        LunarCalendar ret = new LunarCalendar();
        ret.computeByLunarDate(lunarYear, lunarMonth, LunarDate, isLeapMonth);
        return ret;
    }

    // ------------------------ 成员方法 --------------------------------

    /**
     * 公历上的操作，加减均是公历上的“一年”，“一个月”
     * @param field {@link java.util.Calendar} YEAR/MONTH/DATE
     * @param amount 数量，可正可负
     */
    @Override
    public void add(int field, int amount) {
        super.add(field, amount);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    /**
     * 农历上的年月日，加减均是农历上的“一年”，“一个月”
     * @param field {@link java.util.Calendar} YEAR/MONTH/DATE
     * @param amount 数量，可正可负
     */
    public void addByLunar(int field, int amount) {
        switch (field) {
            case Calendar.DATE:
                super.add(Calendar.DATE, amount);
                computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
                break;
            case Calendar.MONTH:
                addLunarMonths(amount);
                break;
            case Calendar.YEAR:
                // 增加一个农历年，保持月/日不变，清空闰月状态
                checkComputeLunarDate(lunarYear + amount, lunarMonth, dayOfLunarMonth, false);
                break;
            default:
                throw new IllegalArgumentException(String.format("unsupported field: %d", field));
        }
    }

    /**
     * 计算月份加减
     *
     * @param amount 数量
     */
    public void addLunarMonths(int amount) {
        int y = lunarYear;
        int m = -1;
        int index = LunarCodes.monthIndex(y, lunarMonth, isLeapMonth);
        boolean isLeapMonth = false;
        int sum = index + amount;
        if (amount > 0) {
            for (int _y = lunarYear; _y < LunarData.MAX_YEAR; _y++) {
                final short[] a = LunarCodes.monthCodes(_y);
                int lunarMonths = a.length - 1;
                sum -= lunarMonths;
                if (sum > 0) {
                    y++;
                }
                if (sum <= 0) {
                    if (sum == 0) m = lunarMonths;
                    else m = lunarMonths + sum;
                    isLeapMonth = a[0] > 0 && a[0] + 1 == m;
                    if (isLeapMonth || a[0] > 0 && a[0] < m) {
                        m--;
                    }
                    break;
                }
            }
            if (sum > 0) {
                throw new IllegalArgumentException(String.format("add of month out of range: %d", amount));
            }
        } else if (amount < 0) {
            if (sum > 0) {
                m = sum;
            } else if (sum == 0) {
                Map.Entry<Integer, Boolean> en = LunarCodes.month(--y, -1);
                m = en.getKey();
                isLeapMonth = en.getValue();
            } else {
                for (int i = lunarYear - 1; i > LunarData.MINI_YEAR; i--) {
                    int lunarMonths = LunarCodes.monthCodes(i).length - 1;
                    sum += lunarMonths;
                    y--;
                    if (sum >= 0) {
                        Map.Entry<Integer, Boolean> en;
                        if (sum == 0) {
                            en = LunarCodes.month(--y, -1);
                        } else {
                            en = LunarCodes.month(y, sum + 1);
                        }
                        m = en.getKey();
                        isLeapMonth = en.getValue();
                        break;
                    }
                }
            }
            if (sum < 0) {
                throw new IllegalArgumentException(String.format("add of month out of range: %d", amount));
            }
        }
        checkComputeLunarDate(y, m, dayOfLunarMonth, isLeapMonth);
    }

    /**
     * 校验 day of month 是否是合法的，如果越限则从30号减到29号
     *
     * @param y      lunar year
     * @param m      lunar month
     * @param d      lunar day of month
     * @param isLeap 闰月
     */
    private void checkComputeLunarDate(int y, int m, int d, boolean isLeap) {
        int days = d;
        if (d > 29 && d > LunarUtils.lengthOfMonth(y, m, isLeap)) {
            days--;
        }
        computeByLunarDate(y, m, days, isLeap);
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    @Override
    public void roll(int field, int amount) {
        super.roll(field, amount);
        computeBySolarDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE));
    }

    @Override
    public String toString() {
        if (this.lunarYear < LunarData.MINI_YEAR || this.lunarYear > LunarData.MAX_YEAR || this.lunarMonth < 1
                || this.lunarMonth > 12 || this.dayOfLunarMonth < 1
                || this.dayOfLunarMonth > 30) {
            return String.format("Wrong lunar date: %d %d %d", lunarYear, lunarMonth, dayOfLunarMonth);
        }
        return String.format("%s年%s%s月%s", LunarData.getYearName(this.lunarYear), this.isLeapMonth() ? "闰" : "",
                LunarData.getMonthName(this.lunarMonth), LunarData.getDayName(this.dayOfLunarMonth));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LunarCalendar)) return false;
        if (!super.equals(o)) return false;
        LunarCalendar that = (LunarCalendar) o;
        return lunarYear == that.lunarYear &&
                lunarMonth == that.lunarMonth &&
                dayOfLunarMonth == that.dayOfLunarMonth &&
                isLeapMonth == that.isLeapMonth &&
                leapMonth == that.leapMonth;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + lunarYear;
        result = 31 * result + lunarMonth;
        result = 31 * result + dayOfLunarMonth;
        result = 31 * result + leapMonth;
        result = 31 * result + (isLeapMonth ? 1 : 0);
        return result;
    }

    @Override
    public Object clone() {
        LunarCalendar other = (LunarCalendar) super.clone();
        other.lunarYear = getLunarYear();
        other.lunarMonth = getLunarMonth();
        other.dayOfLunarMonth = getDayOfLunarMonth();
        other.leapMonth = getLeapMonth();
        other.isLeapMonth = isLeapMonth();
        return other;
    }

    /**
     * 返回农历日期，不包含年份
     *
     * @param showLeap 　是否显示闰月的闰字
     * @return 农历日期
     */
    public String getLunar(boolean showLeap) {
        if (this.lunarMonth < 1 || this.lunarMonth > 12 || this.dayOfLunarMonth < 1
                || this.dayOfLunarMonth > 30) {
            throw new IllegalArgumentException(String.format("Wrong lunar dayOfLunarMonth: %d %d", lunarMonth, dayOfLunarMonth));
        }
        if (showLeap) {
            return String.format("%s%s月%s", this.isLeapMonth() ? "闰" : "", LunarData.getMonthName(this.lunarMonth),
                    LunarData.getDayName(this.dayOfLunarMonth));
        } else {
            return String.format("%s月%s", LunarData.getMonthName(this.lunarMonth),
                    LunarData.getDayName(this.dayOfLunarMonth));
        }
    }

    /**
     * 返回中国农历的全名
     *
     * @return String
     */
    public String getFullLunarName() {
        return String.format("%s %s %s", this, LunarData.getTraditionalYearName(this.lunarYear),
                LunarData.getAnimalYearName(this.lunarYear));
    }

    /**
     * 创建LunarInfo中某一年的一列公历日历编码<br>
     * 公历日历编码：年份+月份+天，用于查询某个公历日期在某个LunarInfo列的哪一个区间<br>
     *
     * @param solarYear 年份
     * @return 公历日历编码
     */
    private int[] builderSolarCodes(int solarYear) {
        if (solarYear < LunarData.MINI_YEAR || solarYear > LunarData.MAX_YEAR) {
            throw new IllegalArgumentException("Illegal solar year: " + solarYear);
        }
        int lunarIndex = solarYear - LunarData.MINI_YEAR;
        int[] solarCodes = new int[LunarData.LUNAR_INFO[lunarIndex].length];
        for (int i = 0; i < solarCodes.length; i++) {
            if (0 == i) { // 第一个数表示闰月，不用更改
                solarCodes[i] = LunarData.LUNAR_INFO[lunarIndex][i];
            } else if (1 == i) {
                if (LunarData.LUNAR_INFO[lunarIndex][1] > 999) {
                    // 这年农历一月一日对应的公历实际是上一年的
                    solarCodes[i] = (solarYear - 1) * 10000 + LunarData.LUNAR_INFO[lunarIndex][i];
                } else {
                    solarCodes[i] = solarYear * 10000 + LunarData.LUNAR_INFO[lunarIndex][i];
                }
            } else {
                solarCodes[i] = solarYear * 10000 + LunarData.LUNAR_INFO[lunarIndex][i];
            }
        }
        return solarCodes;
    }

    /**
     * 通过给定的农历日期，计算公历日期
     *
     * @param lunarYear   　农历年
     * @param lunarMonth  　农历月，从１开始
     * @param lunarDate   　农历日期
     * @param isLeapMonth 　是否为闰月
     */
    private void computeByLunarDate(final int lunarYear, final int lunarMonth, final int lunarDate,
                                    final boolean isLeapMonth) {
        if (lunarYear < LunarData.MINI_YEAR || lunarYear > LunarData.MAX_YEAR) {
            throw new IllegalArgumentException(String.format("LunarYear must in (%d, %d)", LunarData.MINI_YEAR,
                    LunarData.MAX_YEAR));
        }
        this.lunarYear = lunarYear;
        this.lunarMonth = lunarMonth;
        this.dayOfLunarMonth = lunarDate;
        this.isLeapMonth = isLeapMonth;
        short code = LunarCodes.lunarMonthCode(lunarYear, lunarMonth, isLeapMonth);

        // 对设置的day of month 进行检查
        if (lunarDate == 30) {
            long length = LunarCodes.lengthOfMonth(lunarYear, lunarMonth, code);
            if (length != 30) {
                throw new IllegalArgumentException(String.format("农历%d年%d月, 闰月=%s，月天数为%d < %d", lunarYear,
                        lunarMonth, isLeapMonth, length, lunarDate));
            }
        }

        super.set(Calendar.YEAR, lunarYear + LunarCodes.codeYear(code));
        super.set(Calendar.MONTH, LunarCodes.codeMonth(code) - 1);
        super.set(Calendar.DATE, LunarCodes.codeDay(code));
        super.add(Calendar.DATE, lunarDate - 1);
    }

    /**
     * 通过给定公历日期，计算农历日期各个域值
     * <br>
     *     这个方法可能会被调用多次，后续看能否再做优化
     * </br>
     *
     * @param solarYear  公历年
     * @param solarMonth 公历月，0-11
     * @param solarDate  公历日
     */
    private void computeBySolarDate(final int solarYear, final int solarMonth, final int solarDate) {
        if (solarYear < LunarData.MINI_YEAR
                || (solarYear == LunarData.MINI_YEAR && solarMonth < LunarData.MINI_MONTH)
                || (solarYear == LunarData.MINI_YEAR && solarMonth == LunarData.MINI_MONTH && solarDate < LunarData.MINI_DATE)
                || solarYear > LunarData.MAX_YEAR
                || (solarYear == LunarData.MAX_YEAR && solarMonth > LunarData.MAX_MONTH)
                || (solarYear == LunarData.MAX_YEAR && solarMonth == LunarData.MAX_MONTH && solarDate > LunarData.MAX_DATE)
        ) {
            // 有些中间过程日期会超出可计算范围
            // throw new IllegalArgumentException("Illegal solar year: " + solarYear);
            return;
        }
        int solarCode = solarYear * 10000 + 100 * (1 + solarMonth) + solarDate; // 公历码
        leapMonth = LunarData.LUNAR_INFO[solarYear - LunarData.MINI_YEAR][0];
        int[] solarCodes = builderSolarCodes(solarYear);
        int newMonth = LunarUtils.binSearch(solarCodes, solarCode);
        if (-1 == newMonth) {
            throw new IllegalArgumentException("No lunarInfo found by solarCode: " + solarCode);
        }
        int xDate = Long.valueOf(LunarCodes.solarDateCodesDiff(solarCode, solarCodes[newMonth], Calendar.DATE)).intValue();
        if (0 == newMonth) {// 在上一年
            int preYear = solarYear - 1;
            short[] preSolarCodes = LunarData.LUNAR_INFO[preYear - LunarData.MINI_YEAR];
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
            xDate = Long.valueOf(LunarCodes.solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
            if (xDate < 0) {
                throw new IllegalArgumentException("Wrong solarCode: " + solarCode);
            }
            this.dayOfLunarMonth = 1 + xDate;
            this.lunarYear = preYear;
            this.lunarMonth = newMonth;
            this.isLeapMonth = false; // 农历12月不可能为闰月
        } else if (solarCodes.length == newMonth + 1 && xDate >= 30) {// 在下一年(公历12月只有30天)
            newMonth = 1; // 农历肯定是1月
            // 取下一年的公历日期码
            short[] nextSolarCodes = LunarData.LUNAR_INFO[solarYear + 1 - LunarData.MINI_YEAR];
            // 取下一年农历1月1号公历日期码
            int nearSolarCode = solarYear * 10000 + nextSolarCodes[1]; // 下一年农历1月1号公历日期码
            xDate = Long.valueOf(LunarCodes.solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
            if (xDate < 0) {
                throw new IllegalArgumentException("Wrong solarCode: " + solarCode);
            }
            this.dayOfLunarMonth = 1 + xDate;
            this.lunarYear = solarYear + 1; // 农历年到了下一年
            this.lunarMonth = newMonth;
            this.isLeapMonth = false; // 农历1月不可能为闰月
        } else {
            if (xDate < 0) {
                throw new IllegalArgumentException("Wrong solarCode: " + solarCode);
            }
            this.dayOfLunarMonth = 1 + xDate;
            this.lunarYear = solarYear;
            this.isLeapMonth = 0 != leapMonth && (leapMonth + 1 == newMonth);
            if (0 != leapMonth && leapMonth < newMonth) {
                this.lunarMonth = newMonth - 1;
            } else {
                this.lunarMonth = newMonth;
            }
        }
    }

    // ------------------------ getter and setter --------------------------------

    public int getLunarYear() {
        return lunarYear;
    }

    public int getLunarMonth() {
        return lunarMonth;
    }

    public int getDayOfLunarMonth() {
        return dayOfLunarMonth;
    }

    public int getLeapMonth() {
        return leapMonth;
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }
}
