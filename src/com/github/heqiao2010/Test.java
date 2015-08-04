package com.github.heqiao2010;

import java.util.Calendar;

public class Test {
	/**
	 * Test Main!
	 * @param args
	 */
	public static void main(String[] args){
		test2();
	}
	
	private static void test1(){
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
		System.out.println(LunarCalendar.binSearch(LunarConst.LuarInfo[1900-1900], 1121));
		
		c1.set(Calendar.YEAR, 1991);
		c1.set(Calendar.MONTH, 3);
		c1.set(Calendar.DATE, 1);
		c1.set(Calendar.SECOND, 10);
		
		c2.set(Calendar.YEAR, 1991);
		c2.set(Calendar.MONTH, 2);
		c2.set(Calendar.DATE, 1);
		c2.set(Calendar.SECOND, 10);
		System.out.println(LunarCalendar.solarDiff(c1, c2, Calendar.DATE));
		System.out.println(df.format(c1.getTime()));
		System.out.println(df.format(c2.getTime()));
	}
	
	/**
	 * 打印1900-01-31到2099-12-31所有的农历
	 */
	private static void test2(){
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, 1900);
		c1.set(Calendar.MONTH, 0);
		c1.set(Calendar.DATE, 30);
		for(int i=0; i<73019; i++){
			c1.add(Calendar.DATE, 1);
			LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
			System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
		}
	}
	
	private static void test3(){
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		/**
			Solar：2017-12-18Lunar：Wrong lunar date.
			Solar：2017-12-19Lunar：Wrong lunar date.
			Solar：2017-12-20Lunar：Wrong lunar date.
			Solar：2017-12-21Lunar：Wrong lunar date.
			Solar：2017-12-22Lunar：Wrong lunar date.
			Solar：2017-12-23Lunar：Wrong lunar date.
			Solar：2017-12-24Lunar：Wrong lunar date.
			Solar：2017-12-25Lunar：Wrong lunar date.
			Solar：2017-12-26Lunar：Wrong lunar date.
			Solar：2017-12-27Lunar：Wrong lunar date.
			Solar：2017-12-28Lunar：Wrong lunar date.
			Solar：2017-12-29Lunar：Wrong lunar date.
			Solar：2017-12-30Lunar：Wrong lunar date.
			Solar：2017-12-31Lunar：Wrong lunar date.
		 */
		c1.set(Calendar.YEAR, 2017);
		c1.set(Calendar.MONTH, 11);
		c1.set(Calendar.DATE, 31);
		LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
		System.out.println();
		System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
	}
}
