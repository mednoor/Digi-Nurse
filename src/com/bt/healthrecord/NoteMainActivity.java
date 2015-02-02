package com.bt.healthrecord;

import java.util.ArrayList;
import java.util.List;

import com.bt.healthrecord.R;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NoteMainActivity extends CommonActivity {

	private PagerAdapter mPagerAdapter;
	// tabs
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_main_activity);
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new NoteActivity());
		fragments.add(new NoteAudioActivity());
		this.mPagerAdapter = new NoteMyPagerAdapter(super.getSupportFragmentManager(), fragments);

		// Tabs
		pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position){
				// when swiping between pages, select the corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		//Intent i = new Intent();
		final ActionBar actionBar = getActionBar();


		// Specify that tabs should be  displayed in the action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};

		actionBar.addTab(
				actionBar.newTab()
				.setText(R.string.note_tab_note)
				.setTabListener(tabListener).setIcon(android.R.drawable.ic_menu_edit));
		actionBar.addTab(
				actionBar.newTab()
				.setText(R.string.note_tab_audio)
				.setTabListener(tabListener).setIcon(android.R.drawable.ic_menu_add));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_note, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Menu add note */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.noteEcrite:
			goToNoteEcrite();
			return true;
		case R.id.noteAudio:
			goToRecordAudio();
			return true;
		}
		return false;
	}

	private void goToRecordAudio() {
		/* Go to note activity class */
		Intent intent = new Intent(this, NoteAudioAddActivity.class);
		startActivity(intent);	
	}


	private void goToNoteEcrite() {
		/* Go to note activity class */
		Intent intent = new Intent(this, NoteAddNewActivity.class);
		startActivity(intent);
	}

	@Override
	public int getTitleActionBar() {
		return R.string.note;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_notepad;
	}

	@Override
	public int getColorActionBar() {
		return R.color.noteBg;
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
