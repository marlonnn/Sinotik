package com.saray.sinotk.util;

import com.saray.sinotk.R;
import com.saray.sinotk.SettingActivity;
import com.saray.sinotk.SettingLocationActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomTitleBar {
	
	private Activity mActivity;
	
	private TextView middleText;
	private ImageView ImageLeft;
	private ImageView ImageRight;
	private ImageView ImageMiddle;

	private LinearLayout leftll;

	private LinearLayout rightll;
	
	private static final int REQ_CITYLIST=99;
	
	public CustomTitleBar()
	{
		
	}
	
	public CustomTitleBar(Activity activity)
	{
		mActivity = activity;
		TitleBarInitial();
		
	}
	
	private void TitleBarInitial()
	{
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.sinotik_main);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.sinotik_main_titlebar);
		ImageLeft = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_left);
		ImageRight = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_right);
		ImageMiddle = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_middleImage);
		middleText = (TextView) mActivity.findViewById(R.id.sinotik_titlebar_middleText);
		
		leftll = (LinearLayout)mActivity.findViewById(R.id.sinotik_titlebar_letfll);
		rightll = (LinearLayout)mActivity.findViewById(R.id.sinotik_titlebar_rightll);
		SetListener();
	}
	
	public void SetLocation(String title)
	{
		middleText.setText(title);
	}
	
	public void SetListener()
	{
		leftll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,SettingLocationActivity.class);
//				mActivity.startActivity(intent);
				mActivity.startActivityForResult(intent, REQ_CITYLIST);
			}
		});
		
		rightll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,SettingActivity.class);
				mActivity.startActivity(intent);
				
//				CustomDialog selectDialog = new CustomDialog(mActivity,R.style.dialog);//创建Dialog并设置样式主题
//				Window win = selectDialog.getWindow();
//				WindowManager.LayoutParams params = win.getAttributes();
//				win.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//				win.setGravity(Gravity.LEFT | Gravity.TOP);
////				LayoutParams params = new LayoutParams();
//				params.x = 500;//设置x坐标
//				params.y = -20;//设置y坐标
//				win.setAttributes(params);
//				
//				selectDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
//				selectDialog.show();
			}
		});
	}
	

}
