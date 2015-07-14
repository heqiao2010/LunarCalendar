package com.github.heqiao2010;

import java.util.Calendar;

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
	 * 公历转农历
	 * @param solar
	 * @return LunarCalendar
	 */
	public static LunarCalendar solar2Lunar(Calendar solar){
		LunarCalendar ret = new LunarCalendar();
		
		return ret;
	}
}
