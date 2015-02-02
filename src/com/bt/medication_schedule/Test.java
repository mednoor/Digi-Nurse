package com.bt.medication_schedule;

import com.bt.healthrecord.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;

public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test);
	}

	public void click(View v) {
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		NotificationManager notificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(NOTIFICATION_SERVICE);

		notificationManager.cancel(id);
	}
}
