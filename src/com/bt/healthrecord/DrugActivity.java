package com.bt.healthrecord;
 
import android.os.Bundle;

public class DrugActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drug_activity);

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
