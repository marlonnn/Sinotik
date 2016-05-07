package com.saray.sinotk;

import com.saray.sinotik.dialog.ProgressDialog;
import com.saray.sinotk.R;
import com.saray.sinotk.util.DataUtil;
import com.saray.sinotk.util.NetworkUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("WorldReadableFiles") 
public class LoginActivity extends Activity {
	
	protected static final String TAG = "login";
	private EditText userName, password;
	private CheckBox remberPasswordCkBox, autoLoginCkbox;
	private Button btnLogin;
	private ImageButton btnQuit;
	private SharedPreferences sp;
    private String userNameValue,passwordValue;
	private ProgressDialog mProgressDialog;
	private DataUtil dataUtil;
	
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
        	cancelProgressBar();
            switch (msg.what) {
            case 0:
            	//登录失败，用户名或者账号错误
	    		Toast.makeText(LoginActivity.this,"登录失败，用户名或者密码错误，请重新登录！", Toast.LENGTH_SHORT).show();
            	break;
            case 1:
            	//登录成功
            	dataUtil.HasInstalled();
            	Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
            	//跳转到其他页面
            	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            	LoginActivity.this.startActivity(intent);
            	break;
            }
            super.handleMessage(msg);   
       }   
    };
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sinotik_login);
		dataUtil = new DataUtil(this);
        //获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
		remberPasswordCkBox = (CheckBox) findViewById(R.id.cb_mima);
		autoLoginCkbox = (CheckBox) findViewById(R.id.cb_auto);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnQuit = (ImageButton)findViewById(R.id.img_btn);
		
		//判断记住密码多选框的状态
		if(dataUtil.IsRememberPassword())
		{
			//设置默认是记录密码状态
			remberPasswordCkBox.setChecked(true);
			userName.setText(dataUtil.GetUserNameSelf());
			password.setText(dataUtil.GetPasswordSelf());
			//判断自动登陆多选框状态
			if(sp.getBoolean("AUTO_ISCHECK", false))
			{
				//设置默认是自动登录状态
				autoLoginCkbox.setChecked(true);
				//1.检查用户名和密码
				//2.跳转界面
//				Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
//				LoginActivity.this.startActivity(intent);
			}
		}
		
		
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();
			    dataUtil.StoreUserSelf(userNameValue, passwordValue);
			    //检查网络状态
			    if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
			    {
		        	//显示对话框
		        	checkNetworkDialog();
			    }
			    else
			    {
				    startLogin();
			    }
			}
		});
		
	    //监听记住密码多选框按钮事件
		remberPasswordCkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (remberPasswordCkBox.isChecked()) {
                    
					dataUtil.RememberPassword(true);
					
				}else {
					dataUtil.RememberPassword(false);
					
				}

			}
		});
		
		//监听自动登录多选框事件
		autoLoginCkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (autoLoginCkbox.isChecked()) {
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		
		btnQuit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
    private void checkNetworkDialog()
    {
    	AlertDialog.Builder builder = new Builder(LoginActivity.this);
    	builder.setMessage("网络未连接，请检查网络状态，确认设置网络吗?");
    	builder.setTitle("提示");
    	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS"); 
				startActivity(wifiSettingsIntent); 
				
			}
		});
    	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				LoginActivity.this.finish();
			}
		});
    	builder.create().show();
    }
	
	private void startLogin()
	{
		showProgressBar();
		String url = dataUtil.GetFormatLoginString(userNameValue, passwordValue);
		Log.i(TAG,"------------login uri---------" + url);
		dataUtil.PostJsonData(mHandler,url);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
		{
			checkNetworkDialog();
		}
		
	}
	
	//显示进度条
    private void showProgressBar()
    {
    	mProgressDialog = new ProgressDialog(this,"正在登录，请稍等......");
    	mProgressDialog.setCanceledOnTouchOutside(false);
    	mProgressDialog.show();
    }
    
    //取消显示进度条
    private void cancelProgressBar()
    {
    	mProgressDialog.dismiss();
    }

}
