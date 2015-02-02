package com.bt.healthrecord;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NoteMyPagerAdapter extends FragmentPagerAdapter {
	
	private final List<Fragment> fragments;

	//On fournit ˆ l'adapter la liste des fragments ˆ afficher
	public NoteMyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
