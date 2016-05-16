package com.saray.sinotik.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.saray.sinotk.entity.InnerStationData;
import com.saray.sinotk.entity.OutStation;
import com.saray.sinotk.entity.OutStationData;

public class DataCache {
	
	/**
	 * ������������б�
	 */
	private List<OutStation> outStation;
	
	/**
	 * ���е�����������ݻ���
	 */
	private List<OutStationData> outData;
	
	/**
	 * ���л���������
	 */
	private List<InnerStationData> innerData;
	
	/**
	 * ɸѡ������ָ�������һ�������
	 */
	private List<OutStationData> selectDayData = new ArrayList<OutStationData>();

	public static DataCache getInstance() {
        return InstanceHolder.instance;
    }
    
    static class InstanceHolder {
        final static DataCache instance = new DataCache();
    }

	public List<OutStationData> getOutData() {
		return outData;
	}

	public void setOutData(List<OutStationData> outData) {
		this.outData = outData;
	}

	public List<OutStation> getOutStation() {
		return outStation;
	}

	public void setOutStation(List<OutStation> outStation) {
		this.outStation = outStation;
	}
    
    public List<OutStationData> getSelectDayData() {
		return selectDayData;
	}

	public void setSelectDayData(List<OutStationData> selectDayData) {
		this.selectDayData = selectDayData;
	}

	public List<InnerStationData> getInnerData() {
		return innerData;
	}

	public void setInnerData(List<InnerStationData> innerData) {
		this.innerData = innerData;
	}
}
