package com.saray.sinotk.adapter;

import java.util.ArrayList;
import java.util.List;

import com.saray.sinotk.entity.OutStation;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ViewPageAdapter extends PagerAdapter{
	
    private Context context;
	private List<OutStation> outStations;
	private ViewPager viewPager;
	private int gridViewCount;

	public ViewPageAdapter(ViewPager viewPager, List<OutStation> outStations) {
        this.context = viewPager.getContext();
        this.outStations = new ArrayList<OutStation>();
        this.viewPager = viewPager;
        this.gridViewCount = outStations.size();
    }
	
    @Override
    public Object instantiateItem(ViewGroup container, int position) 
    {
    	RecyclerView recycleView = new RecyclerView(context);
		return position;
    	
    }

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
