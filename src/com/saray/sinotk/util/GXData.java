package com.saray.sinotk.util;

import java.util.EnumMap;

import android.content.Context;

public class GXData {
	
	private  EnumMap<GX, Integer> gxs;
	
	public enum GX {
		G1,
		G2,
		G3,
		GX;
	}
	
	public GXData()
	{
	}
	
	public GXData(Context context)
	{
		gxs = new EnumMap<GX, Integer>(GX.class);
		gxs.put(GX.G1, 1);
		gxs.put(GX.G2, 2);
		gxs.put(GX.G3, 3);
		gxs.put(GX.GX, 4);
	}
	
	
	public int GetGx(GX gx)
	{
		return gxs.get(gx);
	}
	
	public String GetGxString(GX gx)
	{
		String gxString = "";
		switch(gxs.get(gx))
		{
		case 1:
			gxString = "G1";
			break;
		case 2:
			gxString = "G2";
			break;
		case 3:
			gxString = "G3";
		    break;
		case 4:
			gxString = "G4";
			break;
		
		}
		return gxString;
	}

}
