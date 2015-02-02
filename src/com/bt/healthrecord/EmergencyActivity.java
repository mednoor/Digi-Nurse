package com.bt.healthrecord;

import java.util.List;

import com.bt.healthrecordDAO.Emergency;
import com.bt.healthrecordDAO.EmergencyDAO;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EmergencyActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_activity);

		EmergencyDAO emergencyDao = new EmergencyDAO(getApplicationContext());
		emergencyDao.open();
		List<Emergency> emergencyList =  emergencyDao.getAllEmergency();
		emergencyDao.close();
		if(emergencyList!=null) {
			ListView lv = (ListView) findViewById(R.id.emergencyListView);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					Emergency emergency = (Emergency) parent.getItemAtPosition(position);
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergency.getTel()));
					startActivity(intent); 
				}

			});

			EmergencyCustomArrayAdapter adapter = new EmergencyCustomArrayAdapter(EmergencyActivity.this,emergencyList); 
			lv.setAdapter(adapter);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_emergency, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Menu add note */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_emergency:
			goToAddEmergency();
			return true;
		}
		return false;
	}

	private void goToAddEmergency() {
		/* Go to note activity class */
		Intent intent = new Intent(this, EmergencyAddActivity.class);
		startActivity(intent);
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
