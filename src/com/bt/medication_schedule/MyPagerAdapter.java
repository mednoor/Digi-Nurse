package com.bt.medication_schedule;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	
	private final List<Fragment> fragments;

	public MyPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fragments.size();
	}
	
	
	

}
