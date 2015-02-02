package com.bt.medication_alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.bt.healthrecord.R;
import com.bt.medication_byday.MedicationToTake;
import com.bt.medication_model.MedicationHourDosage;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class TimeAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent paramIntent) {

	createNotification(context, paramIntent);
    }

    private final void createNotification(Context context, Intent paramIntent) {
	NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);

	MedicationHourDosage medicationHourDosage = (MedicationHourDosage) paramIntent
		.getSerializableExtra("Medication");

	String name = medicationHourDosage.getMedication_schedule().getMedication().getName();
	String text = "Hey! Tu as " + medicationHourDosage.getQuantity() + " unité(s)" + " de " + name + " à prendre";
	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher).setContentTitle("Digi Nurse").setContentText(text);

	Intent intentNotification = new Intent(context, MedicationToTake.class);

	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification, 0);
	mBuilder.setContentIntent(pendingIntent);

	Notification notification = mBuilder.build();
	notification.defaults |= Notification.DEFAULT_VIBRATE;
	notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.shake_pills);

	// notification.defaults |= Notification.DEFAULT_ALL;

	int id = medicationHourDosage.getId();
	notificationManager.notify(id, notification);

    }

}