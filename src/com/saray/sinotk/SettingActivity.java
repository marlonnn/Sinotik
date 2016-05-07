package com.saray.sinotk;

import java.util.ArrayList;
import java.util.List;

import com.saray.sinotk.adapter.SettingAdapter;
import com.saray.sinotk.util.SettingTitleBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SettingActivity extends Activity implements OnClickListener,OnItemClickListener{
	

	
	private SettingTitleBar settingTitleBar;
	private ListView settingList;
	private SettingAdapter settingAdapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingTitleBar = new SettingTitleBar(this);
        
        settingList = (ListView) this.findViewById(R.id.listView_setting);
        settingAdapter = new SettingAdapter(this, initialData());
        settingList.setAdapter(settingAdapter);
        settingList.setOnItemClickListener(this);
        

    }
	
	private List<String> initialData()
	{
		String item = null;
		List<String> list = new ArrayList<String>();
		for(int i=0; i<3; i++)
		{
			switch(i)
			{
			case 0:
				item = "关于产品";
				break;
			case 1:
				item = "使用帮助";
				break;
			case 2:
				item = "联系我们";
				break;
			}
			list.add(item);
		}
		
		return list;
	}



	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch(arg2)
		{
		case 0:
			//"关于产品";
			Intent intent1 = new Intent(SettingActivity.this, About.class);
			startActivity(intent1);
			Log.i("setting", "---------intent1-------");
			break;
		case 1:
			//"使用帮助";
			Intent intent2 = new Intent(SettingActivity.this, ConnectUs.class);
			startActivity(intent2);
			Log.i("setting", "---------intent2-------");
			break;
		case 2:
			//"联系我们";
			Intent intent3 = new Intent(SettingActivity.this, ConnectUs.class);
			startActivity(intent3);
			Log.i("setting", "---------intent3-------");
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
		this.finish();

	}

}
