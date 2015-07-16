package com.github.heqiao2010;

import java.util.Calendar;
import java.util.Date;

public class LunarCalendar {
	//农历年，和公历是一样的
	private int year;
	//农历月
	private int month; 
	//农历日期
	private int day; 
	//农历的闰月，如果不闰月，默认为0
	public int leapMonth = 0;
	//公历日期
	public Calendar solar = null;
	
	/**
	 * 无参构造子，默认为当前日期
	 */
	public LunarCalendar(){
		solar = Calendar.getInstance();
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param isLunar
	 */
	public LunarCalendar(int year, int month, int day, boolean isLunar){
		if(isLunar){
			this.year = year;
			this.month = month;
			this.day = day;
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
	private int findNearLunarInfo(int solarYear, int solarMonth, int solarDay){
		if(solarYear < LunarConst.MINIYEAR && solarYear > LunarConst.MAXYEAR){
			return -1;
		}
		int searchNum = 100 * solarMonth + solarDay; //查询码
		int lunarIndex = solarYear - LunarConst.MINIYEAR;
		int solarCodes[] = builderSolarCodes(solarYear);
		int retI = binSearch(solarCodes, searchNum);
		if(-1 == retI){ //出错
			return -1;
		} else if( 0 == retI ) //在上一年
		
		Calendar.
		solarCodes[retI];
		
		
		
		
		int firstDayNum = LunarConst.LuarInfo[lunarIndex][1]; //取出这年农历一月对应的公历码
		int secondDayNum = LunarConst.LuarInfo[lunarIndex][2];
		int start = 0;
		if(firstDayNum > 999){
			//这年农历一月一号这天公历日期的年份为上一年年份
			start = 2;
			if( secondDayNum > searchNum){ 
				//所求农历位于该年一月份，计算和农历一月一日的天数差
				int segDays = new Date(solarYear, solarMonth, solarDay).get
			}
		} else {
			//这年农历一月一号这天公历日期的年份为当前年
			start = 0;
		}
		int searRet = binSearch(LunarConst.LuarInfo[lunarIndex], searchNum, start ,LunarConst.LuarInfo[lunarIndex].length - 1);
		
		
		int searchRet = binSearch(LunarConst.LuarInfo[solarYear - LunarConst.MINIYEAR], searchNum);
		if(0 == searchRet){ //所找日期在上一年
			
		} else {
			
		}
		return index;
	}
	
	/**
	 * 计算两个日期之差
	 */
	public static dateDiff(){
		
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(int leapMonth) {
		this.leapMonth = leapMonth;
	}

	public Calendar getSolar() {
		return solar;
	}

	public void setSolar(Calendar solar) {
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
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println(c.getDayName(lunarDay));
		System.out.println(c.getMonthName(lunarMonth));
		System.out.println(c.getYearName(lunarYear));
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		System.out.println(df.format(c1.getTime()));
		c1.add(Calendar.MONTH, 10);
		System.out.println(df.format(c1.getTime()));
		System.out.println(binSearch(LunarConst.LuarInfo[1900-1900], 1121));
		
	}
}
