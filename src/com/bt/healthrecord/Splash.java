package com.bt.healthrecord;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/* Only Mode portrait for this activity */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/* Hide the action bar for this activity */
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		setContentView(R.layout.splash);
		/*
		Thread timer = new Thread(){
			public void run() {
				try {
					sleep(7000);
				} catch (InterruptedException e) {
					// handle exception
					e.printStackTrace();
				} finally{
					Intent openStartingPoint = new Intent("com.bt.healthrecord.MAINACTIVITY");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();*/
		
		Intent openStartingPoint = new Intent("com.bt.healthrecord.MAINACTIVITY");
		startActivity(openStartingPoint);
	 
	}
	
	

}
