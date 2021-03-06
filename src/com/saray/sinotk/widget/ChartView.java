package com.saray.sinotk.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.saray.sinotk.entity.EntityData;
import com.saray.sinotk.entity.InnerStationData;
import com.saray.sinotk.entity.OutStationData;
import com.saray.sinotk.util.GXData;
import com.saray.sinotk.util.GXUtil;
import com.saray.sinotk.util.GXData.GX;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;


public class ChartView extends AbstractChart{
	
	public View execute(Context context, List<EntityData> dataEntityList, byte type)
	{
		int discount = 1;
		switch(type)
		{
		case 0x03://每天
			discount = 3;
			break;
		case 0x02://每周
			discount = 2;
			break;
		case 0x01://每月
			discount = 1;
			break;
		}
		
		return 	execute(context, dataEntityList, discount);
	}
	
	private int GetGxValue(GX gx)
	{
		int value = 20;
		switch(gx)
		{
		case G1:
			value = 20;
			break;
		case G2:
			value = 40;
			break;
		case G3:
			value = 50;
			break;
		case GX:
			value = 55;
			break;
		}
		return value;
	}
	
	private int GetGxValue(String gx)
	{
		int value = 20;
		switch(gx)
		{
		case "G1":
			value = 20;
			break;
		case "G2":
			value = 40;
			break;
		case "G3":
			value = 50;
			break;
		case "GX":
			value = 55;
			break;
		}
		return value;
	}
	
