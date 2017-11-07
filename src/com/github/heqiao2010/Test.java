package com.github.heqiao2010;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

public class Test {
	/**
	 * Test Main!
	 * @param args
	 */
	public static void main(String[] args){
		test1();
		test2();
		test3();
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
		int[] arr1900 = { 8, 131, 301, 331, 429, 528, 627, 726, 825, 924, 1023, 1122, 1222, 1320 };
		System.out.println(LunarCalendar.binSearch(arr1900, 1121));
		
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
		FileOutputStream out = null;
		PrintStream p = null;
		try {
			File testFile = new File("./solar2lunar.txt");
			if(!testFile.exists()){
				testFile.createNewFile();
			}
			out = new FileOutputStream(testFile);
			p = new PrintStream(out);
			for(int i=0; i<73019; i++){
				c1.add(Calendar.DATE, 1);
				LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
				p.println("Solar：" + df.format(c1.getTime()) + " <====> Lunar：" + luanr.getFullLunarName());
			}
		} catch (FileNotFoundException e) {
			System.out.println("未找到solar2lunar.txt文件，或者文件创建失败.");
			e.printStackTrace();
		} catch (IOException e){
			System.out.println("未找到solar2lunar.txt文件，或者文件创建失败.");
			e.printStackTrace();
		}finally {
			try {
				out.close();
				p.close();
			} catch (IOException e) {
				System.out.println("关闭流出错。");
				e.printStackTrace();
			}
		}
	}
	
	private static void test3(){
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		
		c1.set(Calendar.YEAR, 2046);
		c1.set(Calendar.MONTH, 01);
		c1.set(Calendar.DATE, 06);
		LunarCalendar luanr = LunarCalendar.solar2Lunar(c1);
		System.out.println();
		System.out.println("Solar：" + df.format(c1.getTime()) + "Lunar：" + luanr);
	}
}
