package com.saray.sinotk.util;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.saray.sinotk.MainActivity;
import com.saray.sinotk.entity.EntityData;
import com.saray.sinotk.entity.Quality;
import com.saray.sinotk.entity.User;
import com.saray.sinotk.util.GXData.GX;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataUtil {
	
	private final String TAG = "DataUtil";
	private final String sharedClassName = "Sinotik";
	
	private final String sharedClassName_Machine = "Sinotik_Machine";
	
	private final String data_key = "baseData";
	
	private final String data_key_machine = "baseData_machine";
	
	private final String sharedClassNameLogin = "Sinotik_Login";
	private final String sharedClassNameSelf = "Sinotik_Self";
	
	private final String sharedClassNameMain = "Sinotik_main";
	private final String data_key_main = "fistInstall";
	
	private Context context;
	private SharedPreferencesUtil mSharedPreferencesUtil;
	private FinalHttp finalHttp;
	
	public List<EntityData> entityDataList;
	public List<Quality> qualityList;//pm2.5 pm10等
	
	public final int YOU = 0X01;
	public final int LIANG = 0X02;
	public final int QINGDU = 0X03;
	public final int ZHONGDU = 0X04;
	public final int ZHONGDUWURAN = 0X05;
	public final int YANGZHONG = 0X06;
	
	public GXData gxData;
	
	public DataUtil()
	{
		
	}
	
	public DataUtil(Context context)
	{
		this.context = context;
		mSharedPreferencesUtil = new SharedPreferencesUtil(context);
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(5000);
		gxData = new GXData(context);
	}
	
	private void StoreUser(User user)
	{
		mSharedPreferencesUtil.SharedPreferencesUserInfoStore(context,sharedClassNameLogin, user);
	}
	
	//通过SharedPreferences存储用户名和密码
	private void StoreUser(String userNameValue,String passwordValue)
	{
		mSharedPreferencesUtil.SharedPreferencesUserInfoStore(context, sharedClassNameLogin, userNameValue, passwordValue);
	}
	
	public void StoreUserSelf(String userNameValue,String passwordValue)
	{
		mSharedPreferencesUtil.SharedPreferencesUserInfoStore(context, sharedClassNameSelf, userNameValue, passwordValue);
	}
	
	
	//获取用户名
	public String GetUserName()
	{
		return mSharedPreferencesUtil.SharedPreferencesUserNameGet(context, sharedClassNameLogin, "");
	}
	//获取密码
	public String GetPassword()
	{
		return mSharedPreferencesUtil.SharedPreferencesUserPasswordGet(context, sharedClassNameLogin, "");
	}
	
	//获取数据中心ID
	public String GetDataCenterID()
	{
		return mSharedPreferencesUtil.SharedPreferencesDataCenterIDGet(context, sharedClassNameLogin, "");
	}
	
	//获取数据名
	public String GetDataCenterName()
	{
		return mSharedPreferencesUtil.SharedPreferencesDataCenterNameGet(context, sharedClassNameLogin, "");
	}
	//获取用户名
	public String GetUserNameSelf()
	{
		return mSharedPreferencesUtil.SharedPreferencesUserNameGet(context, sharedClassNameSelf, "");
	}
	//获取密码
	public String GetPasswordSelf()
	{
		return mSharedPreferencesUtil.SharedPreferencesUserPasswordGet(context, sharedClassNameSelf, "");
	}
	
	//已经安装了并且成功更新了
	public void HasInstalled()
	{
		mSharedPreferencesUtil.HasInstalled(context, sharedClassNameMain);
	}
	
	//判读是否安装了并且更新了
	public boolean IsFirstTimeInstall()
	{
		return mSharedPreferencesUtil.IsFirstTimeInstall(context, sharedClassNameMain, "");
	}
	
	/**
	 * 是否记住用户名
	 * @param isRemember
	 */
	public void RememberUserName(boolean isRemember)
	{
		mSharedPreferencesUtil.RememberName(context, sharedClassNameLogin, isRemember);
	}
	//判断是否记住用户名
	public boolean IsRememberUserName()
	{
		return mSharedPreferencesUtil.IsRememberName(context, sharedClassNameLogin, false);
	}
	/**
	 * 记住密码
	 * @param isRemember
	 */
	public void RememberPassword(boolean isRemember)
	{
		mSharedPreferencesUtil.RememberPassword(context, sharedClassNameLogin, isRemember);
	}
	//判断是否记住密码
	public boolean IsRememberPassword()
	{
		return mSharedPreferencesUtil.IsRememberPassword(context, sharedClassNameLogin, false);
	}
    /**  
     * 用MD5算法进行加密  
     * @param str 需要加密的字符串  
     * @return MD5加密后的结果  
     */    
    public String encodeMD5String(String str) {    
        return encode(str, "MD5");    
    }
    
    private String encode(String str, String method) {    
        MessageDigest md = null;    
        String dstr = null;    
        try {    
            md = MessageDigest.getInstance(method);    
            md.update(str.getBytes());    
            dstr = new BigInteger(1, md.digest()).toString(16);    
        } catch (NoSuchAlgorithmException e) {    
            e.printStackTrace();    
        }    
        return dstr;    
    }
    
    public String GetFormatLoginString(String userName,String password)
    {
    	String md5Password = encodeMD5String(password);
    	String url = "http://115.159.44.146/ebdpmp/Api/user.php?action=userLogin&username=" + userName + "&password=" + md5Password;
    	return url;
    }
    
	public void PostJsonData(final Handler handler,String url)
	{
		final Message msg = new Message();
		finalHttp.post(url, new AjaxCallBack<Object>() {
			
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Log.i(TAG, "---------PostJsonData success---------" + t.toString());
				if(ParseUserInfoJson(t.toString()))
				{
					msg.what = 1;
				}
				else
				{
					msg.what = 0;
				}
				Log.i(TAG, "---------msg ---------" + msg.what);
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.i(TAG, "---------PostJsonData fail---------" + strMsg.toString());
				msg.what = 0;
				handler.sendMessage(msg);
			}

		});
	}
	
	public boolean ParseUserInfoJson(String userJson)
	{
		boolean success = false;
		try {
			String code = new JSONObject(userJson).getString("code");
			Log.i(TAG, "-----code------"+ code);
			if(code.contains("200"))
			{
				JSONArray jsonArray = new JSONObject(userJson).getJSONArray("data");
				
				Log.i(TAG, "-----jsonObj------"+ jsonArray.toString());
				Log.i(TAG, "-----jsonArray.length()------"+ jsonArray.length());
				
				if(jsonArray.length() != 0)
				{
					JSONObject jsonObject = (JSONObject)jsonArray.get(0);
					User user = new User();
					String userName = jsonObject.getString("username");
					String password = jsonObject.getString("password");
					String dataCenterId = jsonObject.getString("datacenterid");
					String name = jsonObject.getString("name");
					user.setName(userName);
					user.setPassword(password);
					user.setDatacenterid(dataCenterId);
					user.setName(name);
					StoreUser(user);
					Log.i(TAG,"-----StoreUser------");
					success = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG,"-----StoreUser------" + e.getMessage());
		}
		return success;
	}	

	//获取室外数据
	public void GetJsonData(final Handler handler,String url)
	{

		final Message msg = new Message();
		Log.i(TAG, "---------url---------" + url);
		finalHttp.get(url, new AjaxCallBack<Object>(){
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				
				mSharedPreferencesUtil.sharedPreferencesStringStore(context, sharedClassName, data_key, t.toString());
				
				entityDataList = ParseJsonData(t.toString());
				msg.what = 1;
				handler.sendMessage(msg);
				hasInstall();
				Log.i(TAG, "---------final http success---------");
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				msg.what = 0;
				msg.arg1 = errorNo;
				ParseJsonData(mSharedPreferencesUtil.getStringSharedPreferences(context, sharedClassName, data_key, null));
				handler.sendMessage(msg);
				Log.i(TAG, "---------final http fail---------");
			}
		});
	}
	
	//获取机房数据
	public void GetMachineJsonData(final Handler handler,String url)
	{

		final Message msg = new Message();
		Log.i(TAG, "---------url---------" + url);
		finalHttp.get(url, new AjaxCallBack<Object>(){
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				
				mSharedPreferencesUtil.sharedPreferencesStringStore(context, sharedClassName_Machine, data_key_machine, t.toString());
				
				entityDataList = ParseMachineJsonData(t.toString());
				msg.what = 1;
				handler.sendMessage(msg);
				hasInstall();
				Log.i(TAG, "---------final http success---------");
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				msg.what = 0;
				msg.arg1 = errorNo;
				ParseMachineJsonData(mSharedPreferencesUtil.getStringSharedPreferences(context, sharedClassName_Machine, data_key_machine, null));
				handler.sendMessage(msg);
				Log.i(TAG, "---------final http fail---------");
			}
		});
	}
	//获取每小时的空气指数
	public int[] getAQIPerHour(List<EntityData> entityDataList)
	{
		int[] ret = new int[8];
		int i = 0;
		if(entityDataList.size() >= 8)
		{
			for(EntityData entityData:entityDataList)
			{
				String aqi = entityData.getApi();
				if(aqi != null)
				{
					ret[i] = Integer.parseInt(aqi);
				}
				else
				{
					ret[i] = 20;
				}
				i++;
				if(i == 8)
				{
					break;
				}
			}
		}
		return ret;
	}
	//获取每小时的温度
	public int[] getTemperaturePerHour(List<EntityData> entityDataList)
	{
		int[] ret = new int[8];
		int i = 0;
		if(entityDataList.size() >= 8)
		{
			for(EntityData entityData:entityDataList)
			{
				String temp = entityData.getTemperature();
				if(temp != null)
				{
					ret[i] = Integer.parseInt(temp);
				}
				else
				{
					ret[i] = 20;
				}
				i++;
				if(i == 8)
				{
					break;
				}
			}
		}
		return ret;
	}
	//获取每小时的湿度
	public int[] getHumidityPerHour(List<EntityData> entityDataList)
	{
		Log.i(TAG, "----getHumidityPerHour size-------" + entityDataList.size());
		int[] ret = new int[8];
		int i = 0;
		if(entityDataList.size() >= 8)
		{
			for(EntityData entityData:entityDataList)
			{
				String humidity = entityData.getHumidity();
				if(humidity != null)
				{
					ret[i] = Integer.parseInt(humidity) - 40;
				}
				else
				{
					ret[i] = 20;
				}
				i++;
				if(i == 8)
				{
					break;
				}
			}
		}
		return ret;
	}
	
	//解析室外数据
	public List<EntityData> ParseJsonData(String json)
	{
		List<EntityData> entityDataList = new ArrayList<EntityData>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONArray jsonArray = jsonObj.getJSONArray("data");
			String code = jsonObj.getString("code");
			if(code.contains("200"))
			{
				Log.i(TAG, "-----jsonObj------"+ jsonArray.toString());
				Log.i(TAG, "-----jsonArray.length()------"+ jsonArray.length());
				int count = jsonArray.length();
				if(count > 0)
				{
					for(int i=0; i<count; i++)
					{
						JSONObject jsonObject = (JSONObject)jsonArray.get(i);
						EntityData entityData = new EntityData();
//						String pm25 = jsonObject.getString("pm25");
//						String pm10 = jsonObject.getString("pm10");
						String aqi = jsonObject.getString("aqi");
//						String o3 = jsonObject.getString("o3");
						String so2 = jsonObject.getString("so2");
						
//						String co = jsonObject.getString("co");
						String no2 = jsonObject.getString("no2");
						
						String testtime = jsonObject.getString("testtime");
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
						dateFormat.setLenient(false); 
						Date timeDate = dateFormat.parse(testtime);
						Log.i("TAG", "--------timeDate------" + timeDate);
						entityData.setApi(aqi);
//						entityData.setPm25(pm25);
//						entityData.setPm10(pm10);
//						entityData.setO3(o3);
						entityData.setSo2(so2);
						GX gxSo2 = GXUtil.CaculateGx("so2",so2);
						entityData.setGxSo2(gxSo2);
						Log.i("TAG", "--------gxSo2------" + gxSo2);
						
//						entityData.setCo(co);
						entityData.setNo2(no2);
						GX gxNo2 = GXUtil.CaculateGx("no2",no2);
						entityData.setGxNo2(gxNo2);
						Log.i("TAG", "--------so2------" + so2);
						Log.i("TAG", "--------no2------" + no2);
						Log.i("TAG", "--------gxNo2------" + gxNo2);
						Log.i("TAG", "--------test------");
						//判断GX的原则：就高不就低
						if(gxData != null)
						{
							Log.i("TAG", "--------gxData != null------");
							Log.i("TAG", "--------gx So2------" + gxData.GetGx(gxSo2));
							Log.i("TAG", "--------gx No2------" + gxData.GetGx(gxNo2));
						}
						GX gx = gxData.GetGx(gxSo2) > gxData.GetGx(gxNo2) ? gxSo2 : gxNo2;
						int value = gxData.GetGx(gxSo2) > gxData.GetGx(gxNo2) ? Integer.parseInt(so2) : Integer.parseInt(no2);
						
						entityData.setGx(gx);
						entityData.setTesttime(testtime);
						entityData.setGxValue(value * 22.4/64);
						entityDataList.add(entityData);
						Log.i("TAG","-----aqi--------" + aqi);
					}
				}
			}
			

		} catch (Exception e) {
			Log.i("TAG","-----exception--------" + e.getMessage());
			e.printStackTrace();
		}
		return entityDataList;
	}
	
	//解析机房数据
	public List<EntityData> ParseMachineJsonData(String json)
	{
		List<EntityData> entityDataList = new ArrayList<EntityData>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONArray jsonArray = jsonObj.getJSONArray("data");
			String code = jsonObj.getString("code");
			if(code.contains("200"))
			{
				Log.i(TAG, "-----jsonObj------"+ jsonArray.toString());
				Log.i(TAG, "-----jsonArray.length()------"+ jsonArray.length());
				int count = jsonArray.length();
				if(count > 0)
				{
					for(int i=0; i<count; i++)
					{
						JSONObject jsonObject = (JSONObject)jsonArray.get(i);
						EntityData entityData = new EntityData();
						String gxlevels = jsonObject.getString("gxlevels");
						
//						String testtime = jsonObject.getString("testtime");
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//设定格式 
						dateFormat.setLenient(false); 
//						Date timeDate = dateFormat.parse(testtime);
//						Log.i("TAG", "--------timeDate------" + timeDate);
						Log.i("TAG", "--------test------");
						GX gx = GX.G1;
						if(gxlevels.contains("G1"))
						{
							gx = GX.G1;
						}
						else if(gxlevels.contains("G2"))
						{
							gx = GX.G2;
						}
						else if(gxlevels.contains("G3"))
						{
							gx = GX.G3;
						}
						else if(gxlevels.contains("GX"))
						{
							gx = GX.GX;
						}
						entityData.setGx(gx);
//						entityData.setTesttime(testtime);
						entityDataList.add(entityData);
					}
				}
			}
			

		} catch (Exception e) {
			Log.i("TAG","-----exception--------" + e.getMessage());
			e.printStackTrace();
		}
		return entityDataList;
	}
	
	public List<EntityData> GetJsonDataFromSharedPreferences()
	{
		String jsonString = "";
		if(MainActivity.IsMachine)
		{
			jsonString = mSharedPreferencesUtil.getStringSharedPreferences(context, sharedClassName_Machine, data_key_machine, null);
			return ParseMachineJsonData(jsonString);
		}
		else
		{
			jsonString = mSharedPreferencesUtil.getStringSharedPreferences(context, sharedClassName, data_key, null);
			return ParseJsonData(jsonString);
		}
	}
	
    public boolean isFirstIntall()
    {
    	String firstTime = mSharedPreferencesUtil.getStringSharedPreferences(context, sharedClassNameMain, data_key_main, "true");
    	if(firstTime.contains("true"))
    	{
    		return true;
    	}
    	else
    	{
        	return false;
    	}
    }
    
    private void hasInstall()
    {
    	mSharedPreferencesUtil.sharedPreferencesStringStore(context, sharedClassNameMain, data_key_main, "false");
    }
	
}
