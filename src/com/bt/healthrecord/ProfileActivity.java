package com.bt.healthrecord;

import java.util.HashMap;

import com.bt.healthrecordDAO.ProfileDAO;
import com.bt.healthrecordDAO.SizeDAO;
import com.bt.healthrecordDAO.WeightDAO;
import com.bt.healthrecordMODEL.Profile;
import com.bt.healthrecordMODEL.Size;
import com.bt.healthrecordMODEL.Weight;
import com.bt.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends CommonActivity {
	TextView civilityTextView, lastNameTextView, firstNameTextView, birthdayTextView, sizeTextView, weightTextView, 
	doctorTextView, bloodTypeTextView, FCMTextView, IMCTextView, sizeUnitTextView, weightUnitTextView;

	ImageView photoImageView;
	HashMap<String, Bitmap> image = new HashMap<String, Bitmap>();
	Profile profile;
	Size size;
	Button editProfileBtn;
	Weight weight;
	String wrongUnity;

	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);

		context = getApplicationContext();
		/* Get the resources from layout */
		getResource();

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
		

		/* Set height and photo to the image view */
		photoImageView.getLayoutParams().height = 300;
		
		/* NO create a new bitmap if exist  */
		if(image.containsKey(profile.getImgLink())){
			photoImageView.setImageBitmap(image.get(profile.getImgLink()));
		}else{
			Bitmap bitmap = Utils.decodeFile(profile.getImgLink());
			photoImageView.setImageBitmap(bitmap);
			image.put(profile.getImgLink(), bitmap);
		}
		
		

		/* Set data */
		civilityTextView.setText(profile.getCivility());
		lastNameTextView.setText(profile.getLastName());
		firstNameTextView.setText(profile.getFirstName());
		birthdayTextView.setText(profile.getBirthDay());
		doctorTextView.setText(profile.getDoctor());
		bloodTypeTextView.setText(profile.getBloodType());
		
		sizeTextView.setText(size.getSize());
		sizeUnitTextView.setText(size.getUnit());

		weightTextView.setText(weight.getWeight());
		weightUnitTextView.setText(weight.getUnit());

		/* Show IMC */
		getIMC();
		
		/* Edit Button Listener */
		editProfileBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Launch intent to edit profile activity
				Intent intent = new Intent (context, ProfileEditActivity.class);
				startActivity(intent);
			}
		});
	}

	private void getIMC() {
		String sizeUnit = size.getUnit();
		String weightUnit = weight.getUnit();
		float sizeValue = 0;
		float weightValue = 0;
		if (sizeUnit.equals("cm") && weightUnit.equals("kg")) {
			sizeValue = Float.valueOf(size.getSize());
			weightValue = Float.valueOf(weight.getWeight());
			sizeValue = sizeValue / 100;
			sizeValue = (float) Math.pow(sizeValue, 2);
			float imc = weightValue / sizeValue;
			IMCTextView.setText(String.valueOf(imc));
		} else if (sizeUnit.equals("feet") && weightUnit.equals("lbs")) {
			sizeValue = Float.valueOf(size.getSize());
			weightValue = Float.valueOf(weight.getWeight());
			float sizeValueFeetToInch = (float) sizeValue * 12; // convert feet to inches
			sizeValueFeetToInch = (float) Math.pow(sizeValueFeetToInch, 2); // *2
			float imc = (weightValue / sizeValue) * 703;
			IMCTextView.setText(String.valueOf(imc));
		} else if (sizeUnit.equals("inches") && weightUnit.equals("lbs")) {
			sizeValue = Float.valueOf(size.getSize());
			weightValue = Float.valueOf(weight.getWeight());
			sizeValue = (float) Math.pow(sizeValue, 2);
			float imc = (weightValue / sizeValue) * 703;
			IMCTextView.setText(String.valueOf(imc));
		} else {
			IMCTextView.setText(R.string.wrong_unity);
			IMCTextView.setTextColor(Color.parseColor("#FF0000"));
		}
	}

	private void getResource() {

		civilityTextView = (TextView) findViewById(R.id.civilityTextView);
		lastNameTextView = (TextView) findViewById(R.id.lastNameTextView);
		firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
		birthdayTextView = (TextView) findViewById(R.id.birthdayTextView);
		doctorTextView = (TextView) findViewById(R.id.doctorTextView);
		bloodTypeTextView = (TextView) findViewById(R.id.bloodTypeTextView);
		sizeTextView = (TextView) findViewById(R.id.sizeTextView);
		weightTextView = (TextView) findViewById(R.id.weightTextView);
		IMCTextView = (TextView) findViewById(R.id.IMCTextView);
		photoImageView = (ImageView) findViewById(R.id.photoImageView);
		sizeUnitTextView = (TextView) findViewById(R.id.cmUnitTextView);
		weightUnitTextView = (TextView) findViewById(R.id.weightUnitTextView);
		editProfileBtn = (Button) findViewById(R.id.editProfileBtn);

	}

	@Override
	public int getTitleActionBar() {
		return R.string.profile;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_action_bar_profile;
	}

	@Override
	public int getColorActionBar() {
		return R.color.profileBg;
	}

	@Override
	public boolean getHomeButtonActionBar() {
		return true;
	}

	@Override
	public boolean getUpNavigation() {
		return true;
	}

}
