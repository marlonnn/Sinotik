package com.saray.sinotk.util;

import com.saray.sinotk.R;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingLocationTitleBar {
	
	private Activity mActivity;
	private TextView middleText;
	private ImageView ImageLeft;
	private ImageView ImageRight;
	
	public SettingLocationTitleBar()
	{
		
	}
	
	public SettingLocationTitleBar(Activity activity)
	{
		mActivity = activity;
		TitleBarInitial();
	}
	
	private void TitleBarInitial()
	{
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.sinotik_setting_location);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.sinotik_setting_location_titlebar);
		ImageLeft = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_left);
		ImageRight = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_right);
		middleText = (TextView) mActivity.findViewById(R.id.sinotik_titlebar_middleText);
		SetListener();
	}
	
	public void SetLocation(String title)
	{
		middleText.setText(title);
	}
	
	public void SetListener()
	{
		ImageLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,  
//                        KeyEvent.KEYCODE_BACK);  
//                mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
                mActivity.finish();
			}
		});
		
		ImageRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
