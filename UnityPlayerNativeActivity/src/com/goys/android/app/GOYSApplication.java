package com.goys.android.app;

import java.util.Locale;

import com.goys.android.app.util.CommonActions;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class GOYSApplication extends Application {
	public static final String APP_SETTINGS_FILE = "fa_preferences";

	public static final String APP_TAG = "fa_log";

	public static boolean isOnChild = false;

	public static String appLanguage = "en";
	public CommonActions ca;

	/*
	 * public static ArrayList<Season> seasonList; public static Season
	 * currentSeason;
	 */

	/**
	 * A singleton instance of the application class for easy access in other
	 * places
	 */
	private static GOYSApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		// initialize the singleton
		sInstance = this;
		ca = new CommonActions(getApplicationContext());
	}

	/**
	 * @return ApplicationController singleton instance
	 */
	public static synchronized GOYSApplication getInstance() {
		return sInstance;
	}

	public void changeLocale(String lang) {
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		
	}

	public boolean isEnglishLanguage() {
		boolean isEnglishSelected = false;

		if (ca.getValueInt("langType", -1) == 0) {
			isEnglishSelected = true;
		} else if (ca.getValueInt("langType", -1) == 1) {
			isEnglishSelected = false;
		}
		return isEnglishSelected;
	}

	public boolean isMusicOn() {
		boolean isMusicOn = true;

		if (ca.getValueInt("playMusic", -1) == 1) {
			isMusicOn = true;
		} else if (ca.getValueInt("playMusic", -1) == 0) {
			isMusicOn = false;
		}
		return isMusicOn;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d("APP Language", GOYSApplication.appLanguage);
		
		  /*
		   * if language is already selected then check the shared preference to
		   * update the last saved lang
		   */
		  if (!isEnglishLanguage()) {
		   appLanguage = "ar";
		  }
		
		changeLocale(GOYSApplication.appLanguage);
	}
}
