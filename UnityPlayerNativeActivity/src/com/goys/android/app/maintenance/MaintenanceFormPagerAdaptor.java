package com.goys.android.app.maintenance;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MaintenanceFormPagerAdaptor extends FragmentPagerAdapter {

	 
	 
	 Fragment[] fragList = null;
	
	public MaintenanceFormPagerAdaptor(FragmentManager fm) {
		super(fm);
		
		
		  /*if (GOYSApplication.langType == 0) {
	            fragList = new Fragment[] {
	                    new MaintenancePublicUserFragment(),new MaintenanceClubUserFragment()
	            };
	        } else {
	            fragList = new Fragment[] {
	            		new MaintenanceClubUserFragment(), new MaintenancePublicUserFragment()
	            };
	        }*/
		
	}

	@Override
	public Fragment getItem(int position) {
		
		//return fragList[position];
		
		switch (position) {
		case 0:
			return new MaintenancePublicUserFragment();
		case 1:
			return new MaintenanceClubUserFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

	

	
}
