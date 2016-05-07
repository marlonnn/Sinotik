package com.saray.sinotk;
import com.saray.sinotk.util.SettingTitleBar;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity{
	
	private SettingTitleBar settingTitleBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingTitleBar = new SettingTitleBar(this);
        setContentView(R.layout.sinotik_about);
        
    }
	
	@Override
	public void onBackPressed() {
		this.finish();

	}
}
