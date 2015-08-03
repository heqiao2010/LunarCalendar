package com.github.heqiao2010;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LunarCalendar {
	//农历年，和公历是一样的
	private int year;
	//农历月
	private int month; 
	//农历日期
	private int date; 
	//农历的闰月，如果不闰月，默认为0
	public int leapMonth = 0;
	//公历日期
	public GregorianCalendar solar = null;
	
	/**
	 * 无参构造子，默认为当前日期
	 */
	public LunarCalendar(){
		solar = new GregorianCalendar();
	}
	
	/**
	 * 通过年、月、日构造LunarCalendar
	 * @param year
	 * @param month
	 * @param day
	 * @param isLunar 是否为农历日期
	 * @param isleapMonth 在为农历日期的前提下，是否闰月
	 */
	public LunarCalendar(int year, int month, int date, boolean isLunar, boolean isleapMonth){
		if(isLunar){
			this.computeByLunarDate(year, month, date, isleapMonth);
			this.year = year;
			this.month = month;
			this.date = date;
		} else {
			this.computeBySolarDate(year, month, date);
			this.getSolar().set(Calendar.YEAR, year);
			this.getSolar().set(Calendar.MONTH, month - 1);
			this.getSolar().set(Calendar.DATE, date);
		}
	}
	
	/**
	 * 创建LunarInfo中某一年的一列公历日历编码<br>
	 * 公历日历编码：年份+月份+天，用于查询某个公历日期在某个LunarInfo列的哪一个区间<br>
	 * @return int[]
	 */
	private int[] builderSolarCodes(int solarYear){
		if(solarYear < LunarConst.MINIYEAR && solarYear > LunarConst.MAXYEAR){
			return null;
		}
		int lunarIndex = solarYear - LunarConst.MINIYEAR;
		int solarCodes[] = new int[LunarConst.LuarInfo[lunarIndex].length];
		for(int i=0; i<solarCodes.length; i++){
			if(0 == i){ //第一个数表示闰月，不用更改
				solarCodes[i] = LunarConst.LuarInfo[lunarIndex][i];
			} else if(1 == i){
				if(LunarConst.LuarInfo[lunarIndex][1] > 999){
					//这年农历一月一日对应的公历实际是上一年的
					solarCodes[i] = (solarYear - 1) * 10000 +  LunarConst.LuarInfo[lunarIndex][i];
				} else {
					solarCodes[i] = solarYear * 10000 +  LunarConst.LuarInfo[lunarIndex][i];
				}
			} else {
				solarCodes[i] = solarYear * 10000 +  LunarConst.LuarInfo[lunarIndex][i];
			}
		}
		return solarCodes; 
	}
	
	/**
	 * Adds the specified (signed) amount of time to the given calendar field
	 * @param field
	 * @param amount
	 */
	public void add(int field, int amount){
		this.getSolar().add(field, amount);
		this.computeBySolarDate(this.getSolar().get(Calendar.YEAR), 
				this.getSolar().get(Calendar.MONTH), 
				this.getSolar().get(Calendar.DATE));
	}
	
	/**
	 * 通过给定的农历日期，公历日期
	 * @param lunarYear
	 * @param lunarMonth
	 * @param lunarDate
	 * @param isleapMonth
	 * @return
	 */
	private boolean computeByLunarDate(int lunarYear, int lunarMonth, int lunarDate, boolean isleapMonth){
		boolean isSuccess = true;
		if (lunarYear < LunarConst.MINIYEAR && lunarYear > LunarConst.MAXYEAR) {
			isSuccess = false;
			return isSuccess;
		}
		this.year = lunarYear;
		this.month = lunarMonth;
		this.date = lunarDate;
		int solarMontDate = LunarConst.LuarInfo[lunarYear - LunarConst.MINIYEAR][lunarMonth];
		int leapMonth = LunarConst.LuarInfo[lunarYear - LunarConst.MINIYEAR][0];
		if (leapMonth !=0 && 
				(lunarMonth > leapMonth || (lunarMonth == leapMonth && isleapMonth))) {
			// 闰月，且当前农历月大于闰月月份，取下一个月的LunarInfo码
			// 闰月，且当前农历月等于闰月月份，并且此农历月为闰月，取下一个月的LunarInfo码
			solarMontDate = LunarConst.LuarInfo[lunarYear - LunarConst.MINIYEAR][lunarMonth + 1];
		} 
		this.getSolar().set(Calendar.YEAR, lunarYear);
		this.getSolar().set(Calendar.MONTH, solarMontDate / 100);
		this.getSolar().set(Calendar.DATE, solarMontDate % 100);
		this.add(Calendar.DATE, lunarDate);
		return isSuccess;
	}
	
	/**
	 * 通过给定公历日期，计算农历日期
	 * @param solarYear
	 * @param solarMonth
	 * @param solarDay
	 * @return
	 */
	private boolean computeBySolarDate(int solarYear, int solarMonth, int solarDate){
		boolean isSuccess = true;
		if (solarYear < LunarConst.MINIYEAR && solarYear > LunarConst.MAXYEAR) {
			isSuccess = false;
			return isSuccess;
		}
		int solarCode = solarYear * 10000 + 100 * solarMonth + solarDate; // 公历码
		int leapMonth = LunarConst.LuarInfo[solarYear - LunarConst.MINIYEAR][0];
		int solarCodes[] = builderSolarCodes(solarYear);
		int newMonth = binSearch(solarCodes, solarCode);
		if(-1 == newMonth){ //出错
			return !isSuccess;
		} else if( 0 == newMonth ) {//在上一年
			solarYear--;
			int[] preSolarCodes = LunarConst.LuarInfo[solarYear - LunarConst.MINIYEAR];
			// 取上年农历12月1号公历日期码
			int nearSolarCode = preSolarCodes[preSolarCodes.length - 1]; //上一年12月
			nearSolarCode = (nearSolarCode / 100 == 13 ? solarYear + 1 : solarYear) * 10000 + 
					(nearSolarCode / 100 == 13 ? nearSolarCode - 1200 : nearSolarCode);
			if(nearSolarCode > solarCode){//此公历日期在上一年农历12月1号，之前，即在上年农历11月内
				newMonth = 11;
				//取农历11月的公历码
				nearSolarCode = solarYear * 10000 + preSolarCodes[preSolarCodes.length - 2];
			} else {//此公历日期在上一年农历12月内
				newMonth = 12;
			}
			int xdate = new Long(solarDateCodesDiff(solarCode, nearSolarCode, Calendar.DATE)).intValue();
			this.date = 1 + (xdate < 0 ? -xdate : xdate);
			this.year = solarYear;
			this.month = newMonth;
		} else {
			int xdate = new Long(solarDateCodesDiff(solarCode, solarCodes[newMonth], Calendar.DATE)).intValue();
			this.date = 1 + (xdate < 0 ? -xdate : xdate);
			this.year = solarYear;
			if(0!=leapMonth && leapMonth < newMonth){
				this.month = newMonth + 1;
			} else {
				this.month = newMonth;
			}
		}
		return isSuccess;
	}
	
	/**
	 * 计算两个日期之差
	 */
	public static void dateDiff(){
		
	}
	
	/**
	 * 公历转农历
	 * @param solar
	 * @return LunarCalendar
	 */
	public static LunarCalendar solar2Lunar(Calendar solar) {
		LunarCalendar ret = new LunarCalendar();
		if(ret.computeBySolarDate(solar.get(Calendar.YEAR),
				solar.get(Calendar.MONTH) + 1, solar.get(Calendar.DATE))){
			return ret;
		} else {
			ret = null;
			return ret;
		}
	}
	
	/**
	 * 农历转公历
	 * @param lunarYear
	 * @param lunarMonth
	 * @param LunarDate
	 * @param isLeapMonth
	 * @return
	 */
	public static Calendar lunar2Solar(int lunarYear, int lunarMonth, int LunarDate, boolean isLeapMonth){
		LunarCalendar ret = new LunarCalendar();
		if(ret.computeByLunarDate(lunarYear, lunarMonth, LunarDate, isLeapMonth)){
			return ret.getSolar();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param c
	 * @param filed
	 * @param n
	 */
	public void solarAdd(int field, int n){
		this.getSolar().add(field, n);
	}

	/**
	 * 获取天
	 * @return
	 */
	public String getDayName(int lunarDay){
		return LunarConst.LunarDayName[lunarDay-1];
	}
	
	/**
	 * 获取农历月份
	 * @param lunarMonth
	 * @return
	 */
	public String getMonthName(int lunarMonth){
		return LunarConst.LunarMonthName[lunarMonth-1];
	}
	
	/**
	 * 获取年
	 */
	public String getYearName(int lunarYear) {
		StringBuilder sb = new StringBuilder();
		sb.append(LunarConst.LunarYearName[lunarYear / 1000]);
		sb.append(LunarConst.LunarYearName[lunarYear % 1000 / 100]);
		sb.append(LunarConst.LunarYearName[lunarYear % 100 / 10]);
		sb.append(LunarConst.LunarYearName[lunarYear % 10]);
		return sb.toString();
	}
	
	/**
	 * 判断两个整数所代表公历日期的差值<br>
	 * 一年按365天计算，一个月按30天计算<br>
	 * @param solarCode1
	 * @param solarCode2
	 * @return
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
	 * @param solar1
	 * @param solar2
	 * @param field
	 * @return
	 */
	public static long solarDiff(Calendar solar1, Calendar solar2, int field){
		long t1 = solar1.getTimeInMillis();
		long t2 = solar2.getTimeInMillis();
		switch(field){
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
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
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
	
	/**
	 * 一个简单的二分查找，返回查找到的元素坐标，用于查找农历二维数组信息
	 * @param array
	 * @param n
	 * @return 
	 */
	private static int binSearch(int[] array, int n){
		if (null == array || array.length == 0){
			return -1;
		}
		int min = 0, max = array.length - 1;
		while(max - min > 1){
			int newIndex = (max + min) / 2; //二分
			if(array[newIndex] > n){ //取小区间
				max = newIndex;
			} else if(array[newIndex] < n){//取大区间
				min = newIndex;
			} else { //相等，直接返回下标
				return newIndex;
			}
		}
		if(array[max] == n){
			return max;
		} else if(array[min] == n){
			return min;
		} else {
			return min; //返回较小的一个
		}
	}
	
	@Override
	public String toString() {
		if(this.year < 1900 || this.year > 2099
				|| this.month < 1 || this.month > 12
				|| this.date < 1 || this.date > 30){
			return "Wrong lunar date.";
		}
		return this.getYearName(this.year) + "年"
				+ this.getMonthName(this.month) + "月"
				+ this.getDayName(this.date);
	}

	/**
	 * Test Main!
	 * @param args
	 */
	public static void main(String[] args){
//		LunarCalendar c = new LunarCalendar();
//		int lunarDay = 9;
//		int lunarMonth = 12;
//		int lunarYear = 1048;
//		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		
//		System.out.println(c.getDayName(lunarDay));
//		System.out.println(c.getMonthName(lunarMonth));
//		System.out.println(c.getYearName(lunarYear));
//		
//		Calendar c1 = Calendar.getInstance();
//		Calendar c2 = Calendar.getInstance();
//		System.out.println(df.format(c1.getTime()));
//		c1.add(Calendar.MONTH, 10);
//		System.out.println(df.format(c1.getTime()));
//		System.out.println(binSearch(LunarConst.LuarInfo[1900-1900], 1121));
//		
//		c1.set(Calendar.YEAR, 1991);
//		c1.set(Calendar.MONTH, 3);
//		c1.set(Calendar.DATE, 1);
//		c1.set(Calendar.SECOND, 10);
//		
//		c2.set(Calendar.YEAR, 1991);
//		c2.set(Calendar.MONTH, 2);
//		c2.set(Calendar.DATE, 1);
//		c2.set(Calendar.SECOND, 10);
//		System.out.println(solarDiff(c1, c2, Calendar.DATE));
//		System.out.println(df.format(c1.getTime()));
//		System.out.println(df.format(c2.getTime()));
		
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		for(int i=0; i<1000; i++){
			c1.add(Calendar.DATE, 1);
			LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
			System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
		}
		
		/**
		 * Solar：2016-12-29Lunar：Wrong lunar date.
		 * Solar：2016-12-30Lunar：Wrong lunar date.
		 * Solar：2016-12-31Lunar：Wrong lunar date.
		 */
//		c1.set(Calendar.YEAR, 2016);
//		c1.set(Calendar.MONTH, 11);
//		c1.set(Calendar.DATE, 29);
//		LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
//		System.out.println();
//		System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
		
	}
}