	public View execute(Context context, List<InnerStationData> dataEntityList, boolean flag)
	{
		if (dataEntityList != null && dataEntityList.size() > 0)
		{
			int size = dataEntityList.size();
			double[] minValues = new double[size];
			double[] maxValues = new double[size];
		    int[] colors = new int[] { Color.CYAN };
		    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		    
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    RangeCategorySeries series = new RangeCategorySeries("");
		    
		    for (int i=0; i<dataEntityList.size(); i++)
		    {
				minValues[i] = 1;
				maxValues[i] = GetGxValue(dataEntityList.get(i).getGxlevels());
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设定格式 
					Date timeDate = dateFormat.parse(dataEntityList.get(i).getTesttime());
					
					SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd HH:mm");
					String displayValue = timeFormatter.format(timeDate);
					renderer.addXTextLabel(i, displayValue);

				} catch (ParseException e) {
					e.printStackTrace();
				}
				series.add(minValues[i], maxValues[i]);
		    }
		    dataset.addSeries(series.toXYSeries());

		    setChartSettings(renderer, "Data from Sinotik", "", "Gx Level", 0, size,
			        0, 60, Color.GRAY, Color.LTGRAY);
		    renderer.setBarSpacing(0.1);

		    renderer.setXLabels(0);
		    renderer.setXAxisMin(-1);
		    renderer.setXLabelsAngle(-30.0f);
		    renderer.setXAxisMax(dataEntityList.size());
		    renderer.setXLabelsAlign(Align.CENTER);
		    renderer.setYLabels(0);
		    
		    renderer.addYTextLabel(20, "G1");
		    renderer.addYTextLabel(40, "G2");
		    renderer.addYTextLabel(50, "G3");
		    renderer.addYTextLabel(55, "GX");
		    renderer.setLabelsTextSize(22);
		    renderer.setLegendTextSize(22 * 2);
		    renderer.setAxisTitleTextSize(22);
		    renderer.setChartTitleTextSize(22);
		    renderer.setMargins(new int[] {0, 50, 10, 0});
		    renderer.setYLabelsAlign(Align.LEFT);
		    renderer.setPanEnabled(true, false);
		    renderer.setZoomButtonsVisible(true);
		    renderer.setZoomEnabled(true, false);
		    if(dataEntityList.size() > 200)
		    {
			    renderer.setZoomRate(3);
		    }
		    else if (dataEntityList.size() > 18 && dataEntityList.size() < 200)
		    {
		    	renderer.setZoomRate(2);
		    }
		    else
		    {
//		    	renderer.setBarWidth(28);
		    }
		    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		    r.setDisplayChartValues(false);
//		    r.setChartValuesTextSize(25);
//		    renderer.setLegendTextSize(20);
//		    r.setChartValuesSpacing(5);
		    r.setGradientEnabled(true);
		    r.setGradientStart(0, Color.YELLOW);
		    r.setGradientStop(55, Color.RED);
		    
		    renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		    renderer.setApplyBackgroundColor(true);
	        renderer.setBackgroundColor(Color.TRANSPARENT);
		    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.STACKED);
		}
		else
		{
			return null;
		}
	}
	
	public View execute(Context context, List<OutStationData> dataEntityList)
	{
		GXData gxData = new GXData(context);
		if (dataEntityList != null && dataEntityList.size() > 0)
		{
			int size = dataEntityList.size();
			double[] minValues = new double[size];
			double[] maxValues = new double[size];
		    int[] colors = new int[] { Color.CYAN };
		    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		    
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    RangeCategorySeries series = new RangeCategorySeries("");
		    
		    for (int i=0; i<dataEntityList.size(); i++)
		    {
			    GX gxSo2 = GXUtil.CaculateGx("so2", dataEntityList.get(i).getSo2());
			    GX gxNo2 = GXUtil.CaculateGx("no2", dataEntityList.get(i).getNo2());
				GX gx = gxData.GetGx(gxSo2) > gxData.GetGx(gxNo2) ? gxSo2 : gxNo2;
				minValues[i] = 1.5;
				maxValues[i] = GetGxValue(gx);
				try {
//					if (i % 2 == 0)
					{
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设定格式 
						Date timeDate = dateFormat.parse(dataEntityList.get(i).getTesttime());
						
						SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd HH:mm");
						String displayValue = timeFormatter.format(timeDate);
						renderer.addXTextLabel(i, displayValue);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
				series.add(minValues[i], maxValues[i]);
		    }
		    dataset.addSeries(series.toXYSeries());

		    setChartSettings(renderer, "Data from Sinotik", "", "Gx Level", 0, size,
			        0, 60, Color.GRAY, Color.LTGRAY);
		    renderer.setBarSpacing(0.1);

		    renderer.setXLabels(0);
		    renderer.setXAxisMin(-1);
		    renderer.setXLabelsAngle(-30.0f);
		    renderer.setXAxisMax(dataEntityList.size());
		    renderer.setXLabelsAlign(Align.CENTER);
		    renderer.setYLabels(0);
		    
		    renderer.addYTextLabel(20, "G1");
		    renderer.addYTextLabel(40, "G2");
		    renderer.addYTextLabel(50, "G3");
		    renderer.addYTextLabel(55, "GX");
		    renderer.setMargins(new int[] {0, 50, 10, 0});
		    renderer.setLabelsTextSize(22);
		    renderer.setLegendTextSize(22 * 2);
		    renderer.setAxisTitleTextSize(22);
		    renderer.setChartTitleTextSize(22);
//		    renderer.setYLabelsPadding(10);
//		    renderer.setXLabelsPadding(10);
		    renderer.setYLabelsAlign(Align.LEFT);
		    renderer.setPanEnabled(true, false);
		    renderer.setZoomButtonsVisible(true);
		    renderer.setZoomEnabled(true, false);
		    renderer.setBarSpacing(0.1);
		    if(dataEntityList.size() > 200)
		    {
//		    	renderer.setBarWidth(18);
			    renderer.setZoomRate(4);
		    }
		    else if (dataEntityList.size() > 18 && dataEntityList.size() < 200)
		    {
//		    	renderer.setBarWidth(28);
		    	renderer.setZoomRate(3);
		    }
		    else
		    {
//		    	renderer.setBarWidth(38);
		    }
		    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		    r.setDisplayChartValues(false);
//		    r.setChartValuesTextSize(12);
//		    r.setChartValuesSpacing(5);
		    r.setGradientEnabled(true);
		    r.setGradientStart(0, Color.YELLOW);
		    r.setGradientStop(55, Color.RED);
		    
		    renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		    renderer.setApplyBackgroundColor(true);
	        renderer.setBackgroundColor(Color.TRANSPARENT);
		    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.STACKED);
		}
		else
		{
			return null;
		}
	}
	
	public View execute(Context context, List<EntityData> dataEntityList, int discount)
	{
		
		if(dataEntityList != null && dataEntityList.size() > 0)
		{
			int size = dataEntityList.size() / discount;
			double[] minValues = new double[size];
			double[] maxValues = new double[size];
		    int[] colors = new int[] { Color.CYAN };
		    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		    
			for(int i=0; i<size; i++)
			{
				minValues[i] = 0;
				GX gx = dataEntityList.get(i).getGx();
				int value = GetGxValue(gx);
//				BigDecimal b = new BigDecimal(dataEntityList.get(i).getGxValue()); 
//				maxValues[i] = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				maxValues[i] = value;
//				renderer.addXTextLabel(i, "1");
			}
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    RangeCategorySeries series = new RangeCategorySeries("GX值");
		    int length = minValues.length;
		    for (int k = 0; k < length; k++) {
		      series.add(minValues[k], maxValues[k]);
		    }
		    dataset.addSeries(series.toXYSeries());

		    setChartSettings(renderer, "数据来源于塑欣科技", "每天GX指数柱状图", "", 0, size + 0.5,
			        0, 60, Color.GRAY, Color.LTGRAY);
		    renderer.setBarSpacing(0.1);
		    renderer.setXLabels(20);
		    renderer.setYLabels(1);
		    
		    renderer.addYTextLabel(20, "G1");
//		    renderer.setYLabelsColor(50, Color.RED);
		    renderer.addYTextLabel(40, "G2");
//		    renderer.setYLabelsColor(125, Color.RED);
		    renderer.addYTextLabel(50, "G3");
//		    renderer.setYLabelsColor(1250, Color.RED);
		    renderer.addYTextLabel(55, "GX");
//		    renderer.setYLabelsColor(1300, Color.RED);
		    
		    renderer.setMargins(new int[] {0, 0, 0, 0});
		    renderer.setYLabelsAlign(Align.LEFT);
		    renderer.setPanEnabled(true, false);
		    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		    r.setDisplayChartValues(false);
		    r.setChartValuesTextSize(12);
		    r.setChartValuesSpacing(3);
		    r.setGradientEnabled(true);
		    r.setGradientStart(0, Color.YELLOW);
		    r.setGradientStop(55, Color.RED);
		    
		    renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		    renderer.setApplyBackgroundColor(true);
	        renderer.setBackgroundColor(Color.TRANSPARENT);
		    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.DEFAULT);
		}
		else
		{
			return null;
		}
		
	}
	
	public View executeMouth(Context context)
	{
	    double[] minValues = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    									0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    									0, 0, 0, 0, 0, 0};
	    double[] maxValues = new double[] { 100, 230, 450, 330, 320, 220, 340, 156, 144, 123, 132, 140,
											110, 310, 89, 110, 210, 188, 175, 154, 132, 130, 120, 140,
											210, 250, 220, 390, 100, 320};
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    RangeCategorySeries series = new RangeCategorySeries("AQI值");
	    int length = minValues.length;
	    for (int k = 0; k < length; k++) {
	      series.add(minValues[k], maxValues[k]);
	    }
	    dataset.addSeries(series.toXYSeries());
	    int[] colors = new int[] { Color.CYAN };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "数据来源于塑欣科技", "每天AQI指数柱状图", "", 0, 30.5,
		        0, 500, Color.GRAY, Color.LTGRAY);
	    renderer.setBarSpacing(0.1);
	    renderer.setXLabels(20);
	    renderer.setYLabels(20);
	    
	    renderer.addYTextLabel(50, "优");
	    renderer.addYTextLabel(100, "良");
	    renderer.addYTextLabel(150, "轻度");
	    renderer.addYTextLabel(200, "中度");
	    renderer.addYTextLabel(300, "重度");
	    renderer.addYTextLabel(500, "严重");
	    renderer.setMargins(new int[] {0, 0, 0, 0});
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(false, false);
	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
	    r.setDisplayChartValues(true);
	    r.setChartValuesTextSize(12);
	    r.setChartValuesSpacing(3);
	    r.setGradientEnabled(true);
	    r.setGradientStart(0, Color.GREEN);
	    r.setGradientStop(500, Color.RED);
	    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.DEFAULT);
	}
	
	  /**
	   * Executes the chart demo.
	   * 
	   * @param context the context
	   * @return the built intent
	   */
	  public View executeDay(Context context) {
	    double[] minValues = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	    double[] maxValues = new double[] { 100, 120, 240, 280, 330, 350, 370, 360, 280, 190, 110, 400, 
	    									110, 310, 89, 110, 210, 188, 175, 154, 132, 130, 120, 140};

	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    RangeCategorySeries series = new RangeCategorySeries("AQI值");
	    int length = minValues.length;
	    for (int k = 0; k < length; k++) {
	      series.add(minValues[k], maxValues[k]);
	    }
	    dataset.addSeries(series.toXYSeries());
	    int[] colors = new int[] { Color.CYAN };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "数据来源于塑欣科技", "每月AQI指数柱状图", "", 0, 24.5,
	        0, 500, Color.GRAY, Color.LTGRAY);
	    renderer.setBarSpacing(0.2);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
