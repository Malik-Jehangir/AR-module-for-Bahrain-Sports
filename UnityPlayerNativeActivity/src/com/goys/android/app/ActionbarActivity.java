package com.goys.android.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.goys.android.app.util.ActionbarUtil;

public class ActionbarActivity extends ActionBarActivity {

	public ActionbarUtil actionbarUtil;

	private String actionbarTitle;
	private int actionbarColor;

	private boolean isParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		setupActionbar();
	}

	protected void setActionbarTitle(String title) {
		this.actionbarTitle = title;
	}

	protected void setActionbarColor(int resoruce) {
		this.actionbarColor = resoruce;
	}

	protected void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	protected void setupActionbar() {
		actionbarUtil = new ActionbarUtil(this);
		actionbarUtil.setTitle(actionbarTitle);
		if (actionbarColor != -1) {
			actionbarUtil.setBackgroundColor(actionbarColor);
		} else {
			actionbarUtil.setBackgroundColor(getResources().getColor(
					R.color.white));
		}

		actionbarUtil.setup(isParent, new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.actionBarMenuIcon:

					onBackPressed();

					break;
				//
				// case R.id.actionBarAppLogo:
				//
				// finish();
				//
				// break;

				default:
					break;
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
