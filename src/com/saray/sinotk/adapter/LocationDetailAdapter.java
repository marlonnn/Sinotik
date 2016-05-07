package com.saray.sinotk.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saray.sinotk.R;
import com.saray.sinotk.adapter.LocationAdapter.ViewHolder;

public class LocationDetailAdapter extends BaseAdapter{
	
	private Context context;
	private List<Map<String, String>> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;

	public LocationDetailAdapter(Context context,List<Map<String, String>> list)
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
		ImageViewListener imageViewListener = null;
		if(convertView == null)
		{
			holder  =  new ViewHolder();
			imageViewListener = new ImageViewListener(holder,position);
			convertView = inflater.inflate(R.layout.sinotik_location_detail_item, null);
			holder.imageView = (ImageView)convertView.findViewById(R.id.sinotik_location_detail_select);
			holder.textView = (TextView)convertView.findViewById(R.id.sinotik_detail_location);
			holder.textView.setText(list.get(position).get("address"));
			holder.imageView .setTag(position);
			convertView.setTag(holder);  
		}
		else
		{
			 holder = (ViewHolder)convertView.getTag();  
		}
		holder.imageView.setTag(position);
		holder.imageView.setOnClickListener(imageViewListener);

		return convertView;
	}
	
	private class ImageViewListener implements OnClickListener{
		
		private int position;
		private ViewHolder holder;
		private boolean flag = true;
		
		public ImageViewListener(ViewHolder holder,int position)
		{
			this.holder = holder;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if(flag)
			{
				holder.imageView.setBackgroundResource(R.drawable.icon_select);
				flag = false;
			}
			else
			{
				holder.imageView.setBackgroundResource(R.drawable.icon_unselect);
				flag = true;
			}
			
		}
		
	}

	public final class ViewHolder {
		public ImageView imageView ;
		public TextView textView;
	}

}
