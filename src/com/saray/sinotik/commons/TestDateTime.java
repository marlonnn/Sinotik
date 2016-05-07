package com.saray.sinotik.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TestDateTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dateString = "2016-01-03 00:00:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
		dateFormat.setLenient(false); 
		java.util.Date timeDate;
		try {
			timeDate = dateFormat.parse(dateString);
			System.out.print(timeDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//util类型
	}

}
