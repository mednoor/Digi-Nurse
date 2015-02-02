package com.bt.medication_schedule;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.medication_byday.MedicationToTake;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_schedule.Page5.OnRecapSelectedListener;
import com.bt.utils.Utils;

public class ScheduleMedicationActivity extends CommonActivity implements
		OnRecapSelectedListener {

	private FragmentPagerAdapter mPagerAdapter;
	MyViewPager pager;
	ActionBar.TabListener tabListener;
	ActionBar actionBar;
	List<Fragment> fragments;
	Integer indice_onglet = 0;
	Integer last_indice_onglet = -1;
	String[] title_tabs;
	Integer[] icons_tabs;
	Boolean editionMode = false;

	MenuItem menu_item_delete;
	MenuItem menu_item_back;
	MenuItem menu_item_next;
	MenuItem menu_item_save;
	final int number_tabs = 5;

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub

		return super.onCreateView(parent, name, context, attrs);
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * Boolean is_ok = true; if (indice_onglet == 0) { is_ok = ((Page1)
	 * fragments.get(indice_onglet)).checkInput(); }
	 * pager.setPagingEnabled(is_ok); return super.onTouchEvent(event); }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.medication_schedule);

		title_tabs = new String[] { "Nom & Apparence", "Durée", "Temps & Jours", "Dosages & Instructions", "Recapitulatif" };
		//icons_tabs = new Integer[] { R.drawable.step1,R.drawable.step1,R.drawable.step1,R.drawable.step1,R.drawable.step1 };

		Medication medication_edit = (Medication) getIntent()
				.getSerializableExtra("Medication");

		Medication medication = FeedMedicine.getInstance().getMedication();

		if (medication_edit == null) {
			medication = new Medication();
		} else {
			medication = medication_edit;
			medication.createBitmap();
			editionMode = true;
		}
		
		FeedMedicine.getInstance().setMedication(medication);

		fragments = new ArrayList<Fragment>();
		fragments.add(new Page1());
		fragments.add(new Page2());
		fragments.add(new Page3());
		fragments.add(new Page4());
		fragments.add(new Page5());

		// DateTime now = new DateTime();

		this.mPagerAdapter = new MyPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		pager = (MyViewPager) super.findViewById(R.id.viewpager);

		pager.setAdapter(this.mPagerAdapter);
		// pager.setOffscreenPageLimit(5);
		// pager.setPagingEnabled(false);

		/*
		 * pager.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub
		 * 
		 * Boolean is_ok = true; if (indice_onglet == 0) { is_ok = ((Page1)
		 * fragments.get(indice_onglet)).checkInput(); } if (is_ok) ((TextView)
		 * findViewById(R.id.name_medication)) .setError(null);
		 * 
		 * if (indice_onglet == 0) { pager.setPagingEnabled(is_ok); } return
		 * false; } });
		 */
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			/*
			 * public void onPageScrollStateChanged(int state) { Boolean is_ok =
			 * true; if (indice_onglet == 0) { is_ok = ((Page1)
			 * fragments.get(indice_onglet)).checkInput(); }
			 * pager.setPagingEnabled(is_ok);
			 * 
			 * }
			 */
			/*
			 * @Override public void onPageScrollStateChanged(int state) { //
			 * TODO Auto-generated method stub Log.d("tabulation",
			 * "onPageScrollStateChanged ====>  " + state);
			 * super.onPageScrollStateChanged(state); }
			 */

			public void onPageSelected(int position) {
				Log.d("tabulation", "onPageSelected ====>  " + indice_onglet);

				saveDataFragment();

				// if not all tab created yet
				if (position > indice_onglet
						&& actionBar.getTabCount() != number_tabs
						&& actionBar.getTabCount() == indice_onglet + 1) {
					addTabStep();
				}

				getActionBar().setSelectedNavigationItem(position);
				handlingButtonActionBar(position);
				indice_onglet = position;

			}

		});

		actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				Log.d("tabulation",
						"onTabUnselected  ===> " + arg0.getPosition());
				saveDataFragment();
			}

			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				pager.setCurrentItem(arg0.getPosition());

				Log.d("tabulation", "onTabSelected  ===> " + arg0.getPosition());
			}

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				Log.d("tabulation",
						"onTabReselected  ===> " + arg0.getPosition());
			}
		};

		/*
		 * actionBar.addTab(actionBar.newTab().setText("2/5")
		 * .setTabListener(tabListener)
		 * .setIcon(android.R.drawable.arrow_up_float));
		 * actionBar.addTab(actionBar.newTab().setText("3/5")
		 * .setTabListener(tabListener)
		 * .setIcon(android.R.drawable.ic_media_play));
		 */

	}

	// save data form when we quit one fragment
	public void saveDataFragment() {
		if (last_indice_onglet != indice_onglet) {
			switch (indice_onglet) {
			case Utils.FRAG_PHOTO:
				((Page1) mPagerAdapter.getItem(indice_onglet)).save();
				break;
			case Utils.FRAG_DURATION:
				((Page2) mPagerAdapter.getItem(indice_onglet)).save();
				break;
			case Utils.FRAG_HOURS:
				((Page3) mPagerAdapter.getItem(indice_onglet)).save();
				break;
			case Utils.FRAG_QUANTITY:
				((Page4) mPagerAdapter.getItem(indice_onglet)).save();
				break;
			}
		}
		last_indice_onglet = indice_onglet;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_schedule_medication, menu);

		menu_item_delete = menu.findItem(R.id.delete_medication);
		menu_item_back = menu.findItem(R.id.back);
		menu_item_next = menu.findItem(R.id.next);
		menu_item_save = menu.findItem(R.id.save);
		menu_item_delete = menu.findItem(R.id.delete_medication);

		menu_item_delete.setVisible(false);
		menu_item_save.setVisible(false);

		if (indice_onglet == 0) {
			menu_item_back.setVisible(false);
		} else {
			menu_item_back.setVisible(true);
		}

		if (editionMode)
			displayAllStep();
		else {
			actionBar.addTab(actionBar.newTab().setText("1/5")
					.setTabListener(tabListener)
					 );
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.next:
			saveDataFragment();
			pager.setCurrentItem(indice_onglet + 1);
			return true;
		case R.id.back:
			pager.setCurrentItem(indice_onglet - 1);
			return true;
		case R.id.save:
			btnSave();
			return true;
		case R.id.delete_medication:
			createDialogDeleteMedication();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void createDialogDeleteMedication() {
		// Create out AlterDialog
		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete?");
		String name_medication = FeedMedicine.getInstance().getMedication()
				.getName();
		builder.setTitle(name_medication);
		builder.setCancelable(true);
		builder.setPositiveButton("Yes", new OkDeletingOnClickListener());
		builder.setNegativeButton("No", new CancelDeletingOnClickListener());
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private final class CancelDeletingOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}

	private final class OkDeletingOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			deleteMedication();
		}
	}

	public void deleteMedication() {
		FeedMedicine.getInstance().deleteMedication(getApplicationContext());
		goToActivityMedicationByDay();
	}

	private void btnSave() {
		if (!editionMode)
			FeedMedicine.getInstance().saveAll(getApplicationContext());
		else {
			FeedMedicine.getInstance()
					.updateMedication(getApplicationContext());
		}
		goToActivityMedicationByDay();

	}

	private void goToActivityMedicationByDay() {
		// after save go to the list of medication by day
		Intent intent_list_medication_date = new Intent(
				getApplicationContext(), MedicationToTake.class);
		startActivity(intent_list_medication_date);
	}

	private void displayAllStep() {

		for (int i = 0; i < number_tabs; i++) {
			actionBar.addTab(actionBar.newTab().setText(title_tabs[i])
					.setTabListener(tabListener)
					);
			
		}
		indice_onglet = number_tabs - 1;
		// go to the summary last tab
		pager.setCurrentItem(number_tabs - 1);
		// handlingButtonActionBar();
	}

	private void addTabStep() {

		if (actionBar.getTabCount() != number_tabs
				&& actionBar.getTabCount() == indice_onglet + 1) {
			actionBar.addTab(actionBar.newTab()
					.setText(title_tabs[indice_onglet + 1])
					.setTabListener(tabListener));
		}
	}

	private void handlingButtonActionBar(int position) {

		menu_item_save.setVisible(false);
		if (position == 0) {
			menu_item_back.setVisible(false);
			menu_item_next.setVisible(true);
		} else {
			menu_item_back.setVisible(true);
			menu_item_next.setVisible(true);
		}

		// if on the last tab
		// if (actionBar.getTabCount() == number_tabs && actionBar.getTabCount()
		// == indice_onglet + 1) {
		if (position == number_tabs - 1) {
			menu_item_next.setVisible(false);
			menu_item_save.setVisible(true);
			if (editionMode)
				menu_item_delete.setVisible(true);
		}
	}

	@Override
	public void goToStep(int number_of_step) {
		pager.setCurrentItem(number_of_step);
	}

	@Override
	public int getTitleActionBar() {
		return R.string.drug;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_pillbox;
	}

	@Override
	public int getColorActionBar() {
		return R.color.drugBg;
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
