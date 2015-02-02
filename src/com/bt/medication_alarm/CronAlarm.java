package com.bt.medication_alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bt.medication_model.FeedMedicine;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class CronAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent paramIntent) {

		FeedMedicine.getInstance().cronAlarm(context);

	}

}