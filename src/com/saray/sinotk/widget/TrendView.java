package com.saray.sinotk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TrendView extends View{
	
	private static final String TAG = "trendview";

	private final int INTERVAL = 8;
	
	private Context context;
	private Paint mPointPaintPink;
	private Paint mPointPaintBlue;

	private Paint mPointPaintWhite;
	private Paint mLinePaintPink;
	private Paint mLinePaintBlue;
	private Paint mLinePaintWhite;
	
	private int[] index_air = null;
	private int[] index_temp = null;
	private int[] index_humidity = null;
	
	private int max = 0;
	private int min = 0;
	private int xx = 50;

	public TrendView(Context context) {
		super(context);
		this.context = context;
		initialPaint();
	}
	
	public TrendView(Context context, AttributeSet attr){
		super(context,attr);
		this.context = context;
		initialPaint();
		initialData();
	}
	
	private void initialData()
	{
		index_air = new int[INTERVAL];
		index_temp = new int[INTERVAL];
		index_humidity = new int[INTERVAL];
	}
	
	public void SetData(int[] index_air, int[] index_temp,int[] index_humidity)
	{
		this.index_air = index_air;
		this.index_temp = index_temp;
		this.index_humidity = index_humidity;
		SortData(index_air);
	}
	
	public void SortData(int[] data)
	{
		if(data != null)
		{
			max = min = data[0];
			for (int i=0; i<data.length; i++)
			{
				if(data[i] > max)
				{
					max = data[i];
				}
				
				if(data[i] < min)
				{
					min = data[i];
				}
			}
			xx = max - min;
		}

	}
	
	private void initialPaint()
	{
		//粉色圆点
		mPointPaintPink = new Paint();
		mPointPaintPink.setAntiAlias(true);
		mPointPaintPink.setColor(Color.rgb(255, 87, 87));
		
		//粉色圆点
		mPointPaintBlue = new Paint();
		mPointPaintBlue.setAntiAlias(true);
		mPointPaintBlue.setColor(Color.rgb(108, 182, 255));
		
		//粉色圆点
		mPointPaintWhite = new Paint();
		mPointPaintWhite.setAntiAlias(true);
		mPointPaintWhite.setColor(Color.rgb(255, 255, 255));
		
		//粉色-空气质量
		mLinePaintPink = new Paint();
		mLinePaintPink.setColor(Color.rgb(255, 87, 87));//粉色#FF8787
		mLinePaintPink.setAntiAlias(true);
		mLinePaintPink.setStrokeWidth(3);
		mLinePaintPink.setStyle(Style.FILL);

		//蓝色-湿度
		mLinePaintBlue = new Paint();
		mLinePaintBlue.setColor(Color.rgb(108, 182, 255));//蓝色#6CB6FF
		mLinePaintBlue.setAntiAlias(true);
		mLinePaintBlue.setStrokeWidth(3);
		mLinePaintBlue.setStyle(Style.FILL);
		
		//白色-温度
		mLinePaintWhite = new Paint();
		mLinePaintWhite.setColor(Color.rgb(255, 255, 255));//白色#FFFFFF
		mLinePaintWhite.setAntiAlias(true);
		mLinePaintWhite.setStrokeWidth(3);
		mLinePaintWhite.setStyle(Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float[] pointx = new float[INTERVAL];
		float width = (float)getWidth();

		float pointy = (float) (getHeight()/(xx+40));
		
		pointx[0] = width / 14;
		for (int i = 1; i < INTERVAL; i++) {
		    pointx[i] = pointx[i - 1] + (width / INTERVAL);
		}
		
		Log.i(TAG, "-----------max-------------" + max);
		
		if(index_air != null)
		{
			//画线
			for (int i = 0; i < INTERVAL-1; i++) {
			    canvas.drawLine(pointx[i],(float)(pointy*(max-index_air[i]+10)+70),pointx[i+1], (float)(pointy*(max-index_air[i+1]+10)+70), mLinePaintWhite);
			}
			
			for (int i = 0; i < INTERVAL; i++) {
			    //画点
			    canvas.drawCircle(pointx[i],(float)(pointy*(max-index_air[i]+10)+70), 8, mLinePaintWhite);
			}
		}

		if(index_temp != null)
		{
			//画线
			for (int i = 0; i < INTERVAL-1; i++) {
			    canvas.drawLine(pointx[i],(float)(pointy*(max-index_temp[i]+10)+70),pointx[i+1], (float)(pointy*(max-index_temp[i+1]+10)+70), mLinePaintPink);
			}
			
			for (int i = 0; i < INTERVAL; i++) {
			    //画点
			    canvas.drawCircle(pointx[i],(float)(pointy*(max-index_temp[i]+10)+70), 8, mLinePaintPink);
			}
		}
		

		if(index_humidity != null)
		{
			//画线
			for (int i = 0; i < INTERVAL-1; i++) {
			    canvas.drawLine(pointx[i],(float)(pointy*(max-index_humidity[i]+10)+70),pointx[i+1], (float)(pointy*(max-index_humidity[i+1]+10)+70), mLinePaintBlue);
			}
			
			for (int i = 0; i < INTERVAL; i++) {
			    //画点
			    canvas.drawCircle(pointx[i],(float)(pointy*(max-index_humidity[i]+10)+70), 8, mLinePaintBlue);
			}
		}

	}

}
