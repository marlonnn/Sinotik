package com.saray.sinotk.adapter;

import java.util.List;
import java.util.Map;

import com.saray.sinotk.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter{
	
	private Context context;
	private List<Map<String, String>> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;

	public LocationAdapter(Context context,List<Map<String, String>> list)
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
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("null")
	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
////		if(convertView == null)
//		{
			holder  =  new ViewHolder();
			convertView = inflater.inflate(R.layout.sinotik_location_item, null);
			holder.textView = (TextView)convertView.findViewById(R.id.sinotik_location);
			holder.textView.setText(list.get(position).get("address"));
			convertView.setTag(holder);  
//		}
//		else
//		{
//			 holder = (ViewHolder)convertView.getTag();  
//		}

		return convertView;
	}
	
	public final class ViewHolder {
		public TextView textView;
	}
}
