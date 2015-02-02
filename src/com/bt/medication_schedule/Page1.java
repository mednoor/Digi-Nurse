package com.bt.medication_schedule;

import java.io.File;

import org.joda.time.DateTime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.utils.Utils;

public class Page1 extends Fragment {

	private String _path;
	private ImageView _image;
	private TextView name_medication;
	LinearLayout linearLayoutTakePhotoMedication;
	private static final String IMAGE_NAME = "image_name";
	Bitmap bitmap;
	TextView text_take_photo_medication;
	String name_picture;
	ScheduleMedicationActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment1, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		linearLayoutTakePhotoMedication = (LinearLayout) getActivity()
				.findViewById(R.id.linearLayoutTakePhotoMedication);
		_image = (ImageView) getActivity().findViewById(R.id.image_medication);
		name_medication = (TextView) getActivity().findViewById(
				R.id.name_medication);
		text_take_photo_medication = (TextView) getActivity().findViewById(
				R.id.text_take_photo_medication);
		if (savedInstanceState != null){
			if(savedInstanceState.getString(Page1.IMAGE_NAME)!=null){
				name_picture = savedInstanceState.getString(IMAGE_NAME);
				_path = Utils.PATH_PICTURE_MEDICATION + name_picture;
				onPhotoTaken();
			}
		}
			
		linearLayoutTakePhotoMedication
				.setOnClickListener(new ButtonClickHandler());
		activity = (ScheduleMedicationActivity) getActivity();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		if(name_picture!=null)
		{
			outState.putString(IMAGE_NAME, name_picture);
		}
		
		
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			// updateObservateur();
			startCameraActivity();
			// checkInput();
		}
	}

	public Boolean checkInput() {
		Boolean is_ok = true;
		if (name_medication.length() == 0) {
			name_medication.setError("Ce champs est obligatoire");
			is_ok = false;
		}

		return is_ok;
	}

	protected void startCameraActivity() {
		DateTime now = new DateTime();
		name_picture = "medication_" + now.getMillis();
		_path = Utils.PATH_PICTURE_MEDICATION + name_picture;
		
		File file = new File(_path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		save();
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 0:
			break;

		case -1:
			onPhotoTaken();
			break;
		}
	}

	/*
	 * @Override public void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); if (_taken) _image.setImageBitmap(bitmap); }
	 */

	protected void onPhotoTaken() {
		/*
		 * BitmapFactory.Options options = new BitmapFactory.Options();
		 * options.inSampleSize = 2; Bitmap bitmap = BitmapFactory.decodeFile(
		 * _path, options );
		 */
		
		bitmap = Utils.decodeFile(_path);
		if(bitmap!=null){
			text_take_photo_medication.setVisibility(View.GONE);
			_image.setBackgroundResource(0);

			FeedMedicine.getInstance().getMedication().setPicture(name_picture);
			FeedMedicine.getInstance().getMedication().setBitmap(bitmap);
			_image.setImageBitmap(bitmap);
		}
	}

	public void save() {
 		if ((activity!=null)&&(activity.findViewById(R.id.fragment_step1) != null)) {
			Medication medication = FeedMedicine.getInstance().getMedication();
			medication.setName(name_medication.getText().toString());
			if (bitmap!=null)
				medication.setPicture(name_picture);

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Medication medication = FeedMedicine.getInstance().getMedication();
		if (medication != null) {
			name_medication.setText(medication.getName());
			if (medication.getPicture().isEmpty()) {// if no image, put image by
				// default question market
				_image.setBackgroundResource(R.drawable.pill_question_mark);
				text_take_photo_medication.setVisibility(View.VISIBLE);
			} else {
				_image.setImageBitmap(medication.getBitmap());
				text_take_photo_medication.setVisibility(View.GONE);
			}

		}

	}
}
