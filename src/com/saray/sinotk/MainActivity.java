package com.saray.sinotk;

import java.util.List;

import android.app.Activity;
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
import com.saray.sinotik.commons.Constants;
import com.saray.sinotik.dialog.ProgressDialog;
import com.saray.sinotk.adapter.QualityAdapter;
import com.saray.sinotk.entity.EntityData;
import com.saray.sinotk.util.CustomTitleBar;
import com.saray.sinotk.util.DataUtil;
import com.saray.sinotk.util.NetworkUtil;
import com.saray.sinotk.util.WriteToSD;
import com.saray.sinotk.widget.ChartView;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;


public class MainActivity extends Activity implements OnItemClickListener,AMapLocationListener, LocationSource{
	
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
	
	private List<EntityData> dataEntityList;
	
	private RelativeLayout chartView;
	private ChartView chartViewBar;
	
	private AMap aMap;
	private MapView mapView;
	
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	
	public static boolean IsMachine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WriteToSD(this);//�������б����ݿ�д�뵽SD��
        customTitleBar = new CustomTitleBar(this);
        customTitleBar.SetLocation("---");
        titleBarLinearlayout = (LinearLayout) this.findViewById(R.id.sinotik_titlebar_linearlayout);
        
        aqi = (TextView)this.findViewById(R.id.sinotik_aqi);
        gx = (TextView)this.findViewById(R.id.sinotik_gx);
        quality =(TextView)this.findViewById(R.id.sinotik_quality);
        
        chartView = (RelativeLayout)findViewById(R.id.chartview);
		chartViewBar = new ChartView();
        
        if(NetworkUtil.isNetworkAvailable(getApplicationContext()))
        {
//        	initAmapConfig();
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
//				mLocationManagerProxy.removeUpdates(MainActivity.this);
//				mLocationManagerProxy.requestLocationData(
//						LocationProviderProxy.AMapNetwork, -1, 15, MainActivity.this);
//				mLocationManagerProxy.setGpsEnable(false);
				Log.i(TAG, "----------txtLocation click------------");
			}
		});
        
        dataUtil = new DataUtil(this);
        if(!dataUtil.isFirstIntall())
        {
        	updateIndexChartView(false);
        }
		
//		initializeTrendButton(null);
		initializeButton();
		

	
		btnPerday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackgroundColor(darkGray);
				btnPerday.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				btnPermounth.setBackgroundColor(darkGray);
				ChartView chartViewBar = new ChartView();
				final List<EntityData> dataList = dataUtil.GetJsonDataFromSharedPreferences();
				Log.i(TAG, "---------dataList.size()-----" + dataList.size());
				if(dataList !=null && dataList.size() > 0)
				{
					chartView.removeAllViews();
					if(dataList.size() <= 7)
					{
						chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
								dataList, 0x01));
					}
					else
					{
						chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
								dataList, 0x03));
					}

				}

			}
		});
		
		btnPerWeek.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				btnPerday.setBackgroundColor(darkGray);
				btnPermounth.setBackgroundColor(darkGray);
				ChartView chartViewBar = new ChartView();
				final List<EntityData> dataList = dataUtil.GetJsonDataFromSharedPreferences();
				Log.i(TAG, "---------dataList.size()-----" + dataList.size());
				if(dataList !=null && dataList.size() > 0)
				{
					chartView.removeAllViews();
					if(dataList.size() <= 7)
					{
						chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
								dataList, 0x01));
					}
					else
					{
						chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
								dataList, 0x02));
					}
				}

				
			}
		});
		
		btnPermounth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPerWeek.setBackgroundColor(darkGray);
				btnPerday.setBackgroundColor(darkGray);
				btnPermounth.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
				ChartView chartViewBar = new ChartView();
				final List<EntityData> dataList = dataUtil.GetJsonDataFromSharedPreferences();
				Log.i(TAG, "---------dataList.size()-----" + dataList.size());
				if(dataList !=null && dataList.size() > 0)
				{
					chartView.removeAllViews();
					chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
							dataList, 0x01));
				}

			}
		});
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
    
