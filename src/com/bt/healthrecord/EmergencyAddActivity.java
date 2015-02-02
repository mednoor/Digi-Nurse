package com.bt.healthrecord;

import com.bt.healthrecordDAO.Emergency;
import com.bt.healthrecordDAO.EmergencyDAO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EmergencyAddActivity extends CommonActivity {

	/* Declare resources */
	Spinner profilSpinner;
	EditText emergencyName, emergencyTel;
	Button emergencyAdd, emergencyCancel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_add);

		getResource();

		/* Retrieve List Array-string from value/string and implement it in the spinners >>> */
		/* Emergency Spinner */
		ArrayAdapter<CharSequence> adapterEmergency = ArrayAdapter.createFromResource(this, R.array.emergency_list, android.R.layout.simple_spinner_dropdown_item);
		adapterEmergency.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		profilSpinner.setAdapter(adapterEmergency);

	}

	public void onClick (View view) {
		switch (view.getId()) {
		case R.id.emergencyAddBtn:

			/* Save all data to DB profile */
			String profilSpinnerString = ((TextView) profilSpinner.getSelectedView()).getText().toString(); // Retrieve and cast profil spinner
			String emergencyNameString = emergencyName.getText().toString(); // Retrieve name text
			String emergencyTelString = emergencyTel.getText().toString(); // Retrieve tel number
			String ErrorForm = getResources().getString(R.string.error); // Retrieve error

			/* 
			 * Check if the input filed is empty
			 * Show the error alert if true
			 */
			if ( emergencyNameString.length() == 0 )
				emergencyName.setError(ErrorForm);
			else if ( emergencyTelString.length() == 0 )
				emergencyTel.setError(ErrorForm);
			else {

				/* Add New Emergency */
				Emergency emergency = new Emergency(0, profilSpinnerString, emergencyNameString, emergencyTelString);
				EmergencyDAO emergencyDao = new EmergencyDAO(getApplicationContext());
				emergencyDao.open();
				emergencyDao.insertEmergency(emergency);
				emergencyDao.close();

				this.callHomeActivity(view);
				Toast.makeText(getApplicationContext(), R.string.record_add, Toast.LENGTH_LONG).show();

			}

			break;

		case R.id.emergencyCancelBtn:

			this.callHomeActivity(view);
			Toast.makeText(getApplicationContext(), R.string.record_cancel, Toast.LENGTH_LONG).show();

			break;
		}
	}

	private void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(), EmergencyActivity.class);
		startActivity(objIntent);
	}

	public void getResource (){
		profilSpinner = (Spinner) findViewById(R.id.profilSpinner);
		emergencyName = (EditText) findViewById(R.id.emergencyAddName);
		emergencyTel = (EditText) findViewById(R.id.emergencyAddTel);
		emergencyAdd = (Button) findViewById(R.id.emergencyAddBtn);
		emergencyCancel = (Button) findViewById(R.id.emergencyCancelBtn);
	}
	@Override
	public int getTitleActionBar() {
		return R.string.emergency;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_urgence;
	}

	@Override
	public int getColorActionBar() {
		return R.color.emergencyBg;
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