//	    renderer.addXTextLabel(1, "01:00");
	    renderer.addXTextLabel(2, "02:00");
//	    renderer.addXTextLabel(3, "03:00");
	    renderer.addXTextLabel(4, "04:00");
//	    renderer.addXTextLabel(5, "05:00");
	    renderer.addXTextLabel(6, "06:00");
//	    renderer.addXTextLabel(7, "07:00");
	    renderer.addXTextLabel(8, "08:00");
//	    renderer.addXTextLabel(9, "09:00");
	    renderer.addXTextLabel(10, "10:00");
//	    renderer.addXTextLabel(11, "11:00");
	    renderer.addXTextLabel(12, "12:00");
//	    renderer.addXTextLabel(13, "13:00");
	    renderer.addXTextLabel(14, "14:00");
//	    renderer.addXTextLabel(15, "15:00");
	    renderer.addXTextLabel(16, "16:00");
//	    renderer.addXTextLabel(17, "17:00");
	    renderer.addXTextLabel(18, "18:00");
//	    renderer.addXTextLabel(19, "19:00");
	    renderer.addXTextLabel(20, "20:00");
//	    renderer.addXTextLabel(21, "21:00");
	    renderer.addXTextLabel(22, "22:00");
//	    renderer.addXTextLabel(23, "23:00");
	    renderer.addXTextLabel(24, "24:00");
	    
	    renderer.addYTextLabel(50, "优");
	    renderer.addYTextLabel(100, "良");
	    renderer.addYTextLabel(150, "轻度");
	    renderer.addYTextLabel(200, "中度");
	    renderer.addYTextLabel(300, "重度");
	    renderer.addYTextLabel(500, "严重");
	    renderer.setMargins(new int[] {0, 0, 0, 0});
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(false, false);
	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
	    r.setDisplayChartValues(true);
	    r.setChartValuesTextSize(12);
	    r.setChartValuesSpacing(3);
	    r.setGradientEnabled(true);
	    r.setGradientStart(0, Color.GREEN);
	    r.setGradientStop(500, Color.RED);
	    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.DEFAULT);
	  }
	  /**
	   * Executes the chart demo.
	   * 
	   * @param context the context
	   * @return the built intent
	   */
	  public View execute(Context context) {
	    double[] minValues = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	    double[] maxValues = new double[] { 100, 120, 240, 280, 330, 350, 370, 360, 280, 190, 110, 400 };

	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    RangeCategorySeries series = new RangeCategorySeries("AQI值");
	    int length = minValues.length;
	    for (int k = 0; k < length; k++) {
	      series.add(minValues[k], maxValues[k]);
	    }
	    dataset.addSeries(series.toXYSeries());
	    int[] colors = new int[] { Color.CYAN };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "数据来源于塑欣科技", "每月AQI指数柱状图", "", 0, 12.5,
	        0, 500, Color.GRAY, Color.LTGRAY);
	    renderer.setBarSpacing(0.5);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
	    renderer.addXTextLabel(1, "一月");
	    renderer.addXTextLabel(2, "二月");
	    renderer.addXTextLabel(3, "三月");
	    renderer.addXTextLabel(4, "四月");
	    renderer.addXTextLabel(5, "五月");
	    renderer.addXTextLabel(6, "六月");
	    renderer.addXTextLabel(7, "七月");
	    renderer.addXTextLabel(8, "八月");
	    renderer.addXTextLabel(9, "九月");
	    renderer.addXTextLabel(10, "十月");
	    renderer.addXTextLabel(11, "十一月");
	    renderer.addXTextLabel(12, "十二月");
	    
	    renderer.addYTextLabel(50, "优");
	    renderer.addYTextLabel(100, "良");
	    renderer.addYTextLabel(150, "轻度");
	    renderer.addYTextLabel(200, "中度");
	    renderer.addYTextLabel(300, "重度");
	    renderer.addYTextLabel(500, "严重");
	    renderer.setMargins(new int[] {0, 0, 0, 0});
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(false, false);
	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
	    r.setDisplayChartValues(true);
	    r.setChartValuesTextSize(12);
	    r.setChartValuesSpacing(3);
	    r.setGradientEnabled(true);
	    r.setGradientStart(0, Color.GREEN);
	    r.setGradientStop(500, Color.RED);
	    return ChartFactory.getRangeBarChartView(context, dataset, renderer, Type.DEFAULT);
	  }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}
}
