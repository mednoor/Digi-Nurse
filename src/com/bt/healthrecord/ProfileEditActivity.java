package com.bt.healthrecord;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.bt.healthrecordDAO.ProfileDAO;
import com.bt.healthrecordDAO.SizeDAO;
import com.bt.healthrecordDAO.WeightDAO;
import com.bt.healthrecordMODEL.Profile;
import com.bt.healthrecordMODEL.Size;
import com.bt.healthrecordMODEL.Weight;
import com.bt.utils.Utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileEditActivity extends CommonActivity {

	/* Declare resources */
	HashMap<String, Bitmap> image = new HashMap<String, Bitmap>();
	TextView birthdayTextView;
	EditText firstNameEditText, lastNameEditText, doctorEditText, sizeEditText, weightEditText;
	Spinner civilitySpinner, bloodTypeSpinner, sizeSpinner, weightSpinner;
	Button saveBtn;
	ImageView pictureImageView;

	String currentDate, picturePath="";
	LinearLayout LinearLayoutPerson, LinearLayoutBirthday;

	Profile profile;
	Size size;
	Weight weight;

	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 1888;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit);

		/* Retrieve buttons and layout from layout */
		getResource();

		/* Retrieve List Array-string from value/string and implement it in the spinners >>> */
		/* Civility Spinner */
		ArrayAdapter<CharSequence> adapterCivility = ArrayAdapter.createFromResource(this, R.array.list_civility, android.R.layout.simple_spinner_dropdown_item);
		adapterCivility.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		civilitySpinner.setAdapter(adapterCivility);

		/* Blood Type Spinner */
		ArrayAdapter<CharSequence> adapterBloodType = ArrayAdapter.createFromResource(this, R.array.list_bloodType, android.R.layout.simple_spinner_dropdown_item);
		adapterBloodType.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		bloodTypeSpinner.setAdapter(adapterBloodType);

		/* Size Spinner */
		ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this, R.array.list_size, android.R.layout.simple_spinner_dropdown_item);
		adapterSize.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sizeSpinner.setAdapter(adapterSize);

		/* Weight Spinner */
		ArrayAdapter<CharSequence> adapterWeight = ArrayAdapter.createFromResource(this, R.array.list_weight, android.R.layout.simple_spinner_dropdown_item);
		adapterWeight.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		weightSpinner.setAdapter(adapterWeight);
		/* <<< */

		/* Profile table */
		ProfileDAO profileDAO = new ProfileDAO(this);
		profileDAO.open();
		profile = profileDAO.getInfoProfile();
		profileDAO.close();

		/* Size table */
		SizeDAO sizeDAO = new SizeDAO(this);
		sizeDAO.open();
		size = sizeDAO.getInfoSize();
		sizeDAO.close();

		/* Weight table */
		WeightDAO weightDAO = new WeightDAO(this);
		weightDAO.open();
		weight = weightDAO.getInfoWeight();
		weightDAO.close();

		birthdayTextView.setText(profile.getBirthDay());
		firstNameEditText.setText(profile.getFirstName());
		lastNameEditText.setText(profile.getLastName());
		doctorEditText.setText(profile.getDoctor());

		sizeEditText.setText(size.getSize());
		weightEditText.setText(weight.getWeight());

		/* Set height and photo to the image view */
		pictureImageView.getLayoutParams().height = 200;

		/* NO create a new bitmap if exist  */
		if(image.containsKey(profile.getImgLink())){
			pictureImageView.setImageBitmap(image.get(profile.getImgLink()));
		}else{
			Bitmap bitmap = Utils.decodeFile(profile.getImgLink());
			pictureImageView.setImageBitmap(bitmap);
			image.put(profile.getImgLink(), bitmap);
		}
		picturePath = profile.getImgLink();

	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.saveBtn:
			/* Save all data to DB profile */
			String civilitySpinnerString = ((TextView) civilitySpinner.getSelectedView()).getText().toString(); // Retrieve and cast civility spinner
			String firstNameString = firstNameEditText.getText().toString(); // Retrieve the first name
			String lasteNameString = lastNameEditText.getText().toString(); // Retrieve the last name
			String birthdayString = birthdayTextView.getText().toString(); // Retrieve the birthday
			String sizeString = sizeEditText.getText().toString(); // Retrieve the size for size table
			String sizeUnitString = ((TextView) sizeSpinner.getSelectedView()).getText().toString(); // Retrieve the unit size
			String weightString = weightEditText.getText().toString(); // Retrieve the weight for weight table
			String weightUnitString = ((TextView) weightSpinner.getSelectedView()).getText().toString(); // Retrieve the unit weight
			String doctorString = doctorEditText.getText().toString(); // Retrieve the doctor name
			String bloodTypeString = ((TextView) bloodTypeSpinner.getSelectedView()).getText().toString(); // Retrieve and cast blood type spinner
			String imgLink = picturePath; // Retrieve the link of picture take
			String ErrorForm = getResources().getString(R.string.error); // Retrieve error

			/* 
			 * Check if the input filed is empty
			 * Show the error alert if true
			 */
			if ( firstNameString.length() == 0 )
				firstNameEditText.setError(ErrorForm);
			else if ( lasteNameString.length() == 0 )
				lastNameEditText.setError(ErrorForm);
			else if ( sizeString.length() == 0 )
				sizeEditText.setError(ErrorForm);
			else if ( weightString.length() == 0 )
				weightEditText.setError(ErrorForm);
			else if ( doctorString.length() == 0 )
				doctorEditText.setError(ErrorForm);
			else {

				/* Get current date */
				getCurrentDate();

				/* Add New Profile */
				profile.setFirstName(firstNameString);
				profile.setBirthDay(birthdayString);
				profile.setBloodType(bloodTypeString);
				profile.setCivility(civilitySpinnerString);
				profile.setDoctor(doctorString);
				profile.setImgLink(imgLink);
				profile.setLastName(lasteNameString);

				ProfileDAO profileDAO = new ProfileDAO(this);
				profileDAO.open();
				profileDAO.updateProfile(profile);
				profileDAO.close();

				/* Add New Size */
				size.setSize(sizeString);
				size.setUnit(sizeUnitString);
				size.setDate(currentDate);

				SizeDAO sizeDAO = new SizeDAO(this);
				sizeDAO.open();
				sizeDAO.updateSize(size);
				sizeDAO.close();

				/* Add New Weight */
				weight.setWeight(weightString);
				weight.setUnit(weightUnitString);
				weight.setDate(currentDate);

				WeightDAO weightDAO = new WeightDAO(this);
				weightDAO.open();
				weightDAO.updateWeight(weight);
				weightDAO.close();


				this.callHomeActivity(view);
			}

			break;

		case R.id.LinearLayoutBirthday :
			/* launch date picker dialog */
			showDatePickerDialog(view);
			break;
		case R.id.PictureImageView :
			/* launch picture dialog */
			showPictureAlertDialog();
			break;
		}
	}

	private void getResource() {
		birthdayTextView = (TextView) findViewById(R.id.birthDay);
		firstNameEditText = (EditText) findViewById(R.id.firstName);
		lastNameEditText = (EditText) findViewById(R.id.lastName);
		doctorEditText = (EditText) findViewById(R.id.doctor);
		sizeEditText = (EditText) findViewById(R.id.size);
		weightEditText = (EditText) findViewById(R.id.weight);
		civilitySpinner = (Spinner) findViewById(R.id.civilitySpinner);
		bloodTypeSpinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
		sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
		weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
		pictureImageView = (ImageView) findViewById(R.id.PictureImageView);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		LinearLayoutPerson = (LinearLayout) findViewById(R.id.LinearLayoutPerson);
		LinearLayoutBirthday = (LinearLayout) findViewById(R.id.LinearLayoutBirthday);
		
	}

	private void getCurrentDate () {

		/* Use the current date as the default date in the picker */
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		String languagePhone = Locale.getDefault().getLanguage();

		if (languagePhone.toString().equals("fr")) {
			// Return the data of the datePicker to the parent activity in French format
			currentDate = day+"/"+(month+1)+"/"+year;
		} else {
			// Return the data of the datePicker to the parent activity in English format
			currentDate = (month+1)+"/"+day+"/"+year;
		}
	}

	/* Launch when the user click on the birthdayBtn */
	public void showDatePickerDialog(View v) {
		ProfileAddNewDatePickerFragment newFragment = new ProfileAddNewDatePickerFragment();
		newFragment.setDate(birthdayTextView); // show the date in text view birthday
		newFragment.show(getFragmentManager(), "datePicker");
	}

	/* Launch the alert dialog when the user click on the pictureBtn */
	public void showPictureAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileEditActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle(R.string.take_photo_alert_dialog);

		// Setting Positive "choose" Btn
		alertDialog.setPositiveButton(R.string.take_photo_alert_dialog_choose,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
			}
		});

		// Setting Negative "take" Btn
		alertDialog.setNegativeButton(R.string.take_photo_alert_dialog_take,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});

		// Showing Alert Dialog
		alertDialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			/* Path of the picture selected from the gallery */
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			/* set height of the layout on the imageview */
			pictureImageView.getLayoutParams().height = LinearLayoutPerson.getHeight();

			/* insert the img loaded in the imageview */
			pictureImageView.setImageBitmap(Utils.decodeFile(picturePath));

			Toast.makeText(getApplicationContext(), R.string.take_photo_loaded, Toast.LENGTH_LONG).show();
			Log.d("URI Loaded",picturePath);

		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {

			Uri uri = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};

			Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
			if (cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
			}
			cursor.close();

			/* set height of the layout on the imageview */
			pictureImageView.getLayoutParams().height = LinearLayoutPerson.getHeight();
			/* insert the img loaded in the imageview */
			pictureImageView.setImageBitmap(Utils.decodeFile(picturePath));

			Toast.makeText(getApplicationContext(), R.string.take_photo_loaded, Toast.LENGTH_LONG).show();
			Log.d("URI Taked",picturePath);
		}

	}
	@Override
	public int getTitleActionBar() {
		// TODO Auto-generated method stub
		return R.string.edit_profile;
	}

	@Override
	public int getIconActionBar() {
		// TODO Auto-generated method stub
		return R.drawable.ic_action_bar_profile;
	}

	@Override
	public int getColorActionBar() {
		// TODO Auto-generated method stub
		return R.color.profileBg;
	}

	@Override
	public boolean getHomeButtonActionBar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getUpNavigation() {
		// TODO Auto-generated method stub
		return true;
	}

	/* Return to parent activity */
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(), ProfileActivity.class);
		startActivity(objIntent);
	}
}
