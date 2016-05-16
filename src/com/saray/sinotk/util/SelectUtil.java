package com.saray.sinotk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.saray.sinotik.cache.DataCache;

public class SelectUtil {
	
	@SuppressWarnings("deprecation")
	public static void SelectDayData(String stationname)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
		try {
			Date current = dateFormat.parse(DataCache.getInstance().getOutData().get(0).getTesttime());
			int day = current.getDay();
			int hours = current.getHours();
			for (int i=0; i<DataCache.getInstance().getOutData().size(); i++)
			{
				Date timeDate = dateFormat.parse(DataCache.getInstance().getOutData().get(i).getTesttime());
				if (DataCache.getInstance().getOutData().get(i).getStationname().contains(stationname))
				{
					if (timeDate.getDay() == day)
					{
						if ( timeDate.getHours() <= hours)
						{
							DataCache.getInstance().getSelectDayData().add(DataCache.getInstance().getOutData().get(i));
						}
					}
					else if (timeDate.getDay() == day -1)
					{
						if ( timeDate.getHours() >= hours)
						{
							DataCache.getInstance().getSelectDayData().add(DataCache.getInstance().getOutData().get(i));
						}
					}	
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void SelectWeekData(String stationname)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
		try {
			Date current = dateFormat.parse(DataCache.getInstance().getOutData().get(0).getTesttime());
			int day = current.getDay();
			int hours = current.getHours();
			for (int i=0; i<DataCache.getInstance().getOutData().size(); i++)
			{
				Date timeDate = dateFormat.parse(DataCache.getInstance().getOutData().get(i).getTesttime());
				if (DataCache.getInstance().getOutData().get(i).getStationname().contains(stationname))
				{
					if (timeDate.getDay() == day)
					{
						if ( timeDate.getHours() <= hours)
						{
							DataCache.getInstance().getSelectDayData().add(DataCache.getInstance().getOutData().get(i));
						}
					}
					else if (timeDate.getDay() >= day -7)
					{
						if ( timeDate.getHours() >= hours)
						{
							DataCache.getInstance().getSelectDayData().add(DataCache.getInstance().getOutData().get(i));
						}
					}	
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
