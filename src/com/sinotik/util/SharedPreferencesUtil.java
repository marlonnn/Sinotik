package com.sinotik.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/***
 * SharedPreferences util class.
 * 
 * @author Zhong Wen 2015/06/03_22:21
 *
 */
@SuppressLint("WorldWriteableFiles")
public class SharedPreferencesUtil {
	private Context mContext;
	public SharedPreferencesUtil()
	{
		
	}
	public SharedPreferencesUtil(Context context)
	{
		this.mContext = context;
	}
	
	@SuppressWarnings("deprecation")
	public SharedPreferences sharedPreferences(String className) {
		SharedPreferences sp = mContext.getSharedPreferences(className,
				Context.MODE_WORLD_WRITEABLE);
		return sp;
	}

	/**
	 * sharedPreferencesStringStore
	 * @param className Class Name
	 * @param type type string 
	 * @param string string to be stored
	 */
	public void sharedPreferencesStringStore(String className,String type,String string) {
		Editor editor = sharedPreferences(className).edit();
		try {
			editor.putString(type, string);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	/**
	 * getStringSharedPreferences
	 * @param className class name
	 * @param type type to get
	 * @return string string to return
	 */
	public String getStringSharedPreferences(String className,String type) {
		String string = null;
		try {
			string = sharedPreferences(className).getString(type, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * clearData
	 * @param className class name
	 */
	public void clearData(String className)
	{
		Editor editor = sharedPreferences(className).edit();; 
		editor.clear().commit();
	}
}
