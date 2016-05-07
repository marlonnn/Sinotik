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
            	//��¼ʧ�ܣ��û��������˺Ŵ���
	    		Toast.makeText(LoginActivity.this,"��¼ʧ�ܣ��û�������������������µ�¼��", Toast.LENGTH_SHORT).show();
            	break;
            case 1:
            	//��¼�ɹ�
            	dataUtil.HasInstalled();
            	Toast.makeText(LoginActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();
            	//��ת������ҳ��
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
        //���ʵ������
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
		remberPasswordCkBox = (CheckBox) findViewById(R.id.cb_mima);
		autoLoginCkbox = (CheckBox) findViewById(R.id.cb_auto);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnQuit = (ImageButton)findViewById(R.id.img_btn);
		
		//�жϼ�ס�����ѡ���״̬
		if(dataUtil.IsRememberPassword())
		{
			//����Ĭ���Ǽ�¼����״̬
			remberPasswordCkBox.setChecked(true);
			userName.setText(dataUtil.GetUserNameSelf());
			password.setText(dataUtil.GetPasswordSelf());
			//�ж��Զ���½��ѡ��״̬
			if(sp.getBoolean("AUTO_ISCHECK", false))
			{
				//����Ĭ�����Զ���¼״̬
				autoLoginCkbox.setChecked(true);
				//1.����û���������
				//2.��ת����
//				Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
//				LoginActivity.this.startActivity(intent);
			}
		}
		
		
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();
			    dataUtil.StoreUserSelf(userNameValue, passwordValue);
			    //�������״̬
			    if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
			    {
		        	//��ʾ�Ի���
		        	checkNetworkDialog();
			    }
			    else
			    {
				    startLogin();
			    }
			}
		});
		
	    //������ס�����ѡ��ť�¼�
		remberPasswordCkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (remberPasswordCkBox.isChecked()) {
                    
					dataUtil.RememberPassword(true);
					
				}else {
					dataUtil.RememberPassword(false);
					
				}

			}
		});
		
		//�����Զ���¼��ѡ���¼�
		autoLoginCkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (autoLoginCkbox.isChecked()) {
					System.out.println("�Զ���¼��ѡ��");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("�Զ���¼û��ѡ��");
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
    	builder.setMessage("����δ���ӣ���������״̬��ȷ������������?");
    	builder.setTitle("��ʾ");
    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS"); 
				startActivity(wifiSettingsIntent); 
				
			}
		});
    	builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
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
	
	//��ʾ������
    private void showProgressBar()
    {
    	mProgressDialog = new ProgressDialog(this,"���ڵ�¼�����Ե�......");
    	mProgressDialog.setCanceledOnTouchOutside(false);
    	mProgressDialog.show();
    }
    
    //ȡ����ʾ������
    private void cancelProgressBar()
    {
    	mProgressDialog.dismiss();
    }

}
