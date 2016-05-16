package com.saray.sinotk.util;

import com.saray.sinotk.entity.User;

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
	public SharedPreferencesUtil(Context context)
	{
		this.mContext = context;
	}
	
	@SuppressWarnings("deprecation")
	public  SharedPreferences sharedPreferences(Context context,String className) {
		SharedPreferences sp = mContext.getSharedPreferences(className,
				Context.MODE_WORLD_WRITEABLE);
		return sp;
	}
	
	
	//�Ѿ���װ
	public void HasInstalled(Context context,String className)
	{
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putString("FIIRST", "false");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	public boolean IsFirstTimeInstall(Context context,String className,String value)
	{
		boolean temp = false;
		try {
			String string= sharedPreferences(context,className).getString("FIIRST", value);
			if(!string.contains("false"))
			{
				temp = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	/**
	 * ��ס�û���
	 * @param context
	 * @param className
	 * @param isRemember
	 */
	public void RememberName(Context context,String className,boolean isRemember)
	{
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putBoolean("ISREMEMBERNAME", isRemember);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	//�ж��Ƿ��ס�û���
	public boolean IsRememberName(Context context,String className,boolean defaultValue)
	{
		boolean ret = false;
		try {
			ret = sharedPreferences(context,className).getBoolean("ISREMEMBERNAME", defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ��ס����
	 * @param context
	 * @param className
	 * @param isRemember
	 */
	public void RememberPassword(Context context,String className,boolean isRemember)
	{
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putBoolean("ISREMEMBERPASSWORD", isRemember);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	//�ж��Ƿ��ס����
	public boolean IsRememberPassword(Context context,String className,boolean defaultValue)
	{
		boolean ret = false;
		try {
			ret = sharedPreferences(context,className).getBoolean("ISREMEMBERPASSWORD", defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * �洢�û��������롢��������ID������������
	 * @param context
	 * @param className
	 * @param user
	 */
	public void SharedPreferencesUserInfoStore(Context context,String className,User user)
	{
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putString("USER_NAME", user.getName());
			editor.putString("PASSWORD",user.getPassword());
			editor.putString("DATA_CENTER_ID", user.getDatacenterid());
			editor.putString("NAME",user.getName());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	/**
	 * �洢�û���������
	 * @param context ������
	 * @param className ����
	 * @param userNameValue �û���
	 * @param passwordValue �û�����
	 */
	public void SharedPreferencesUserInfoStore(Context context,String className,String userNameValue,String passwordValue)
	{
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putString("USER_NAME", userNameValue);
			editor.putString("PASSWORD",passwordValue);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	/***
	 * ��ȡ�û���
	 * @param context
	 * @param className
	 * @param value ���û��ֵ��Ĭ��Ϊvalue
	 * @return
	 */
	public String SharedPreferencesUserNameGet(Context context,String className,String value)
	{
		String string = null;
		try {
			string = sharedPreferences(context,className).getString("USER_NAME", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * ��ȡ����
	 * @param context
	 * @param className
	 * @param value ���û��ֵ��Ĭ��Ϊvalue
	 * @return
	 */
	public String SharedPreferencesUserPasswordGet(Context context,String className,String value)
	{
		String string = null;
		try {
			string = sharedPreferences(context,className).getString("PASSWORD", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/***
	 * ��ȡ��������ID
	 * @param context
	 * @param className
	 * @param value ���û��ֵ��Ĭ��Ϊvalue
	 * @return
	 */
	public String SharedPreferencesDataCenterIDGet(Context context,String className,String value)
	{
		String string = null;
		try {
			string = sharedPreferences(context,className).getString("DATA_CENTER_ID", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * ��ȡ����������
	 * @param context
	 * @param className
	 * @param value ���û��ֵ��Ĭ��Ϊvalue
	 * @return
	 */
	public String SharedPreferencesDataCenterNameGet(Context context,String className,String value)
	{
		String string = null;
		try {
			string = sharedPreferences(context,className).getString("NAME", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * sharedPreferencesStringStore
	 * @param className Class Name
	 * @param key key string 
	 * @param value value to be stored
	 */
	public  void sharedPreferencesStringStore(Context context,String className,String key,String value) {
		Editor editor = sharedPreferences(context,className).edit();
		try {
			editor.putString(key, value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	/**
	 * getStringSharedPreferences
	 * @param className class name
	 * @param key type to get
	 * @param value  Value to return if this preference does not exist.
	 * @return string string to return
	 */
	public  String getStringSharedPreferences(Context context,String className,String key,String value) {
		String string = null;
		try {
			string = sharedPreferences(context,className).getString(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * clearData
	 * @param className class name
	 */
	public  void clearData(Context context,String className)
	{
		Editor editor = sharedPreferences(context,className).edit();; 
		editor.clear().commit();
	}
}
