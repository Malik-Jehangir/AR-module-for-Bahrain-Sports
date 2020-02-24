package com.goys.android.app.demo;

import com.goys.android.app.GOYSApplication;
import com.goys.android.app.MainActivity;
import com.goys.android.app.R;
import com.viewpagerindicator.CirclePageIndicator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/*
 * This class represents Application Demo page screen
 * which will appear only the first time when application run.
 * 
 */
public class DemoActivity extends ActionBarActivity {

	ViewPager mPager;
	CirclePageIndicator mIndicator;
	ImageSlideFragmentAdaptor imageSlideAdaptor;
	ImageButton btnStartHome;
	int selectedIndex;
	boolean mPageEnd = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		initView();

	}

	public void initView() {

		imageSlideAdaptor = new ImageSlideFragmentAdaptor(
				getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);

		mPager.setAdapter(imageSlideAdaptor);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setRadius(20);
		mIndicator
				.setFillColor(getResources().getColor(R.color.action_bar_red));
		mIndicator.setPageColor(getResources().getColor(R.color.gray_2));

		mIndicator.setViewPager(mPager);
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			mIndicator.setCurrentItem(4);
			selectedIndex = 4;
		}

		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				Log.d("Position", position + "");
				selectedIndex = position;

			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {

				Log.d("onPageScrolled", position + "");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

				if (arg0 == 0) {
					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						if (selectedIndex == 4 && mPageEnd) {
							mPageEnd = false;

							Intent i = new Intent(DemoActivity.this,
									MainActivity.class);
							startActivity(i);
							finish();
						} else {
							mPageEnd = false;
						}
					} else {
						if (selectedIndex == 0 && mPageEnd) {
							mPageEnd = false;

							Intent i = new Intent(DemoActivity.this,
									MainActivity.class);
							startActivity(i);
							finish();
						} else {
							mPageEnd = false;
						}
					}

				}

				Log.d("onPageScrollStateChanged", arg0 + "");
				if (GOYSApplication.getInstance().isEnglishLanguage()) {
					if (selectedIndex == imageSlideAdaptor.getCount() - 1) {
						mPageEnd = true;
					}
				} else {
					if (selectedIndex == 0) {
						mPageEnd = true;
					}
				}
			}
		});

		btnStartHome = (ImageButton) findViewById(R.id.btn_home);
		btnStartHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(DemoActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

}
