package com.saray.sinotk.util;

import com.saray.sinotik.config.Config;

public class UrlUtil {

	public static String GetUrl (int taskType)
	{
        String url = "";
        switch (taskType)
        {
        /**
         * 登陆
         */
        case Config.LOGIN_TYPE:
            url = Config.ADDRESS + "?action=userLogin";
            break;
        /**
         * 获取监测点列表
         */
        case Config.staion_list:
        	url = Config.Data + "?action=stationListOfCity";
        	break;
        /**
         * 获取所有监测点数据
         */
        case Config.all_out_door:
        	url = Config.Data + "?action=allOutDoorDataListOfCity";
        	break;
    	/**
    	 * 获取所有机房数据
    	 */
        case Config.all_inner:
        	url = Config.Data + "?action=dataCenterDataList";
        	break;
        }
        return url;
	}
}
