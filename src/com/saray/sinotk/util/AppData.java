package com.saray.sinotk.util;

import android.text.format.Time;

public class AppData {
	
	public void GetTime()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
	}
	
	//当前时间前的八小时
	public static String[] GetPerHour()
	{
		String[] ret = new String[]{"01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00"};
		return ret;
	}
	
	public static String[] GetPerDay()
	{
		String[] ret = new String[]{"04:00","07:00","10:00","13:00","16:00","19:00","22:00","23:00"};
		return ret;
	}
	
	public static String[] GetPerMounth()
	{
		String[] ret = new String[]{"1","5","9","13","17","21","25","29"};
		return ret;
	}

}
