package com.saray.sinotk.util;

import com.saray.sinotk.util.GXData.GX;

public class GXUtil{
	
	public static GX CaculateGx(String item, String itemValue)
	{
		if(item != null)
		{
			int value = Integer.parseInt(itemValue);
	    	GX gx = null;
			if(item.contains("so2"))
			{
				//计算GX的值， 抓取的SO2数据单位为ug/m3
		    	double ppbValue = value *22.4/64 ;
		    	if(ppbValue<=10){
		    		gx= GX.G1;
		    	}else if(10<ppbValue && ppbValue<=100){
		    		gx= GX.G2;
		    	}else if(100<ppbValue && ppbValue<=300){
		    		gx= GX.G3;
		    	}else{
		    		gx= GX.GX;
		    	}
			}
			else if(item.contains("no2"))
			{
				//计算GX的值， 抓取的NO2数据单位为ug/m3
		    	double ppbValue = value *22.4/64 ;
		    	if(ppbValue<=50){
		    		gx= GX.G1;
		    	}else if(50<ppbValue && ppbValue<=125){
		    		gx= GX.G2;
		    	}else if(125<ppbValue && ppbValue<1250){
		    		gx= GX.G3;
		    	}else{
		    		gx= GX.GX;
		    	}
			}
			return gx;
		}
		else
		{
			return null;
		}
	}
	
}
