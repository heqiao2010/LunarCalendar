package com.github.heqiao2010.lunar;

import java.util.*;
import static com.github.heqiao2010.lunar.LunarData.*;

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

    /**
     * 获取农历日的表示
     *
     * @param lunarDay 　农历日数值表示
     * @return 农历日传统字符表示
     */
    public static String getDayName(int lunarDay) {
        return LunarDayName[lunarDay - 1];
    }

    /**
     * 获取农历月份
     *
     * @param lunarMonth 　农历月数值表示
     * @return 农历月传统字符表示
     */
    public static char getMonthName(int lunarMonth) {
        return LunarMonthName[lunarMonth - 1];
    }

    /**
     * 获取农历年份
     *
     * @param lunarYear 　农历年数值表示
     * @return 农历年传统字符表示
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
        return solarDiff(c1, c2, field);
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
     * 返回传统天干地支年名称
     *
     * @param y 农历年
     * @return 传统农历年份的表示
     */
    public static String getTraditionalYearName(int y) {
        // 1804年是甲子年
        y = y - 1804;
        return ("" + LunarGan[y % 10] + LunarZhi[y % 12] + "年");
    }

    /**
     * 获取生肖名
     *
     * @param y 　农历年
     * @return 生肖名
     */
    public static char getAnimalYearName(int y) {
        return LunarAnimalName[(y - 4) % 12];
    }

    public static short[] monthCodes(int year) {
        return LUNAR_INFO[year - MINI_YEAR];
    }

    static short lunarMonthCode(int lunarYear, int lunarMonth, boolean isLeapMonth) {
        short[] codes = monthCodes(lunarYear);
        int index = lunarMonth;
        if (codes[0] > 0 && codes[0] < lunarMonth || codes[0] == lunarMonth && isLeapMonth) {
            index++;
        }
        return codes[index];
    }

    static long lengthOfMonth(int lunarYear, int month, boolean isLeapMonth) {
        short[] codes = monthCodes(lunarYear);
        int i = isLeapMonth ? month + 1: month;
        return lengthOfMonth(lunarYear, month, codes[i]);
    }

    static int codeYear(int code) {
        return code / 100 > 12 ? 1 : 0;
    }

    static int codeMonth(int code) {
        int m = code / 100;
        if (m > 12) m -= 12;
        return m;
    }

    static int codeDay(int code) {
        return code % 100;
    }

    /**
     * 求一个农历月的天数
     * @param lunarYear 农历年
     * @param month 农历月
     * @param code 农历月有日期，Mdd 表示
     * @return 月的天数
     */
    private static long lengthOfMonth(int lunarYear, int month, short code) {
        short md2;
        short[] starts = monthCodes(lunarYear);
        int y2 = lunarYear;
        if (month + 1 < starts.length && starts[month] == code) {
            md2 = starts[month + 1];
        } else if (month + 2 < starts.length && starts[month + 1] == code) {
            md2 = starts[month + 2];
        } else if (lunarYear - MINI_YEAR + 1 < LUNAR_INFO.length) {
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

        return solarDiff(c2, c1, Calendar.DATE);
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
        int index = monthIndex(y, lunarMonth, isLeapMonth);
        boolean isLeapMonth = false;
        int sum = index + amount;
        if (amount > 0) {
            for (int _y = lunarYear; _y < MAX_YEAR; _y++) {
                final short[] a = monthCodes(_y);
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
                Map.Entry<Integer, Boolean> en = month(--y, -1);
                m = en.getKey();
                isLeapMonth = en.getValue();
            } else {
                for (int i = lunarYear - 1; i > MINI_YEAR; i--) {
                    int lunarMonths = monthCodes(i).length - 1;
                    sum += lunarMonths;
                    y--;
                    if (sum >= 0) {
                        Map.Entry<Integer, Boolean> en;
                        if (sum == 0) {
                            en = month(--y, -1);
                        } else {
                            en = month(y, sum + 1);
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
    public void checkComputeLunarDate(int y, int m, int d, boolean isLeap) {
        int days = d;
        if (d > 29 && d > lengthOfMonth(y, m, isLeap)) {
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
        if (this.lunarYear < MINI_YEAR || this.lunarYear > MAX_YEAR || this.lunarMonth < 1
                || this.lunarMonth > 12 || this.dayOfLunarMonth < 1
                || this.dayOfLunarMonth > 30) {
            return "Wrong lunar date: " + lunarYear + " " + lunarMonth + " " + dayOfLunarMonth;
        }
        return getYearName(this.lunarYear) + "年" + (this.isLeapMonth() ? "闰" : "")
                + getMonthName(this.lunarMonth) + "月" + getDayName(this.dayOfLunarMonth);
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
            throw new IllegalArgumentException("Wrong lunar dayOfLunarMonth: " + lunarMonth + " " + dayOfLunarMonth);
        }
        if (showLeap) {
            return (this.isLeapMonth() ? "闰" : "") + getMonthName(this.lunarMonth) + "月"
                    + getDayName(this.dayOfLunarMonth);
        } else {
            return getMonthName(this.lunarMonth) + "月" + getDayName(this.dayOfLunarMonth);
        }
    }

    /**
     * 返回中国农历的全名
     *
     * @return String
     */
    public String getFullLunarName() {
        return this.toString() + " " + getTraditionalYearName(this.lunarYear) + " " + getAnimalYearName(this.lunarYear);
    }

    /**
     * 创建LunarInfo中某一年的一列公历日历编码<br>
     * 公历日历编码：年份+月份+天，用于查询某个公历日期在某个LunarInfo列的哪一个区间<br>
     *
     * @param solarYear 年份
     * @return 公历日历编码
     */
    private int[] builderSolarCodes(int solarYear) {
        if (solarYear < MINI_YEAR || solarYear > MAX_YEAR) {
            throw new IllegalArgumentException("Illegal solar year: " + solarYear);
        }
        int lunarIndex = solarYear - MINI_YEAR;
        int[] solarCodes = new int[LUNAR_INFO[lunarIndex].length];
        for (int i = 0; i < solarCodes.length; i++) {
            if (0 == i) { // 第一个数表示闰月，不用更改
                solarCodes[i] = LUNAR_INFO[lunarIndex][i];
            } else if (1 == i) {
                if (LUNAR_INFO[lunarIndex][1] > 999) {
                    // 这年农历一月一日对应的公历实际是上一年的
                    solarCodes[i] = (solarYear - 1) * 10000 + LUNAR_INFO[lunarIndex][i];
                } else {
                    solarCodes[i] = solarYear * 10000 + LUNAR_INFO[lunarIndex][i];
                }
            } else {
                solarCodes[i] = solarYear * 10000 + LUNAR_INFO[lunarIndex][i];
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
        if (lunarYear < MINI_YEAR || lunarYear > MAX_YEAR) {
            throw new IllegalArgumentException("LunarYear must in (" + MINI_YEAR + "," + MAX_YEAR + ")");
        }
        this.lunarYear = lunarYear;
        this.lunarMonth = lunarMonth;
        this.dayOfLunarMonth = lunarDate;
        this.isLeapMonth = isLeapMonth;
        short code = lunarMonthCode(lunarYear, lunarMonth, isLeapMonth);

        // 对设置的day of month 进行检查
        if (lunarDate == 30) {
            long length = lengthOfMonth(lunarYear, lunarMonth, code);
            if (length != 30) {
                throw new IllegalArgumentException(String.format("农历%d年%d月, 闰月=%s，月天数为%d < %d", lunarYear, lunarMonth, isLeapMonth, length, lunarDate));
            }
        }

        super.set(Calendar.YEAR, lunarYear + codeYear(code));
        super.set(Calendar.MONTH, codeMonth(code) - 1);
        super.set(Calendar.DATE, codeDay(code));
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
        if (solarYear < MINI_YEAR
                || (solarYear == MINI_YEAR && solarMonth < MINI_MONTH)
                || (solarYear == MINI_YEAR && solarMonth == MINI_MONTH && solarDate < MINI_DATE)
                || solarYear > MAX_YEAR
                || (solarYear == MAX_YEAR && solarMonth > MAX_MONTH)
                || (solarYear == MAX_YEAR && solarMonth == MAX_MONTH && solarDate > MAX_DATE)
        ) {
            // 有些中间过程日期会超出可计算范围
            // throw new IllegalArgumentException("Illegal solar year: " + solarYear);
            return;
        }
        int solarCode = solarYear * 10000 + 100 * (1 + solarMonth) + solarDate; // 公历码
        leapMonth = LUNAR_INFO[solarYear - MINI_YEAR][0];
        int[] solarCodes = builderSolarCodes(solarYear);
        int newMonth = binSearch(solarCodes, solarCode);
        if (-1 == newMonth) {
            throw new IllegalArgumentException("No lunarInfo found by solarCode: " + solarCode);
        }
        int xDate = Long.valueOf(solarDateCodesDiff(solarCode, solarCodes[newMonth], Calendar.DATE)).intValue();
        if (0 == newMonth) {// 在上一年
            int preYear = solarYear - 1;
            short[] preSolarCodes = LUNAR_INFO[preYear - MINI_YEAR];
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
            xDate = Long.valueOf(solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
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
            short[] nextSolarCodes = LUNAR_INFO[solarYear + 1 - MINI_YEAR];
            // 取下一年农历1月1号公历日期码
            int nearSolarCode = solarYear * 10000 + nextSolarCodes[1]; // 下一年农历1月1号公历日期码
            xDate = Long.valueOf(solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
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

    /**
     * 一个简单的二分查找，返回查找到的元素坐标，用于查找农历二维数组信息
     *
     * @param array 　数组
     * @param n     　待查询数字
     * @return 查到的坐标
     */
    private static int binSearch(int[] array, int n) {
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
