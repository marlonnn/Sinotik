package com.saray.sinotk.util;

import com.saray.sinotk.R;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingTitleBar {
	
	private Activity mActivity;
	private LinearLayout Left;
	
	public SettingTitleBar()
	{
		
	}
	
	public SettingTitleBar(Activity activity)
	{
		mActivity = activity;
		TitleBarInitial();
	}
	
	private void TitleBarInitial()
	{
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.sinotik_setting);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.sinotik_setting_titlebar);
		Left = (LinearLayout) mActivity.findViewById(R.id.sinotik_setting_titlebar_letfll);
		SetListener();
	}
	
	public void SetListener()
	{
		Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                mActivity.finish();
			}
		});
	}

}