//    //��ʼ���ߵµ�ͼ
//    private void initAmapConfig()
//    {
//		// ��ʼ����λ��ֻ�������綨λ
//		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
//		mLocationManagerProxy.setGpsEnable(false);
//		// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
//		// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
//		// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
//		// ����������ʱ��Ϊ-1����λֻ��һ��,
//		//�ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
//		mLocationManagerProxy.requestLocationData(
//				LocationProviderProxy.AMapNetwork, -1, 15, this);
//		Log.i(TAG, "---------initAmapConfig-----------");
//    }
    
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
    
    //ȡ����ʾ������
    private void cancelProgressBar()
    {
    	if(mProgressDialog != null)
    	{
    		mProgressDialog.dismiss();
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
				cancelProgressBar();
				updateIndexChartView(true);
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
    private void updateIndexChartView(boolean flag)
    {
    	chartView.removeAllViews();
    	if(flag)
    	{
    		//��һ��
        	Log.i(TAG, "---------dataUtil.entityDataList.size()-----" +dataUtil.entityDataList.size());
        	if(dataUtil.entityDataList != null && dataUtil.entityDataList.size() > 0)
        	{
        		dataEntityList = dataUtil.entityDataList;
        		String aqiText = dataEntityList.get(0).getApi();
        		if(aqiText != null)
        		{
            		aqi.setText(aqiText);
            		quality.setText(getQuality(Integer.parseInt(aqiText)));
        		}
        		else
        		{
            		aqi.setText("---");
            		quality.setText("---");
        		}
        		gx.setText(dataUtil.gxData.GetGxString(dataEntityList.get(0).getGx()));
				if(dataUtil.entityDataList.size() <= 7)
				{
					chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
							dataUtil.entityDataList, 0x01));
				}
				else
				{
					chartView.addView(chartViewBar.execute(MainActivity.this.getApplicationContext(), 
							dataUtil.entityDataList, 0x03));
				}
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
    	else
    	{
    		List<EntityData> entityDataList = dataUtil.GetJsonDataFromSharedPreferences();
    		if(entityDataList != null && entityDataList.size() > 0)
    		{
    			String aqiText = entityDataList.get(0).getApi();
        		if(aqiText != null)
        		{
            		aqi.setText(aqiText);
            		quality.setText(getQuality(Integer.parseInt(aqiText)));
        		}
        		else
        		{
            		aqi.setText("---");
            		quality.setText("---");
        		}
        		gx.setText(dataUtil.gxData.GetGxString(entityDataList.get(0).getGx()));
        		chartView.addView(chartViewBar.execute(this.getApplicationContext(), entityDataList, 0x03));
    		}
    		else
    		{
        		//û�и���������
        		Toast.makeText(MainActivity.this,"û�и��������ˣ�", Toast.LENGTH_LONG).show();
    		}
    	}
    }
    
    private String getQuality(int aqi)
    {
    	String quality = "";
    	if(aqi >= 0 && aqi <= 50)
    	{
    		quality = "��";
    	}
    	else if(aqi >= 51 && aqi <= 100)
    	{
    		quality = "��";
    	}
    	else if(aqi >= 101 && aqi <= 150)
    	{
    		quality = "�����Ⱦ";
    	}
    	else if(aqi >= 151 && aqi <= 200)
    	{
    		quality = "�ж���Ⱦ";
    	}
    	else if(aqi >= 201 && aqi <= 300)
    	{
    		quality = "�ض���Ⱦ";
    	}
    	else if(aqi >= 301)
    	{
    		quality = "������Ⱦ";
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
    
//    private void removeUpdate(LocationManagerProxy mLocationManagerProxy, AMapLocationListener aMapLocationListener)
//    {
//    	if(mLocationManagerProxy != null)
//    	{
//    		//�Ƴ���λ����
//    		mLocationManagerProxy.removeUpdates(aMapLocationListener);
//    		// ���ٶ�λ
//    		mLocationManagerProxy.destroy();
//    	}
//    }
    
    private void initializeButton()
    {
		btnPerWeek = (Button)MainActivity.this.findViewById(R.id.sinotik_btnperweek);
		btnPerday = (Button)MainActivity.this.findViewById(R.id.sinotik_btnperday);
		btnPermounth = (Button)MainActivity.this.findViewById(R.id.sinotik_btnpermounth);
		
		
		btnPerWeek.setBackgroundColor(darkGray);
		btnPerday.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.gridview_sel));
		btnPermounth.setBackgroundColor(darkGray);
    }
    
	private void initializeTrendButton(List<EntityData> dataEntityList)
	{

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
						sendMessage(mHandler, Constants.FIRST_INTSALL);
					}
					else
					{
						IsMachine = true;
						sendMessage(mHandler, 5);
					}
					
				}
				else
				{
					Toast.makeText(MainActivity.this,"���е�ַ����Ϊ�գ�����³������ݿ⣡", Toast.LENGTH_LONG).show();
				}

			}
		}
	}

//	@Override
//	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//		// TODO Auto-generated method stub
//		
//	}

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
					sendMessage(mHandler, Constants.FIRST_INTSALL);			
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
}
