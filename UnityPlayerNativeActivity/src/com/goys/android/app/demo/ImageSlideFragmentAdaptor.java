package com.goys.android.app.demo;

import com.goys.android.app.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.goys.android.app.GOYSApplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImageSlideFragmentAdaptor extends FragmentPagerAdapter implements
		IconPagerAdapter {


	private int[] Images = new int[] { R.drawable.demo_lets_take_a_tour,
			R.drawable.demo_choose_menu, R.drawable.demo_select_eservices,
			R.drawable.demo_initiate_form, R.drawable.demo_settings };

	protected static final int[] ICONS = new int[] {
			R.drawable.abc_ab_share_pack_holo_dark,
			R.drawable.abc_ab_share_pack_holo_dark,
			R.drawable.abc_ab_share_pack_holo_dark,
			R.drawable.abc_ab_share_pack_holo_dark,
			R.drawable.abc_ab_share_pack_holo_dark };

	private int mCount = Images.length;

	public ImageSlideFragmentAdaptor(FragmentManager fm) {
		super(fm);

		if(!GOYSApplication.getInstance().isEnglishLanguage()){
			Images = new int[] {R.drawable.demo_settings,
					R.drawable.demo_initiate_form,
					 R.drawable.demo_select_eservices,
					R.drawable.demo_choose_menu,R.drawable.demo_lets_take_a_tour };
		}
	}

	@Override
	public Fragment getItem(int position) {
		return new ImageSlideFragment(Images[position]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index % ICONS.length];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}