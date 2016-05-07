package com.saray.sinotk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils {
	
//	private  final String filename = android.os.Environment.getExternalStorageDirectory()+"/weather/database.db";
	private Context context;
	private String filename;
	public DBUtils(Context context)
	{
		this.context = context;
		filename = this.context.getFilesDir() + "/weather/cityCode.db";
	}
	//查询省
	public List<Map<String, String>> getProvince(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select DISTINCT province from city";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//查询市
	public List<Map<String, String>> getCity(String province){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select DISTINCT city from city where province = "+"'"+province+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
		do {
			Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
		} while (cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//查询县（区）
	public List<Map<String, String>> getCountry(String city){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select county from city where city = "+"'"+city+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//查询地址id号
	public String getAddressId(String country){
		String result = "";
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select cityCode from city where county = "+"'"+country+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	result = cursor.getString(0);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return result;
	}
	
	//查询城市编码cityCode号
	public String getCityCode(String country){
		String result = "";
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select cityCode from city where county = "+"'"+country+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	result = cursor.getString(0);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return result;
	}
	
	//保存添加的地区和id号
	public void saveCityAndId(String city, String id){
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		ContentValues values = new ContentValues();
		values.put("city", city);
		values.put("addressId", id);
		database.insert("cityTbl", null, values);
		database.close();
	}
	//查询添加的地区和id号
	public List<Map<String, String>> getCityAndId(){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select city,addressId from cityTbl";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
	        do{
	        	String city = cursor.getString(0);
	        	String id = cursor.getString(1);
	        	Map<String, String> map = new HashMap<String, String>();
	        	map.put("address", city);
	        	map.put("id", id);
	        	list.add(map);
	        }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
	}
	//删除指定id的地区记录
	public void deleteCityAndId(String id){
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "delete from cityTbl where addressId = '" + id + "'";
		database.execSQL(SQL);
        database.close();
	}

}
