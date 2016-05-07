package com.saray.sinotk.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saray.sinotk.entity.*;
import com.saray.sinotk.R;

public class QualityAdapter extends BaseAdapter{
	
	private Context context;
	private List<Quality> listData;
	protected LayoutInflater inflater;

	public QualityAdapter()
	{
		
	}
	
	public QualityAdapter(Context context,List<Quality> listData)
	{
		this.context = context;
		this.listData = listData;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = inflater.inflate(R.layout.sinotik_quality_item, null);
		TextView qualityItem = (TextView) v.findViewById(R.id.sinotik_quality_item);//pm25 pm10 o3 so2 co2
		TextView qualityIndex = (TextView) v.findViewById(R.id.sinotik_quality_index);//指数
		TextView quality = (TextView) v.findViewById(R.id.sinotik_quality);//质量
		
		qualityItem.setText(listData.get(position).getQualityItem());
		qualityIndex.setText(listData.get(position).getQualityIndex());
		
		quality.setText(listData.get(position).getQuality());

		return v;
	}

}
