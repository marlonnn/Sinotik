package com.saray.sinotk;

import com.saray.sinotk.util.SettingTitleBar;

import android.app.Activity;
import android.os.Bundle;

public class ConnectUs extends Activity{
	private SettingTitleBar settingTitleBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingTitleBar = new SettingTitleBar(this);
        setContentView(R.layout.sinotik_connectus);
        
    }
	
	@Override
	public void onBackPressed() {
		this.finish();

	}
}
