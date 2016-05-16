package com.saray.sinotik.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.saray.sinotk.entity.InnerStationData;
import com.saray.sinotk.entity.OutStation;
import com.saray.sinotk.entity.OutStationData;

public class DataCache {
	
	/**
	 * 所有室外监测点列表
	 */
	private List<OutStation> outStation;
	
	/**
	 * 所有的室外监测点数据缓存
	 */
	private List<OutStationData> outData;
	
	/**
	 * 所有机房的数据
	 */
	private List<InnerStationData> innerData;
	
	/**
	 * 筛选出来的指定监测点的一天的数据
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
