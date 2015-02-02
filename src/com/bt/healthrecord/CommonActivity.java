package com.bt.healthrecord;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class CommonActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Only Mode portrait for this activity */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		/* Custom the action bar */
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getResources().getString(getTitleActionBar()));
		actionBar.setIcon(getIconActionBar());
		actionBar.setHomeButtonEnabled(getHomeButtonActionBar());
		actionBar.setDisplayHomeAsUpEnabled(getUpNavigation()); // Up Navigation parent activity

		// Set the background color of a ActionBar to a hexadecimal color
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(getResources().getString(getColorActionBar()))));
	}

	public abstract int getTitleActionBar();

	public abstract int getIconActionBar();

	public abstract int getColorActionBar();
	
	public abstract boolean getHomeButtonActionBar();
	
	public abstract boolean getUpNavigation();
}
