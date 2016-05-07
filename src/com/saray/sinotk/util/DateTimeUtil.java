package com.saray.sinotk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

	public static Date GetDate(String testtime)
	{
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
		dateFormat.setLenient(false); 
		try {
			date = dateFormat.parse(testtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
