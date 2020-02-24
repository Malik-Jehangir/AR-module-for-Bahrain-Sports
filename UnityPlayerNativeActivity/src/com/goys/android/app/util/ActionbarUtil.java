package com.goys.android.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.goys.android.app.settings.SettingsActivity;

public class ActionbarUtil {

	private View actionbarView;

	private RelativeLayout actionbar_header_mainlayout;

	private ActionBar actionBar;

	private Activity activity;

	private ImageButton ibActionBarMenuIcon;

	private String activity_title;

	public ISettingNotifier notifier;

	private AnimationDrawable mFrameAnimation;

	public ActionbarUtil(Context context) {
		actionbarView = ((Activity) context).getLayoutInflater().inflate(
				R.layout.actionbar_header, null);
		activity = (Activity) context;
		actionBar = ((ActionBarActivity) activity).getSupportActionBar();

		actionbar_header_mainlayout = (RelativeLayout) actionbarView
				.findViewById(R.id.actionbar_header_mainlayout);

		ibActionBarMenuIcon = (ImageButton) actionbarView
				.findViewById(R.id.actionBarMenuIcon);

		ibActionBarMenuIcon.setVisibility(View.GONE);

	}

	@SuppressLint("InlinedApi")
	public void setup(boolean isParent, View.OnClickListener listener) {
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		actionBar.setDisplayHomeAsUpEnabled(false);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		

		actionBar.setCustomView(actionbarView, lp);
		Toolbar parent = (Toolbar) actionbarView.getParent();
		parent.setContentInsetsAbsolute(0, 0);

		((ImageButton) actionbarView.findViewById(R.id.actionBarMenuIcon))
				.setOnClickListener(listener);

		if (!isParent) {
			((ImageButton) actionbarView.findViewById(R.id.actionBarMenuIcon))
					.setImageResource(R.drawable.back);
			((ImageButton) actionbarView.findViewById(R.id.actionBarMenuIcon))
					.setVisibility(View.VISIBLE);

			if (activity_title.equals(activity.getResources().getString(
					R.string.menu_settings))) {
				setSettingIcon(false);
				((ImageButton) actionbarView
						.findViewById(R.id.actionBarMenuIcon))
						.setImageResource(R.drawable.backarrow_gray);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_eservice))) {
				setSettingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_contactus))) {
				setSettingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_news))) {
				setSettingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_event_calendar_details))) {
				setLocationIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_news_details))) {
				setSharingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_app_linking_form))){
				setSettingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_tv_streaming_form))){
				setSettingIcon(true);
			} else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_event_calendar))){
				setSettingIcon(true);
			}  else if (activity_title.equals(activity.getResources().getString(
					R.string.menu_arinformaiton))){
				setSettingIcon(true);
			} 
			else{
				setSettingIcon(false);
			}

		} else {
			setSettingIcon(true);
		}

	}

	public void hide() {
		actionBar.hide();
	}

	public void setBackgroundColor(int resourceid) {
		if (resourceid == -1) {
			((TextView) actionbarView.findViewById(R.id.tvActionbarTitle))
					.setTextColor(actionbarView.getResources().getColor(
							R.color.header_title_color));

		}
		actionbar_header_mainlayout.setBackgroundColor(resourceid);
	}

	public void setTitle(String title) {
		this.activity_title = title;
		((TextView) actionbarView.findViewById(R.id.tvActionbarTitle))
				.setText(title);
	}

	public void setSharingIcon(boolean show) {

		if (show) {
			ImageView sharingImage = (ImageView) actionbarView
					.findViewById(R.id.settingImage);
			sharingImage.setImageDrawable(actionbarView.getResources()
					.getDrawable(R.drawable.share));
			sharingImage.setVisibility(View.VISIBLE);
			sharingImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					notifier.OnClick();
				}
			});
		} else {
			ImageView sharingImage = (ImageView) actionbarView
					.findViewById(R.id.settingImage);
			sharingImage.setImageDrawable(actionbarView.getResources()
					.getDrawable(R.drawable.ic_settings));
			setSettingIcon(false);
		}
	}
	
	public void setLocationIcon(boolean show) {

		if (show) {
			ImageView sharingImage = (ImageView) actionbarView
					.findViewById(R.id.settingImage);
			sharingImage.setImageDrawable(actionbarView.getResources()
					.getDrawable(R.drawable.share));
			sharingImage.setVisibility(View.VISIBLE);
			sharingImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					notifier.OnClick();
				}
			});
		} else {
			ImageView sharingImage = (ImageView) actionbarView
					.findViewById(R.id.settingImage);
			sharingImage.setImageDrawable(actionbarView.getResources()
					.getDrawable(R.drawable.ic_settings));
			setSettingIcon(false);
		}
	}

	public void setSettingIcon(boolean show) {

		if (show) {
			((ImageView) actionbarView.findViewById(R.id.settingImage))
					.setVisibility(View.VISIBLE);
			((ImageView) actionbarView.findViewById(R.id.settingImage))
					.setOnClickListener(new OnClickListener() {
					
						@Override
						public void onClick(View v) {
							notifier.OnClick();
							Intent intent = new Intent(activity
									.getApplicationContext(),
									SettingsActivity.class);
							activity.startActivity(intent);

						}
					});

		} else {
			((ImageView) actionbarView.findViewById(R.id.settingImage))
					.setVisibility(View.GONE);
		}
	}

	
	public void showLoadingBar() {
		final int totalProgressTime = 100;
		mFrameAnimation.start();
		final Thread t = new Thread() {

			@Override
			public void run() {

				int jumpTime = 0;
				while (jumpTime < totalProgressTime) {
					try {
						sleep(100);
						jumpTime += 2;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				hideLoadingBar();

			}

		};
		t.start();
	}

	public void hideLoadingBar() {

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});

	}

}
