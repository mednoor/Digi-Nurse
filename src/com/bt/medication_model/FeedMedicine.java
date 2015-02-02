package com.bt.medication_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bt.medication_alarm.CronAlarm;
import com.bt.medication_alarm.TimeAlarm;
import com.bt.medication_dao.MedicationDao;
import com.bt.medication_dao.MedicationFrequencyDao;
import com.bt.medication_dao.MedicationHourDosageDao;
import com.bt.medication_dao.MedicationScheduleDao;

public class FeedMedicine {

	/** L'instance statique */
	private static FeedMedicine instance;

	private Medication medication;

	public MedicationSchedule getMedication_schedule() {
		return this.medication.getSchedule_medication();
	}

	public MedicationFrequency getMedicationFrequency() {
		return this.medication.getSchedule_medication()
				.getMedication_frequency();
	}

	public Medication getMedication() {
		return this.medication;
	}

	public void setMedication(Medication medication) {
		this.medication = medication;
	}

	private FeedMedicine() {

	}

	public static FeedMedicine getInstance() {
		if (null == instance) { // Premier appel
			instance = new FeedMedicine();
		}
		return instance;
	}

	public void saveAll(Context context) {

		MedicationFrequencyDao medicationFrequencyDao = new MedicationFrequencyDao(
				context);
		medicationFrequencyDao.open();
		MedicationFrequency medication_frequency = medicationFrequencyDao
				.insertMedicationFrequency(medication.getSchedule_medication()
						.getMedication_frequency());
		medicationFrequencyDao.close();
		medication.getSchedule_medication().setMedication_frequency(
				medication_frequency);

		MedicationScheduleDao medicationScheduleDao = new MedicationScheduleDao(
				context);
		medicationScheduleDao.open();
		MedicationSchedule medicationSchedule = medicationScheduleDao
				.insertMedicationSchedule(medication.getSchedule_medication());
		medicationScheduleDao.close();
		medication.setSchedule_medication(medicationSchedule);

		// set all id of MedicationHourDosage to MedicationSchedule
		ArrayList<MedicationHourDosage> medication_hour_dosage_list = medication
				.getSchedule_medication().getMedication_hour_dosage_list();
		Iterator<MedicationHourDosage> it = medication_hour_dosage_list
				.iterator();
		while (it.hasNext()) {
			MedicationHourDosage medicationHourDosage = (MedicationHourDosage) it
					.next();
			medicationHourDosage.setMedication_schedule(medication
					.getSchedule_medication());

			MedicationHourDosageDao medicationHourDosageDao = new MedicationHourDosageDao(
					context);
			medicationHourDosageDao.open();
			medicationHourDosage = medicationHourDosageDao
					.insertMedicationHourDosage(medicationHourDosage);
			medicationHourDosageDao.close();
		}

		MedicationDao medicationDao = new MedicationDao(context);
		medicationDao.open();
		medication = medicationDao.insertMedication(medication);
		medicationDao.close();

		sendAlarm(medication, context);

	}

	public void updateMedication(Context context) {

		MedicationDao medicationDao = new MedicationDao(context);
		medicationDao.open();
		medicationDao.update(medication);
		medicationDao.close();

		MedicationScheduleDao medicationScheduleDao = new MedicationScheduleDao(
				context);
		medicationScheduleDao.open();
		medicationScheduleDao.update(medication.getSchedule_medication());
		medicationScheduleDao.close();

		MedicationFrequencyDao medicationFrequencyDao = new MedicationFrequencyDao(
				context);
		medicationFrequencyDao.open();
		medicationFrequencyDao.update(medication.getSchedule_medication()
				.getMedication_frequency());
		medicationFrequencyDao.close();

		MedicationHourDosageDao medicationHourDosageDao = new MedicationHourDosageDao(
				context);
		medicationHourDosageDao.open();
		// cancel previous alarms
		cancelAlarm(
				medicationHourDosageDao.getListMedicationHourDosage(medication
						.getSchedule_medication()), context);
		medicationHourDosageDao.delete(medication.getSchedule_medication()
				.getId());

		// set all id of MedicationHourDosage to MedicationSchedule
		ArrayList<MedicationHourDosage> medication_hour_dosage_list = medication
				.getSchedule_medication().getMedication_hour_dosage_list();
		Iterator<MedicationHourDosage> it = medication_hour_dosage_list
				.iterator();
		while (it.hasNext()) {
			MedicationHourDosage medicationHourDosage = (MedicationHourDosage) it
					.next();
			medicationHourDosage.setMedication_schedule(medication
					.getSchedule_medication());

			medicationHourDosage = medicationHourDosageDao
					.insertMedicationHourDosage(medicationHourDosage);
		}

		sendAlarm(medication, context);

	}

	public void deleteMedication(Context context) {
		MedicationDao medicationDao = new MedicationDao(context);
		MedicationHourDosageDao medicationHourDosageDao = new MedicationHourDosageDao(
				context);
		medicationDao.open();
		medicationHourDosageDao.open();
		// cancel alarms of this medication
		cancelAlarm(
				medicationHourDosageDao.getListMedicationHourDosage(medication
						.getSchedule_medication()), context);
		medicationDao.delete(medication.getId());
		medicationDao.close();
		medicationHourDosageDao.close();

	}

	public void cancelAlarm(
			ArrayList<MedicationHourDosage> listMedicationHourDosage,
			Context context) {

		// Retrieve alarm manager from the system
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		for (MedicationHourDosage medicationHourDosage : listMedicationHourDosage) {

			// Prepare the intent which should be launched at the date
			Intent intent = new Intent(context, TimeAlarm.class);

			// Prepare the pending intent
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					medicationHourDosage.getId(), intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			alarmManager.cancel(pendingIntent);
		}

	}

