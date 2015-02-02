package com.bt.consultation;

import com.bt.caldroid.CaldroidFragment;
import com.bt.caldroid.CaldroidGridAdapter;


public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}

}
