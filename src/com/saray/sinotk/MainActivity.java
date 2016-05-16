package com.saray.sinotk;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saray.sinotik.cache.DataCache;
import com.saray.sinotik.commons.Constants;
import com.saray.sinotik.config.Config;
import com.saray.sinotik.dialog.ProgressDialog;
import com.saray.sinotk.adapter.QualityAdapter;
import com.saray.sinotk.entity.InnerStationData;
import com.saray.sinotk.entity.OutStation;
import com.saray.sinotk.entity.OutStationData;
import com.saray.sinotk.util.CustomTitleBar;
import com.saray.sinotk.util.DataUtil;
import com.saray.sinotk.util.GXData;
import com.saray.sinotk.util.GXUtil;
import com.saray.sinotk.util.NetworkUtil;
import com.saray.sinotk.util.UrlUtil;
import com.saray.sinotk.util.WriteToSD;
import com.saray.sinotk.util.GXData.GX;
import com.saray.sinotk.widget.ChartView;
import com.summer.activity.BaseActivity;
import com.summer.dialog.CustomProgressDialog;
import com.summer.factory.ThreadPoolFactory;
import com.summer.handler.InfoHandler;
import com.summer.handler.InfoHandler.InfoReceiver;
import com.summer.json.Entity;
import com.summer.logger.XLog;
import com.summer.task.HttpBaseTask;
import com.summer.treadpool.ThreadPoolConst;
import com.summer.utils.JsonUtil;
import com.summer.utils.ToastUtil;

public class MainActivity extends BaseActivity implements OnClickListener, OnItemClickListener,AMapLocationListener, LocationSource{
	
	private static final String TAG = "main";
	
	public ImageView imageView;
	
	public CustomTitleBar customTitleBar;
	public QualityAdapter qualityAdapter;
	
	private final int darkGray = 0x00ff0000;
	private static final int REQ_CITYLIST=99;
	
	private LinearLayout titleBarLinearlayout;
	
	private DataUtil dataUtil;
	private String cityCode;
	private TextView aqi;
	private TextView gx;
	private TextView quality;
	private ProgressDialog mProgressDialog;
	
	private RelativeLayout chartView;
	private ChartView chartViewBar;
	
	private AMap aMap;
	private MapView mapView;
	
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private InfoReceiver infoReceiver;
	
	public static boolean IsMachine = false;
	
	private Gson gson;
	
	private List<OutStation> stationList;
	
	private int currentPosion = 0;
	
	private TextView name, stationName;
	
	private ImageButton previous, next;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WriteToSD(this);//�������б����ݿ�д�뵽SD��
        customTitleBar = new CustomTitleBar(this);
        customTitleBar.SetLocation("---");
        gson = new Gson();
        titleBarLinearlayout = (LinearLayout) this.findViewById(R.id.sinotik_titlebar_linearlayout);
        
        aqi = (TextView)this.findViewById(R.id.sinotik_aqi);
        gx = (TextView)this.findViewById(R.id.sinotik_gx);
        quality =(TextView)this.findViewById(R.id.sinotik_quality);
        
        chartView = (RelativeLayout)findViewById(R.id.chartview);
		chartViewBar = new ChartView();
		
		previous = (ImageButton)findViewById(R.id.previous);
		next = (ImageButton)findViewById(R.id.next);
		previous.setOnClickListener(this);
		next.setOnClickListener(this);
		name = (TextView)findViewById(R.id.name);
		stationName = (TextView)findViewById(R.id.stationName);
        if(NetworkUtil.isNetworkAvailable(getApplicationContext()))
        {
    		mapView = (MapView) findViewById(R.id.map);
    		mapView.onCreate(savedInstanceState);// �˷���������д
    		init();
        }
        else
        {
        	//��ʾ�Ի���
        	checkNetworkDialog();
        }
        
        titleBarLinearlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ע�������λʱ�����Ҫ�Ƚ���λ����ɾ�����ٽ��ж�λ����
				Log.i(TAG, "----------txtLocation click------------");
			}
		});
        
        dataUtil = new DataUtil(this);
		initializeButton();
		

	
		btnPerday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackgroundColor(darkGray);
				btnPerday.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				btnPermounth.setBackgroundColor(darkGray);
				if(IsMachine)
				{
					sendInnerStationRequest(cityCode);
				}
				else
				{
					sendOutStationRequest(cityCode, DataCache.getInstance().getOutStation().get(currentPosion).getStationName(), "1");
				}
			}
		});
		
		btnPerWeek.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				btnPerday.setBackgroundColor(darkGray);
				btnPermounth.setBackgroundColor(darkGray);
				if(IsMachine)
				{
					sendInnerStationRequest(cityCode);
				}
				else
				{
					sendOutStationRequest(cityCode, DataCache.getInstance().getOutStation().get(currentPosion).getStationName(), "7");
				}
			}
		});
		
		btnPermounth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackgroundColor(darkGray);
				btnPerday.setBackgroundColor(darkGray);
				btnPermounth.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				if(IsMachine)
				{
					sendInnerStationRequest(cityCode);
				}
				else
				{
					sendOutStationRequest(cityCode, DataCache.getInstance().getOutStation().get(currentPosion).getStationName(), "30");
				}
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
		                    XLog.e(e);
		                    e.printStackTrace();
		                    RequestFailed(-1, "Json Parse Error", -1);
		                }
		            }
		        }
			}
		};
    }
    
	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
	   // aMap.setMyLocationType()
	}
    
    private void checkNetworkDialog()
    {
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
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
				MainActivity.this.finish();
			}
		});
    	builder.create().show();
    }
    
    //��ʾ������
    private void showProgressBar()
    {
    	if(mProgressDialog == null)
    	{
        	mProgressDialog = new ProgressDialog(this,"���������ʼ�����ݣ����Ե�......");
        	mProgressDialog.setCanceledOnTouchOutside(false);
        	mProgressDialog.show();
    	}
    	else
    	{
        	mProgressDialog.setCanceledOnTouchOutside(false);
        	mProgressDialog.show();
    	}

    }
    
    //��������Ϣhandler
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {   
            switch (msg.what) {
			case -1:
				//������󣬻����쳣
				break;
			case 0:
				//��������ʧ��
				break;
			case 1:
				//�������سɹ�,��ʾ����
				Log.i(TAG, "-----mHandler 1-------");
//				cancelProgressBar();
//				updateIndexChartView(true);
				initializeButton();
				break;
			case 2:
				//��λ��������ʼ��������
				Log.i(TAG, "-----mHandler 2-------");
				//ÿ�λ�ȡһ���µ�����
//				getJsonData(cityCode, "1");
				break;
			case 3:
				//��λʧ�ܣ���ʼ��������
				Log.i(TAG, "-----mHandler 3-------");
				Toast.makeText(MainActivity.this,"��λʧ�ܣ�", Toast.LENGTH_LONG).show();
				break;
			case 4:
				//��һ�ΰ�װ
				Log.i(TAG, "-----mHandler 4-------");
				showProgressBar();
				getJsonData(cityCode, "5");
				break;
			case 5:
				//�����������
				Log.i(TAG, "-----mHandler 5-------");
				showProgressBar();
				getMachineJsonData(cityCode, "5");
			break;
            }   
            super.handleMessage(msg);
       }   
    };

	private Button btnPerWeek;

	private Button btnPerday;

	private Button btnPermounth;
    
    private void getJsonData(String cityCode, String days)
    {
        Log.i(TAG, "----------getJsonData start---------");
		String URL = "http://115.159.44.146/ebdpmp/Api/data.php?action=allOutDoorDataListOfCity&cityCode=" + cityCode + "&cycle=" + days;
        dataUtil.GetJsonData(mHandler,URL);
        Log.i(TAG, "----------getJsonData end---------");
    }
    
    private void getMachineJsonData(String cityCode, String days)
    {
        Log.i(TAG, "----------getJsonData start---------");
		String URL = "http://115.159.44.146/ebdpmp/Api/data.php?action=dataCenterDataList&userid=" + cityCode;
        dataUtil.GetMachineJsonData(mHandler,URL);
        Log.i(TAG, "----------getJsonData end---------");
    }
    
    //���¿���ָ����ͼ��
    private void updateIndexChartViewByOuter()
    {
    	GXData gxData = new GXData(this);
    	if(DataCache.getInstance().getOutData() != null && DataCache.getInstance().getOutData().size() > 0)
    	{
    		String aqiText = DataCache.getInstance().getOutData().get(0).getAqi();
    		if(aqiText != null)
    		{
    			String qualityString = getQuality(aqi, quality, Integer.parseInt(aqiText));
        		aqi.setText(aqiText);
        		quality.setText(qualityString);
    		}
    		else
    		{
        		aqi.setText("---");
        		quality.setText("---");
    		}
		    GX gxSo2 = GXUtil.CaculateGx("so2", DataCache.getInstance().getOutData().get(0).getSo2());
		    GX gxNo2 = GXUtil.CaculateGx("no2", DataCache.getInstance().getOutData().get(0).getNo2());
			GX gxValue = gxData.GetGx(gxSo2) > gxData.GetGx(gxNo2) ? gxSo2 : gxNo2;
    		gx.setText(gxData.GetGxString(gxValue));
    	}
    	else
    	{
    		//û�и���������
    		aqi.setText("---");
    		quality.setText("---");
    		gx.setText("--");
    		Toast.makeText(MainActivity.this,"û�и��������ˣ�", Toast.LENGTH_LONG).show();
    	}
    }
    
    //���¿���ָ����ͼ��
    private void updateIndexChartViewByInner()
    {
    	if (DataCache.getInstance().getInnerData() != null && DataCache.getInstance().getInnerData().size() > 0)
    	{
    		aqi.setText("---");
    		quality.setText("---");
    		gx.setText(DataCache.getInstance().getInnerData().get(0).getGxlevels());
    	}
    	else
    	{
    		//û�и���������
    		aqi.setText("---");
    		quality.setText("---");
    		gx.setText("--");
    		Toast.makeText(MainActivity.this,"û�и��������ˣ�", Toast.LENGTH_LONG).show();
    	}
    }
    
    private String getQuality(TextView txtAqi, TextView txtQuality, int aqi)
    {
    	String quality = "";
    	if(aqi >= 0 && aqi <= 50)
    	{
    		quality = "��";
    		txtAqi.setTextColor(Color.parseColor("#00FF00"));
    		txtQuality.setTextColor(Color.parseColor("#00FF00"));
    	}
    	else if(aqi >= 51 && aqi <= 100)
    	{
    		quality = "��";
    		txtAqi.setTextColor(Color.parseColor("#80FF00"));
    		txtQuality.setTextColor(Color.parseColor("#80FF00"));
    	}
    	else if(aqi >= 101 && aqi <= 150)
    	{
    		quality = "�����Ⱦ";
    		txtAqi.setTextColor(Color.parseColor("#FFFF00"));
    		txtQuality.setTextColor(Color.parseColor("#FFFF00"));
    	}
    	else if(aqi >= 151 && aqi <= 200)
    	{
    		quality = "�ж���Ⱦ";
    		txtAqi.setTextColor(Color.parseColor("#FF8000"));
    		txtQuality.setTextColor(Color.parseColor("#FF8000"));
    	}
    	else if(aqi >= 201 && aqi <= 300)
    	{
    		quality = "�ض���Ⱦ";
    		txtAqi.setTextColor(Color.parseColor("#FF00FF"));
    		txtQuality.setTextColor(Color.parseColor("#FF00FF"));
    	}
    	else if(aqi >= 301)
    	{
    		quality = "������Ⱦ";
    		txtAqi.setTextColor(Color.parseColor("#C11E1E"));
    		txtQuality.setTextColor(Color.parseColor("#C11E1E"));
    	}
    	return quality;
    }
    
	@Override
	protected void onStart() {
		super.onStart();	
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(!NetworkUtil.isNetworkAvailable(getApplicationContext()))
		{
			checkNetworkDialog();
		}
		mapView.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		removeUpdate(mLocationManagerProxy, this);
		mapView.onPause();
		deactivate();
	}
	
	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

    @Override
	protected void onDestroy() {
		super.onDestroy();
//		removeUpdate(mLocationManagerProxy, this);
		mapView.onDestroy();
	}
    
    private void initializeButton()
    {
		btnPerWeek = (Button)MainActivity.this.findViewById(R.id.sinotik_btnperweek);
		btnPerday = (Button)MainActivity.this.findViewById(R.id.sinotik_btnperday);
		btnPermounth = (Button)MainActivity.this.findViewById(R.id.sinotik_btnpermounth);
		
		
		btnPerWeek.setBackgroundColor(darkGray);
		btnPerday.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
		btnPermounth.setBackgroundColor(darkGray);
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
	
	//Activity��ת�ص�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//resultCode : RESULT_OK=-1
		//requestCode :�Լ������
		if(resultCode==RESULT_OK){
			if(requestCode==REQ_CITYLIST){

				String cityName = data.getExtras().get("cityName").toString();
				
				String cityId = data.getExtras().get("cityId").toString();
				String code = data.getExtras().get("cityCode").toString();
				if(code != null)
				{
					Log.i(TAG, "----------city name------------" + cityName);
					Log.i(TAG, "----------city Id------------" + cityId);
					Log.i(TAG, "----------city code------------" + code);
					customTitleBar.SetLocation(cityName);	
					cityCode = code;
					if(code.length() > 3)
					{
						IsMachine = false;
//						sendMessage(mHandler, Constants.FIRST_INTSALL);
						sendStationListRequest(cityCode);
					}
					else
					{
						IsMachine = true;
//						sendMessage(mHandler, 5);
						sendInnerStationRequest(cityCode);
					}
					
				}
				else
				{
					Toast.makeText(MainActivity.this,"���е�ַ����Ϊ�գ�����³������ݿ⣡", Toast.LENGTH_LONG).show();
				}

			}
		}
	}

	public void onLocationChanged(AMapLocation amapLocation) {
		customTitleBar.SetLocation(amapLocation.getDistrict());
		
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				cityCode = amapLocation.getAdCode();
				Log.i(TAG, "----------location city code------------" + amapLocation.getCityCode());
				if(cityCode != "")
				{
//					sendMessage(mHandler, Constants.FIRST_INTSALL);
					sendStationListRequest(cityCode);
				}
				else
				{
					sendMessage(mHandler, Constants.LOCATEFAIL);
				}
				mlocationClient.stopLocation();
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
		
	}
	
	private void sendMessage(Handler mHandler, int what)
	{
		if(mHandler != null)
		{
			final Message msg = new Message();
			msg.what = what;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//���ö�λ����
			mlocationClient.setLocationListener(this);
			//����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			
			mLocationOption.setWifiActiveScan(true);
			//���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mlocationClient.startLocation();
		}
		
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
		
	}
	
	/**
	 * ��ȡ���еļ���
	 * @param cityCode
	 */
    private void sendStationListRequest(String cityCode)
    {
    	HashMap<String, String> entity = new HashMap<String, String>();
    	entity.put("cityCode", cityCode);
    	List<NameValuePair> params = JsonUtil.requestForNameValuePair(entity);
    	ShowProgressDialog("���ڻ�ȡ��������...");
    	addToThreadPool(Config.staion_list, "send station list request", params);
    }
    
    private void sendOutStationRequest(String cityCode, String stationName, String cycle)
    {
    	HashMap<String, String> entity = new HashMap<String, String>();
    	entity.put("cityCode", cityCode);
    	entity.put("cycle", cycle);
    	entity.put("stationname", stationName);
    	List<NameValuePair> params = JsonUtil.requestForNameValuePair(entity);
    	ShowProgressDialog("���ڻ�ȡ��������...");
    	addToThreadPool(Config.all_out_door, "send all out stationrequest", params);
    }
    
    private void sendInnerStationRequest(String cityCode)
    {
    	HashMap<String, String> entity = new HashMap<String, String>();
    	entity.put("userid", cityCode);
    	List<NameValuePair> params = JsonUtil.requestForNameValuePair(entity);
    	ShowProgressDialog("���ڻ�ȡ��������...");
    	addToThreadPool(Config.all_inner, "send all out stationrequest", params);
    }
    
    private void addToThreadPool(int taskType, String Tag, List<NameValuePair> params)
    {
    	HttpBaseTask httpTask = new HttpBaseTask(ThreadPoolConst.THREAD_TYPE_FILE_HTTP, Tag, params, UrlUtil.GetUrl(taskType));
    	httpTask.setTaskType(taskType);
    	InfoHandler handler = new InfoHandler(infoReceiver);
    	httpTask.setInfoHandler(handler);
    	ThreadPoolFactory.getThreadPoolManager().addTask(httpTask);
    }
    
	/**
	 * Show custom dialog progress
	 * @param title
	 */
	protected void ShowProgressDialog(String title) {
		try {
			if (progressDialog != null) 
			{
				return;
			}
			progressDialog = CustomProgressDialog.CreateDialog(this);
			progressDialog.setMessage(title);
			progressDialog.setCancelable(false);
			progressDialog.show();

			progressDialog.setOnCancelListener(
					new DialogInterface.OnCancelListener() 
					{
						@Override
						public void onCancel(DialogInterface arg0) 
						{
							if (progressDialog != null) {
								progressDialog.dismiss();
								progressDialog = null;
							}
						}
					});
		} 
		catch (Exception e) 
		{
			XLog.e("showProgressDialog exception: " + e.toString(), e);
			e.printStackTrace();
		}
	}
	
	private void setViewVisiable(boolean visiable)
	{
		if(visiable)
		{
			name.setVisibility(View.VISIBLE);
			stationName.setVisibility(View.VISIBLE);
			previous.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
		}
		else
		{
			name.setVisibility(View.INVISIBLE);
			stationName.setVisibility(View.INVISIBLE);
			previous.setVisibility(View.INVISIBLE);
			next.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void RequestSuccessful(String jsonString, int taskType) {
		XLog.i("jsonString: " + jsonString);
		switch(taskType)
		{
		/**
		 * ��ȡ�����б�
		 */
		case Config.staion_list:
			Entity<List<OutStation>> entity = gson.fromJson(jsonString,
					new TypeToken<Entity<List<OutStation>>>() {
					}.getType());
			
			if (entity != null && entity.getData() != null)
			{
				setViewVisiable(true);
				DataCache.getInstance().setOutStation(entity.getData());
				stationList = DataCache.getInstance().getOutStation();
				currentPosion = 0;
				stationName.setText(stationList.get(currentPosion).getStationName());
				sendOutStationRequest(cityCode, DataCache.getInstance().getOutStation().get(0).getStationName(), "1");
			}
			break;
		/**
		 * �������м�������
		 */
		case Config.all_out_door:
			Entity<List<OutStationData>> outStation = gson.fromJson(jsonString,
					new TypeToken<Entity<List<OutStationData>>>() {
					}.getType());
			if (outStation != null && outStation.getData() != null)
			{
				DataCache.getInstance().setOutData(outStation.getData());
				XLog.i("-----test---- " + DataCache.getInstance().getSelectDayData().size());
				chartView.removeAllViews();
				chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
						DataCache.getInstance().getOutData()));
				updateIndexChartViewByOuter();
			}
			break;
			
		case Config.all_inner:
			Entity<List<InnerStationData>> innerStation = gson.fromJson(jsonString,
					new TypeToken<Entity<List<InnerStationData>>>() {
					}.getType());
			if (innerStation != null && innerStation.getData() != null)
			{
				setViewVisiable(false);
				DataCache.getInstance().setInnerData(innerStation.getData());
				chartView.removeAllViews();
				chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
						DataCache.getInstance().getInnerData(), true));
				updateIndexChartViewByInner();
			}
			break;
			
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId())
		{
		case R.id.previous:
			if (stationList != null && stationList.size() >0)
			{
				if ( currentPosion <= stationList.size() && currentPosion > 0)
				{
					currentPosion --;
					if (currentPosion < 0)
					{
						currentPosion = 0;
					}
					stationName.setText(stationList.get(currentPosion).getStationName());
					sendOutStationRequest(cityCode, stationList.get(currentPosion).getStationName(), "1");
				}
				else
				{
					ToastUtil.show(this, "û�и��������");
				}
			}
			break;
			
		case R.id.next:
			if (stationList != null && stationList.size() >0)
			{
				if ( currentPosion < stationList.size() -1)
				{
					currentPosion ++;
					stationName.setText(stationList.get(currentPosion).getStationName());
					sendOutStationRequest(cityCode, stationList.get(currentPosion).getStationName(), "1");
				}
				else
				{
					ToastUtil.show(this, "û�и��������");
				}
			}

			break;
		}
		
	}
}
