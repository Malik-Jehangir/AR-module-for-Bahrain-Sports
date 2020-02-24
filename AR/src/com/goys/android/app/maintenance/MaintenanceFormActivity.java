package com.goys.android.app.maintenance;

import java.util.ArrayList;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.R.id;
import com.goys.android.app.R.layout;
import com.goys.android.app.R.menu;
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.util.CustomViewPager;
import com.goys.android.app.util.MyAttachmentAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment.SavedState;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/*
 * 
 * This class represents SnapFix Form page screen. This class hold two fragments for
 * Public and Club User Forms
 * 
 */
public class MaintenanceFormActivity extends ActionbarActivity {

	ImageButton btnPublicUser, btnClubUser;

	CustomViewPager pager;

	int viewPagerId = -1;
	
	public static boolean isGovernorateEnable = false;

	public static String[] governorateList = null;

	MaintenanceFormPagerAdaptor maintenancePagerAdaptor;

	MaintenanceClubUserFragment fragmentClub;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		setActionbarTitle(getResources().getString(R.string.menu_snapfix_form));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));

		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			if (savedInstanceState.getInt("ViewPagerId", -1) != -1) {
				viewPagerId = savedInstanceState.getInt("ViewPagerId", -1);
			}

			String fileUri = savedInstanceState.getString("file-uri");
			if (fileUri != null) {
				savedInstanceState.putString("file-uri", fileUri);

			}

			if (savedInstanceState.getParcelableArrayList("attached_files") != null) {
				ArrayList<Attachment> attachmentList = savedInstanceState
						.getParcelableArrayList("attached_files");
				savedInstanceState.putParcelableArrayList("attached_files",
						attachmentList);
				MaintenancePublicUserFragment f = new MaintenancePublicUserFragment();
				f.setArguments(savedInstanceState);
				getSupportFragmentManager().saveFragmentInstanceState(f);
			}
		}

		setContentView(R.layout.activity_maintenance_form);

		initViews();

	}

	@SuppressWarnings("deprecation")
	private void initViews() {

		btnPublicUser = (ImageButton) findViewById(R.id.btn_public);
		btnClubUser = (ImageButton) findViewById(R.id.btn_club);

		pager = (CustomViewPager) findViewById(R.id.pager);

		if (viewPagerId == 0) {
			pager.setCurrentItem(viewPagerId);
			btnPublicUser.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_public_user_blue));
			btnClubUser.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_club_user_grey));
			
		} else if (viewPagerId == 1) {
			pager.setCurrentItem(viewPagerId);
			btnPublicUser.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_public_user_grey));
			btnClubUser.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_club_user_blue));
			
		}

		
		pager.setOffscreenPageLimit(0);

		maintenancePagerAdaptor = new MaintenanceFormPagerAdaptor(
				getSupportFragmentManager());
		pager.setAdapter(maintenancePagerAdaptor);
		pager.setPagingEnabled(false);
		

		btnPublicUser.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				btnPublicUser.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_public_user_blue));
				btnClubUser.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_club_user_grey));

				pager.setCurrentItem(0);

			}
		});

		btnClubUser.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				btnPublicUser.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_public_user_grey));
				btnClubUser.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_club_user_blue));

				pager.setCurrentItem(1);
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("ViewPagerId", pager.getCurrentItem());

	}

}
