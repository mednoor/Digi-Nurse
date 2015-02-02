package com.bt.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateMidnight;
import org.joda.time.LocalTime;
import org.joda.time.base.BaseDateTime;
import org.joda.time.format.DateTimeFormat;

import com.bt.healthrecord.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class Utils {

	// Medication schedule
	public static final int FRAG_PHOTO = 0;
	public static final int FRAG_DURATION = 1;
	public static final int FRAG_HOURS = 2;
	public static final int FRAG_QUANTITY = 3;
	public static final int FRAG_RECAP = 4;

	public static final int MATIN = 1;
	public static final int MIDDAY = 2;
	public static final int EVENING = 3;

	public static final String PATH_PICTURE_MEDICATION = Environment
			.getExternalStorageDirectory() + "/DCIM/Medication/";

	public static int[] drawables = new int[] { R.drawable.eye,
			R.drawable.cardiology, R.drawable.dentistry,
			R.drawable.pediatrics,
			R.drawable.general_practice, R.drawable.ear,
			R.drawable.neurology,R.drawable.sexology,
			R.drawable.gastroenterology,
			R.drawable.other_speciality };

	public static String displayDate(BaseDateTime date, Context context) {

		String current = context.getResources().getConfiguration().locale
				.getLanguage();
		String format;

		if (current.equals(Locale.ENGLISH.toString())) {
			format = "yyyy/MM/dd";
		} else {
			format = "dd/MM/yyyy";
		}

		return date.toString(DateTimeFormat.forPattern(format));
	}

	public static String displayHour(LocalTime hour, Context context) {

		String current = context.getResources().getConfiguration().locale
				.getLanguage();
		String format;

		if (current.equals(Locale.ENGLISH.toString())) {
			format = "hh:mm aa";
		} else {
			format = "HH:mm";
		}

		return hour.toString(DateTimeFormat.forPattern(format));
	}

	public static String displayDayWeek(Context context, String day_week) {

		String[] bases = context.getResources().getStringArray(
				R.array.DayOfWeek);

		List<String> list_day_week_text = new ArrayList<String>();
		String[] day_week_separated = day_week.split(",");
		for (int i = 0; i < day_week_separated.length; i++) {
			list_day_week_text.add(bases[Integer
					.parseInt(day_week_separated[i]) - 1]);
		}

		return Utils.implode(", ", list_day_week_text);
	}

	public static <T> String implode(String glue, List<T> list) {
		// list is empty, return empty string
		if (list == null || list.isEmpty()) {
			return "";
		}

		Iterator<T> iter = list.iterator();

		// init the builder with the first element
		StringBuilder sb = new StringBuilder();
		sb.append(iter.next());

		// concat each element
		while (iter.hasNext()) {
			sb.append(glue).append(iter.next());
		}

		// return result
		return sb.toString();
	}

	public static String getStringResourceByName(Context context, String aString) {
		String packageName = context.getPackageName();
		int resId = context.getResources().getIdentifier(aString, "string",
				packageName);
		return context.getString(resId);
	}

	public static Bitmap decodeFile(String path) {// you can provide file path
		// here
		int orientation;

		if (path == null) {
			return null;
		}
		// decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		// Find the correct scale value. It should be the power of 2.
		// decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = 6;
		Bitmap bm = BitmapFactory.decodeFile(path, o2);
		
		Bitmap bitmap = bm;
		try {
			ExifInterface exif = new ExifInterface(path);

			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			Log.e("ExifInteface .........", "rotation =" + orientation);

			// exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				// m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;

	}

}
