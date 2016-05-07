package com.saray.sinotk.util;

import com.saray.sinotk.R;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailTitleBar {

	private Activity mActivity;
	
	private TextView middleText;
	private ImageView ImageLeft;
	private ImageView ImageRight;
	
	public DetailTitleBar()
	{
		
	}
	
	public DetailTitleBar(Activity activity)
	{
		mActivity = activity;
		TitleBarInitial();
	}
	
	private void TitleBarInitial()
	{
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.sinotik_detail_trend);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.sinotik_detail_titlebar);
		ImageLeft = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_left);
		ImageRight = (ImageView) mActivity.findViewById(R.id.sinotik_titlebar_right);
		middleText = (TextView) mActivity.findViewById(R.id.sinotik_titlebar_middleText);
//		SetListener();
	}
	
	public void SetTitle(String title)
	{
		middleText.setText(title);
	}
	
	public void SetListener()
	{
		ImageLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(mActivity,SettingLocationActivity.class);
//				mActivity.startActivity(intent);
			}
		});
		
		ImageRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
