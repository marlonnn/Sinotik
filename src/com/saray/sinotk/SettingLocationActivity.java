package com.saray.sinotk;

import java.util.List;
import java.util.Map;

import com.saray.sinotk.adapter.LocationAdapter;
import com.saray.sinotk.util.DBUtils;
import com.saray.sinotk.util.DataUtil;
import com.saray.sinotk.util.SettingLocationTitleBar;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SettingLocationActivity extends Activity implements OnClickListener,OnItemClickListener{
	
    private SettingLocationTitleBar settingTitleBar;
	private TextView airMachine;
	private TextView airOutter;
	private List<Map<String, String>> list;
	private LocationAdapter adapter;
	private ListView cityList;
	private int state = 0;
	private String city = "";
	private String id = "";
	private DBUtils dbUtils;
	private DataUtil dataUtil;
	private FrameLayout realtabcontent;
	private TextView txtview;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = 0;
        settingTitleBar = new SettingLocationTitleBar(this);
        settingTitleBar.SetLocation("设置区域");
        
        realtabcontent = (FrameLayout)this.findViewById(R.id.realtabcontent);
        
        airMachine = (TextView) this.findViewById(R.id.sinotik_air_machine);
        airOutter = (TextView) this.findViewById(R.id.sinotik_air_outter);
        
        cityList = (ListView) this.findViewById(R.id.listView_index);
        dbUtils = new DBUtils(this);
        dataUtil = new DataUtil(this);
        initialData(true);
        initialListData();
        
        txtview = new TextView(this);//定义组件  
        String name = dataUtil.GetDataCenterName();
        Log.i("setting", "----------name------------" + name);
        if(name != "")
        {
        	txtview.setText(dataUtil.GetDataCenterName());
        }
        else
        {
            txtview.setText("---");
        }
    	txtview.setGravity(Gravity.CENTER);
//    	txtview.setTextSize(30);
    	
        airMachine.setOnClickListener(this);
        airOutter.setOnClickListener(this);
        cityList.setOnItemClickListener(this);
        txtview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String centerId = dataUtil.GetDataCenterID();
				String centerName = dataUtil.GetDataCenterName();
				if(centerId != null && centerName != null)
				{
					Intent intent = new Intent();
					intent.putExtra("cityId", id);
					intent.putExtra("cityName", centerName);
					intent.putExtra("cityCode", centerId);
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});
    }
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}



	private void initialData(boolean flag)
	{
        realtabcontent.removeView(txtview);
		if(flag)
		{
			airMachine.setFocusable(false);
			airOutter.setFocusable(true);
			airMachine.setBackgroundColor(this.getResources().getColor(R.color.transparent));
			airOutter.setBackgroundColor(this.getResources().getColor(R.color.gray));
			list = dbUtils.getProvince();
			cityList.setVisibility(View.VISIBLE);
		}
		else
		{
			airMachine.setFocusable(true);
			airOutter.setFocusable(false);
			airMachine.setBackgroundColor(this.getResources().getColor(R.color.gray));
			airOutter.setBackgroundColor(this.getResources().getColor(R.color.transparent));
			cityList.setVisibility(View.GONE);
			setMachineData();
		}
	}
	
	private void setMachineData()
	{
        FrameLayout.LayoutParams tparams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,   
                ViewGroup.LayoutParams.WRAP_CONTENT);//定义显示组件参数 
        realtabcontent.addView(txtview, tparams);
	}
	
	private void initialListData()
	{
		list = dbUtils.getProvince();
		adapter = new LocationAdapter(SettingLocationActivity.this, list);
		cityList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.sinotik_air_machine:
			initialData(false);
			break;
		case R.id.sinotik_air_outter:
			initialData(true);
	        state = 0;
			initialListData();
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String temp = list.get(arg2).get("address");
		Log.i("location", "-------temp---------" + temp);
		Log.i("location", "-------state---------" + state);
		switch (state) {
		case 0: // 查询市
			getCity(temp);
			city = temp;
			state = 1;
			break;
		case 1: // 查询县（区）
			getCountry(temp);
			state = 2;
			break;
		case 2: // 查询ID并返回数据
			getId(temp);
			// 返回id数据
			Intent intent = new Intent();
			intent.putExtra("cityId", id);
			intent.putExtra("cityName", temp);
			intent.putExtra("cityCode", getCityId(temp));
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}

	}
	
	private void getProvince() {
		list.clear();
		list.addAll(dbUtils.getProvince());
		adapter.notifyDataSetChanged();
	}

	private void getCity(String province) {
		list.clear();
		list.addAll(dbUtils.getCity(province));
		adapter.notifyDataSetChanged();
	}

	private void getCountry(String city) {
		list.clear();
		list.addAll(dbUtils.getCountry(city));
		adapter.notifyDataSetChanged();
	}

	private void getId(String country) {
		id = dbUtils.getAddressId(country);
	}
	
	private String getCityId(String country) {
		return id = dbUtils.getCityCode(country);
	}
	
	@Override
	public void onBackPressed() {
		switch (state) {
		case 0:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case 1: // 显示省
			getProvince();
			state = 0;
			break;
		case 2: // 显示市
			getCity(city);
			state = 1;
			break;
		default:
			break;
		}
	}

}
