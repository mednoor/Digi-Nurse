package com.bt.medication_byday;

import org.joda.time.DateMidnight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.medication_listing.MedicationListing;
import com.bt.medication_schedule.ScheduleMedicationActivity;
import com.bt.utils.Utils;

public class MedicationToTake extends CommonActivity {

    ListView lvListMorningMedication;
    ListView lvListMiddayMedication;
    ListView lvListEveningMedication;
    ListView lvListSunsetMedication;

    TextView textViewDateChoosed;

    CustomMedicationDayArrayAdapter adapterListview;

    LinearLayout linearLayoutMorning;
    LinearLayout linearLayoutMidday;
    LinearLayout linearLayoutEvening;

    Button nextDay;
    Button previousDay;

    static DateMidnight date_choosed;

    Button button_add_medication;
    Button button_listing_medication;

    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private static final int NUMBER_OF_PAGES = 100;
    private static final int MIDDLE_NUMBER_OF_PAGES = 50;
    Integer current_position = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);

	setContentView(R.layout.medication_to_take);
	date_choosed = new DateMidnight();

	textViewDateChoosed = (TextView) findViewById(R.id.textViewDateChoosed);
	textViewDateChoosed.setText(Utils.displayDate(date_choosed, getApplicationContext()));

	nextDay = (Button) findViewById(R.id.buttonNextDay);
	previousDay = (Button) findViewById(R.id.buttonPreviousDay);
	button_add_medication = (Button) findViewById(R.id.button_add_medication);
	button_listing_medication = (Button) findViewById(R.id.button_listing_medication);

	nextDay.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		mViewPager.setCurrentItem(current_position+1);
	    }
	});

	previousDay.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		mViewPager.setCurrentItem(current_position-1);
	    }
	});

	button_add_medication.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(getApplicationContext(), ScheduleMedicationActivity.class);
		startActivity(i);
	    }
	});

	button_listing_medication.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(getApplicationContext(), MedicationListing.class);
		startActivity(i);
	    }
	});

	mViewPager = (ViewPager) findViewById(R.id.viewpager);
	mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
	mViewPager.setAdapter(mMyFragmentPagerAdapter);
	mViewPager.setCurrentItem(current_position);

	mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	    public void onPageSelected(int position) {
		current_position = position;
		textViewDateChoosed.setText(Utils.displayDate(calculateDateSlide(position), getApplicationContext()));
	    }
	});

    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	public MyFragmentPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int index) {

	    return PageFragment.newInstance(calculateDateSlide(index));
	}

	@Override
	public int getCount() {

	    return NUMBER_OF_PAGES;
	}
    }

    private static DateMidnight calculateDateSlide(Integer position) {
	DateMidnight date_today = new DateMidnight();
	if (position < MIDDLE_NUMBER_OF_PAGES) {
	    date_today = date_today.minusDays(MIDDLE_NUMBER_OF_PAGES - position);
	}
	if (position > MIDDLE_NUMBER_OF_PAGES) {
	    date_today = date_today.plusDays(position - MIDDLE_NUMBER_OF_PAGES);
	}

	return date_today;
    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	// loadListMedication();
    }

    /*
     * public void loadListMedication() {
     * 
     * ArrayList<Medication> listMedicationToTaken =
     * FeedMedicine.getInstance().getMedicationForDay(date_choosed,
     * getApplicationContext());
     * 
     * ArrayList<MedicationHourDosage> listAllMedicationHourDosage = new
     * ArrayList<MedicationHourDosage>(); for (Medication medication :
     * listMedicationToTaken) { for (MedicationHourDosage medicationHourDosage :
     * medication.getSchedule_medication() .getMedication_hour_dosage_list()) {
     * 
     * listAllMedicationHourDosage.add(medicationHourDosage);
     * 
     * }
     * 
     * }
     * 
     * StickyListHeadersListView stickyList = (StickyListHeadersListView)
     * findViewById(R.id.list); stickyList.setOnItemClickListener(new
     * ItemMedicationClick()); Collections.sort(listAllMedicationHourDosage);
     * adapterListview = new CustomMedicationDayArrayAdapter(this,
     * listAllMedicationHourDosage); stickyList.setAdapter(adapterListview);
     * 
     * }
     * 
     * public class ItemMedicationClick implements OnItemClickListener {
     * 
     * @Override public void onItemClick(AdapterView<?> parent, View view, int
     * position, long id) {
     * 
     * MedicationHourDosage medicationHourDosage = (MedicationHourDosage)
     * parent.getItemAtPosition(position);
     * 
     * Medication medication_selected =
     * medicationHourDosage.getMedication_schedule().getMedication();
     * 
     * Intent i = new Intent(getApplicationContext(),
     * ScheduleMedicationActivity.class); i.putExtra("Medication",
     * medication_selected); startActivity(i);
     * 
     * }
     * 
     * }
     */

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
