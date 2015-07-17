package com.github.heqiao2010;

import java.util.Calendar;
import java.util.Date;
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
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param isLunar
	 */
	public LunarCalendar(int year, int month, int date, boolean isLunar){
		if(isLunar){
			this.year = year;
			this.month = month;
			this.date = date;
		} else {
			
		}
	}
	
	/**
	 * 创建LunarInfo中某一年的一列公历日历编码<br>
	 * 公历日历编码：年份+月份+天，用于查询某个公历日期在某个LunarInfo列的哪一个区间<br>
	 * @return
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
	 * 在LunarInfo中，找到和公历日期YYYY-MM-DD最接近的两个日期中较小的一个
	 * @param solarYear
	 * @param solarMonth
	 * @param solarDay
	 * @return
	 */
	private int findNearLunarInfo(int solarYear, int solarMonth, int solarDate){
		if (solarYear < LunarConst.MINIYEAR && solarYear > LunarConst.MAXYEAR) {
			return -1;
		}
		int solarCode = solarYear * 10000 + 100 * solarMonth + solarDate; // 公历码
		int leapMonth = LunarConst.LuarInfo[solarYear - LunarConst.MINIYEAR][0];
		int solarCodes[] = builderSolarCodes(solarYear);
		int newMonth = binSearch(solarCodes, solarCode);
		if(-1 == newMonth){ //出错
			return -1;
		} else if( 0 == newMonth ) {//在上一年
			
		} else if( 13 == newMonth) {//在下一年
			
		}
		else {
			this.date = new Long(solarDateCodesDiff(solarCode, solarCodes[newMonth], Calendar.DATE)).intValue();
			this.year = solarYear;
			this.month = newMonth;
		}
		return 1;
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
	public static LunarCalendar solar2Lunar(Calendar solar){
		LunarCalendar ret = new LunarCalendar();
		
		return ret;
	}
	
	/**
	 * 
	 * @param c
	 * @param filed
	 * @param n
	 */
	public void add(int field, int n){
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
		GregorianCalendar c1 = new GregorianCalendar(solarCode1 / 10000, solarCode1 % 10000 / 100,
				solarCode1 % 10000 % 100);
		GregorianCalendar c2 = new GregorianCalendar(solarCode2 / 10000, solarCode2 % 10000 / 100,
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
			return (t1 - t2) / 1000;
		case Calendar.MINUTE:
			return (t1 - t2) / (60 * 1000);
		case Calendar.HOUR:
			return (t1 - t2) / (3600 * 1000);
		case Calendar.DATE:
			return (t1 - t2) / (24 * 3600 * 1000);
		case Calendar.MONTH:
			return (t1 - t2) / (30 * 24 * 3600 * 1000);
		case Calendar.YEAR:
			return (t1 - t2) / (365 * 24 * 3600 * 1000);
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
		return min; //返回较小的一个
	}
	
	/**
	 * Test Main!
	 * @param args
	 */
	public static void main(String[] args){
		LunarCalendar c = new LunarCalendar();
		int lunarDay = 9;
		int lunarMonth = 12;
		int lunarYear = 1048;
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		System.out.println(c.getDayName(lunarDay));
		System.out.println(c.getMonthName(lunarMonth));
		System.out.println(c.getYearName(lunarYear));
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		System.out.println(df.format(c1.getTime()));
		c1.add(Calendar.MONTH, 10);
		System.out.println(df.format(c1.getTime()));
		System.out.println(binSearch(LunarConst.LuarInfo[1900-1900], 1121));
		
		c1.set(Calendar.YEAR, 1991);
		c1.set(Calendar.MONTH, 3);
		c1.set(Calendar.DATE, 1);
		c1.set(Calendar.SECOND, 10);
		
		c2.set(Calendar.YEAR, 1991);
		c2.set(Calendar.MONTH, 2);
		c2.set(Calendar.DATE, 1);
		c2.set(Calendar.SECOND, 10);
		System.out.println(solarDiff(c1, c2, Calendar.DATE));
		System.out.println(df.format(c1.getTime()));
		System.out.println(df.format(c2.getTime()));
	}
}