	public void prepareRepeatingAlamrs(Context context) {
		// Retrieve alarm manager from the system
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// Prepare the intent which should be launched at the date
		Intent intent = new Intent(context, CronAlarm.class);
		int id_repeating_alarms = 999;
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				id_repeating_alarms, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		DateMidnight dateMidnight = new DateMidnight();
		dateMidnight = dateMidnight.plusDays(1);		 

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				dateMidnight.getMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);
		cronAlarm(context);
	}

	public void sendAlarm(Medication medication, Context context) {

		DateMidnight today = new DateMidnight();
		ArrayList<Medication> listMedicationToday = getMedicationForDay(today,
				context);

		if (listMedicationToday.contains(medication)) {

			// Retrieve alarm manager from the system
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			// Every scheduled intent needs a different ID, else it is just
			// executed
			// once

			ArrayList<MedicationHourDosage> listMedicationHourDosage = medication
					.getSchedule_medication().getMedication_hour_dosage_list();

			LocalTime actual_hour = new LocalTime();
			LocalTime hour_to_take;
			for (MedicationHourDosage medicationHourDosage : listMedicationHourDosage) {

				// send alarm only for future hour
				hour_to_take = medicationHourDosage.getHour();
				if (hour_to_take.isBefore(actual_hour))
					continue;
				// Prepare the intent which should be launched at the date
				Intent intent = new Intent(context, TimeAlarm.class);
				// don't serialize bitmap
				medicationHourDosage.getMedication_schedule().getMedication()
						.setBitmap(null);
				intent.putExtra("Medication", medicationHourDosage);
				// Prepare the pending intent
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, medicationHourDosage.getId(), intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				DateTime date_today_plus_hour = hour_to_take.toDateTimeToday();

				alarmManager.set(AlarmManager.RTC_WAKEUP,
						date_today_plus_hour.getMillis(), pendingIntent);

				// alarmManager.cancel(pendingIntent);
				// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				// date.getMillis(), intervals, pendingIntent);

			}

		}

	}

	public void cronAlarm(Context context) {
		DateMidnight date_today = new DateMidnight();
		ArrayList<Medication> listMedicationToday = getMedicationForDay(
				date_today, context);

		for (Medication medication : listMedicationToday) {
			sendAlarm(medication, context);
		}

	}

	public ArrayList<Medication> getMedicationForDay(DateMidnight date_choosed,
			Context context) {
		MedicationDao medicationDao = new MedicationDao(context);
		medicationDao.open();

		ArrayList<Medication> list_medication1 = medicationDao
				.getPotentialMedicationEveryDay(date_choosed);
		ArrayList<Medication> list_medication2 = medicationDao
				.getPotentialMedicationDayWeek(date_choosed);
		ArrayList<Medication> list_medication3 = medicationDao
				.getPotentialMedicationIntervalDay(date_choosed);

		medicationDao.close();
		ArrayList<Medication> new_list_medication = new ArrayList<Medication>();
		for (Medication n : list_medication3) {
			if (inIntervalDay(n, date_choosed))
				new_list_medication.add(n);
		}

		ArrayList<Medication> listMedication = new ArrayList<Medication>();
		listMedication.addAll(list_medication1);
		listMedication.addAll(list_medication2);
		listMedication.addAll(new_list_medication);

		return listMedication;
	}

	public MedicationHourDosage getNextMedicationToday(Context context) {
		DateMidnight todayMidnight = new DateMidnight();
		LocalTime actualLocalTime = new LocalTime();
		MedicationHourDosage medicationHourDosage = null;
		ArrayList<MedicationHourDosage> lisMedicationHourDosages = new ArrayList<MedicationHourDosage>();
		ArrayList<Medication> listMedicationToTakeToday = getMedicationForDay(
				todayMidnight, context);
		for (Medication medication : listMedicationToTakeToday) {
			lisMedicationHourDosages.addAll(medication.getSchedule_medication()
					.getMedication_hour_dosage_list());
		}
		Collections.sort(lisMedicationHourDosages);
		for (MedicationHourDosage medicationHourDosage2 : lisMedicationHourDosages) {
			if (medicationHourDosage2.getHour().isAfter(actualLocalTime))
				return medicationHourDosage2;
		}

		return medicationHourDosage;
	}

	public ArrayList<Medication> getAllMedication(Context context) {
		MedicationDao medicationDao = new MedicationDao(context);
		medicationDao.open();

		ArrayList<Medication> listing_all_medication = medicationDao
				.getAllMedication();
		medicationDao.close();

		return listing_all_medication;
	}

	public Boolean inIntervalDay(Medication medication,
			DateMidnight date_selected) {

		Boolean is_continious = medication.getSchedule_medication()
				.getIs_continuous();
		DateMidnight date_end = new DateMidnight();
		if (!is_continious)
			date_end = medication
					.getSchedule_medication()
					.getDate_start()
					.plusDays(
							medication.getSchedule_medication()
									.getNumber_of_days());

		Integer interval_day = medication.getSchedule_medication()
				.getMedication_frequency().getInterval_day();
		DateMidnight date_interval = medication.getSchedule_medication()
				.getDate_start();
		while (date_interval.toLocalDate().compareTo(date_selected.toLocalDate()) <= 0) {
			// if out duration of taking medication
			if (!is_continious && date_interval.compareTo(date_end) > 0)
				break;
			if (date_selected.toLocalDate().compareTo(date_interval.toLocalDate()) == 0)
				return true;
			date_interval = date_interval.plusDays(interval_day);
		}

		return false;
	}

	public void getAllPotentielDate() {

	}

}
