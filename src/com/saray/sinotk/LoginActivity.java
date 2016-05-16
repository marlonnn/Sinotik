package com.saray.sinotk;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saray.sinotik.config.Config;
import com.saray.sinotk.R;
import com.saray.sinotk.entity.User;
import com.saray.sinotk.util.NetworkUtil;
import com.saray.sinotk.util.UrlUtil;
import com.summer.activity.BaseActivity;
import com.summer.factory.ThreadPoolFactory;
import com.summer.handler.InfoHandler;
import com.summer.handler.InfoHandler.InfoReceiver;
import com.summer.json.Entity;
import com.summer.logger.XLog;
import com.summer.task.HttpBaseTask;
import com.summer.treadpool.ThreadPoolConst;
import com.summer.utils.JsonUtil;
import com.summer.utils.MD5;
import com.summer.utils.StringUtil;
import com.summer.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("WorldReadableFiles") 
public class LoginActivity extends BaseActivity {
	
	protected static final String TAG = "login";
	private EditText userName, password;
	private CheckBox remberPasswordCkBox;
	private Button btnLogin;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
	private InfoReceiver infoReceiver;
	
	private Gson gson;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sinotik_login);
		
		Config.setFileDir(getApplicationInfo().dataDir);
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
		remberPasswordCkBox = (CheckBox) findViewById(R.id.cb_mima);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnQuit = (ImageButton)findViewById(R.id.img_btn);
		gson = new Gson();
		Config.LoadConfig();
		
		if (Config.getUsername() != null && !Config.getUsername().isEmpty())
		{
			userName.setText(Config.getUsername());
		}
		if (Config.getPassword() != null && !Config.getPassword().isEmpty())
		{
			password.setText(Config.getPassword());
			//设置默认是记录密码状态
			remberPasswordCkBox.setChecked(true);
		}	
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();
			    //检查网络状态
			    if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
			    {
		        	//显示对话框
		        	checkNetworkDialog();
			    }
			    else
			    {
				    sendLoginRequest();
			    }
			}
		});
		
		btnQuit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		infoReceiver = new InfoReceiver() {
			
			@Override
			public void onNotifyText(String notify) {
				
			}
			
			@Override
			public void onInfoReceived(int errorCode, HashMap<String, Object> items) {
				RemoveProgressDialog();
		        if (errorCode == 0)
		        {
		            XLog.i(errorCode);
		            XLog.i("items : " + items.toString());
		            String jsonString = (String) items.get("content");
		            if (jsonString != null)
		            {
		                JSONObject object;
		                try {
		                    object = new JSONObject(jsonString);
		                    String msg = object.optString("message");
		                    int code = object.optInt("code", -1);
		                    int taskType = (Integer) items.get("taskType");
		                    if (code == 200)
		                    {
		                        RequestSuccessful(jsonString, taskType);
		                    }
		                    else
		                    {
		                        RequestFailed(code, msg, taskType);
		                    }
		                } catch (JSONException e) {
		                    //parse error
		                    XLog.e(e);
		                    e.printStackTrace();
		                    RequestFailed(-1, "Json Parse Error", -1);
		                }
		            }
		        }
			}
		};
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
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
		{
			checkNetworkDialog();
		}
		
	}

    private void sendLoginRequest()
    {
    	userNameValue = userName.getText().toString().trim();
    	passwordValue = password.getText().toString().trim();
    	if (StringUtil.empty(userNameValue)) 
    	{
    		ToastUtil.show(this, "用户名不能空");
    		return;
    	}
    	if (StringUtil.empty(passwordValue)) 
    	{
    		ToastUtil.show(this, "密码不能空");
    		return;
    	}
    	HashMap<String, String> entity = new HashMap<String, String>();
    	entity.put("username", userNameValue);
    	entity.put("password", MD5.getInstance().getMd5(passwordValue));
    	List<NameValuePair> params = JsonUtil.requestForNameValuePair(entity);
    	ShowProgressDialog("正在登录，请稍等...");
    	addToThreadPool(Config.LOGIN_TYPE, "send login request", params);
    }
    
    private void addToThreadPool(int taskType, String Tag, List<NameValuePair> params)
    {
    	HttpBaseTask httpTask = new HttpBaseTask(ThreadPoolConst.THREAD_TYPE_FILE_HTTP, Tag, params, UrlUtil.GetUrl(taskType));
    	httpTask.setTaskType(taskType);
    	InfoHandler handler = new InfoHandler(infoReceiver);
    	httpTask.setInfoHandler(handler);
    	ThreadPoolFactory.getThreadPoolManager().addTask(httpTask);
    }
    
	@Override
	public void RequestSuccessful(String jsonString, int taskType) {
		switch(taskType)
		{
		case Config.LOGIN_TYPE:
			XLog.i("jsonString: " + jsonString);
			Entity<List<User>> entity = gson.fromJson(jsonString,
					new TypeToken<Entity<List<User>>>() {
					}.getType());
			
			if (entity != null && entity.getData() != null)
			{
				Config.user = entity.getData().get(0);
            	Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
            	if (remberPasswordCkBox.isChecked())
            	{
                	Config.setUsername(userNameValue);
                	Config.setPassword(passwordValue);
                	Config.saveConfig();
            	}
            	else
            	{
            		Config.clearPassword();
            	}

            	//跳转到其他页面
            	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            	startActivity(intent);
			}
			break;
			
		}
	}

}
