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
        new WriteToSD(this);//将城市列表数据库写入到SD卡
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
    		mapView.onCreate(savedInstanceState);// 此方法必须重写
    		init();
        }
        else
        {
        	//显示对话框
        	checkNetworkDialog();
        }
        
        titleBarLinearlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 注意更换定位时间后，需要先将定位请求删除，再进行定位请求
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
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
	}
    
    private void checkNetworkDialog()
    {
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
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
				MainActivity.this.finish();
			}
		});
    	builder.create().show();
    }
    
//    //初始化高德地图
//    private void initAmapConfig()
//    {
//		// 初始化定位，只采用网络定位
//		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
//		mLocationManagerProxy.setGpsEnable(false);
//		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//		// 在定位结束后，在合适的生命周期调用destroy()方法
//		// 其中如果间隔时间为-1，则定位只定一次,
//		//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//		mLocationManagerProxy.requestLocationData(
//				LocationProviderProxy.AMapNetwork, -1, 15, this);
//		Log.i(TAG, "---------initAmapConfig-----------");
//    }
    
    //显示进度条
    private void showProgressBar()
    {
    	if(mProgressDialog == null)
    	{
        	mProgressDialog = new ProgressDialog(this,"正在请求初始化数据，请稍等......");
        	mProgressDialog.setCanceledOnTouchOutside(false);
        	mProgressDialog.show();
    	}
    	else
    	{
        	mProgressDialog.setCanceledOnTouchOutside(false);
        	mProgressDialog.show();
    	}

    }
    
    //取消显示进度条
    private void cancelProgressBar()
    {
    	if(mProgressDialog != null)
    	{
    		mProgressDialog.dismiss();
    	}
    }
    
    //主处理消息handler
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {   
            switch (msg.what) {
			case -1:
				//网络错误，或者异常
				break;
			case 0:
				//数据下载失败
				break;
			case 1:
				//数据下载成功,显示界面
				Log.i(TAG, "-----mHandler 1-------");
				cancelProgressBar();
				updateIndexChartView(true);
				initializeButton();
				break;
			case 2:
				//定位结束，开始请求数据
				Log.i(TAG, "-----mHandler 2-------");
				//每次获取一个月的数据
//				getJsonData(cityCode, "1");
				break;
			case 3:
				//定位失败，开始请求数据
				Log.i(TAG, "-----mHandler 3-------");
				Toast.makeText(MainActivity.this,"定位失败！", Toast.LENGTH_LONG).show();
				break;
			case 4:
				//第一次安装
				Log.i(TAG, "-----mHandler 4-------");
				showProgressBar();
				getJsonData(cityCode, "5");
				break;
			case 5:
				//请求机房数据
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
    
    //更新空气指数和图表
    private void updateIndexChartView(boolean flag)
    {
    	chartView.removeAllViews();
    	if(flag)
    	{
    		//第一次
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
        		//没有更多数据了
        		aqi.setText("---");
        		quality.setText("---");
        		gx.setText("--");
        		Toast.makeText(MainActivity.this,"没有更多数据了！", Toast.LENGTH_LONG).show();
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
        		//没有更多数据了
        		Toast.makeText(MainActivity.this,"没有更多数据了！", Toast.LENGTH_LONG).show();
    		}
    	}
    }
    
    private String getQuality(int aqi)
    {
    	String quality = "";
    	if(aqi >= 0 && aqi <= 50)
    	{
    		quality = "优";
    	}
    	else if(aqi >= 51 && aqi <= 100)
    	{
    		quality = "良";
    	}
    	else if(aqi >= 101 && aqi <= 150)
    	{
    		quality = "轻度污染";
    	}
    	else if(aqi >= 151 && aqi <= 200)
    	{
    		quality = "中度污染";
    	}
    	else if(aqi >= 201 && aqi <= 300)
    	{
    		quality = "重度污染";
    	}
    	else if(aqi >= 301)
    	{
    		quality = "严重污染";
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
	 * 方法必须重写
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
//    		//移除定位请求
//    		mLocationManagerProxy.removeUpdates(aMapLocationListener);
//    		// 销毁定位
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
	
	//Activity跳转回调函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//resultCode : RESULT_OK=-1
		//requestCode :自己定义的
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
					Toast.makeText(MainActivity.this,"城市地址编码为空，请更新城市数据库！", Toast.LENGTH_LONG).show();
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
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
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
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
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
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			
			mLocationOption.setWifiActiveScan(true);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
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
