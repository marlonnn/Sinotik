package com.saray.sinotk.adapter;

import java.util.List;

import com.saray.sinotk.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> list;
	private LayoutInflater inflater;

	public SettingAdapter(Context context, List<String> list)
	{
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = inflater.inflate(R.layout.sinotik_setting_item, null);
		TextView setting = (TextView) v.findViewById(R.id.sinotik_settingText);
		setting.setText(list.get(arg0));
		return v;
	}

}
