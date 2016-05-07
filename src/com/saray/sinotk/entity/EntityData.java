package com.saray.sinotk.entity;

import com.saray.sinotk.util.GXData.GX;

public class EntityData {
	
	private String stationid;
	private String api;
	private String pm25;
	private String pm10;
	private String no2;
	private GX gxNo2;
	private String so2;
	private GX gxSo2;
	private String o3;
	private String co;
	private double gxValue;
	
	private GX gx;
	
	private String quality;
	private String primarypollutant;
	private String temperature;
	private String humidity;
	private String testtime;
	private String sourceurl;
	
	public String getStationid() {
		return stationid;
	}
	public void setStationid(String stationid) {
		this.stationid = stationid;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getPm10() {
		return pm10;
	}
	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}
	public String getNo2() {
		return no2;
	}
	public void setNo2(String no2) {
		this.no2 = no2;
	}
	public String getSo2() {
		return so2;
	}
	public void setSo2(String so2) {
		this.so2 = so2;
	}
	public String getO3() {
		return o3;
	}
	public void setO3(String o3) {
		this.o3 = o3;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public GX getGx() {
		return gx;
	}
	public void setGx(GX gx) {
		this.gx = gx;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getPrimarypollutant() {
		return primarypollutant;
	}
	public void setPrimarypollutant(String primarypollutant) {
		this.primarypollutant = primarypollutant;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTesttime() {
		return testtime;
	}
	public void setTesttime(String testtime) {
		this.testtime = testtime;
	}
	public String getSourceurl() {
		return sourceurl;
	}
	public void setSourceurl(String sourceurl) {
		this.sourceurl = sourceurl;
	}
	public GX getGxNo2() {
		return gxNo2;
	}
	public void setGxNo2(GX gxNo2) {
		this.gxNo2 = gxNo2;
	}
	public GX getGxSo2() {
		return gxSo2;
	}
	public void setGxSo2(GX gxSo2) {
		this.gxSo2 = gxSo2;
	}
	public double getGxValue() {
		return gxValue;
	}
	public void setGxValue(double gxValue) {
		this.gxValue = gxValue;
	}
	
	

}
