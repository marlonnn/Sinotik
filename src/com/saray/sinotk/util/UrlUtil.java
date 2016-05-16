package com.saray.sinotk.util;

import com.saray.sinotik.config.Config;

public class UrlUtil {

	public static String GetUrl (int taskType)
	{
        String url = "";
        switch (taskType)
        {
        /**
         * ��½
         */
        case Config.LOGIN_TYPE:
            url = Config.ADDRESS + "?action=userLogin";
            break;
        /**
         * ��ȡ�����б�
         */
        case Config.staion_list:
        	url = Config.Data + "?action=stationListOfCity";
        	break;
        /**
         * ��ȡ���м�������
         */
        case Config.all_out_door:
        	url = Config.Data + "?action=allOutDoorDataListOfCity";
        	break;
    	/**
    	 * ��ȡ���л�������
    	 */
        case Config.all_inner:
        	url = Config.Data + "?action=dataCenterDataList";
        	break;
        }
        return url;
	}
}
