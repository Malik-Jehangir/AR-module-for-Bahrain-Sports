package com.goys.android.app.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.crashlytics.android.Crashlytics;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.MainActivity;
import com.goys.android.app.R;
import com.goys.android.app.demo.DemoActivity;
import com.goys.android.app.util.CommonActions;

/*
 * This class represents Splash page screen which appears on start of screen
 * 
 */

public class SplashActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar sb;
	CommonActions ca;
	LinearLayout llLanguageButtons;
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 500;
	int langType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_splash);

		llLanguageButtons = (LinearLayout) findViewById(R.id.langLayout);

		sb = (SeekBar) findViewById(R.id.slider);

		sb.setMax(2);

		sb.setProgress(1);

		sb.setOnSeekBarChangeListener(this);

		ca = new CommonActions(this);

		langType = ca.getValueInt("langType", -1);

		if (langType > -1) {
			new Thread() {
				public void run() {
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (langType == 0) {
						GOYSApplication.getInstance().changeLocale("en");
					} else if (langType == 1) {
						GOYSApplication.getInstance().changeLocale("ar");
					}
					Intent i = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(i);
					finish();
				};
			}.start();

			llLanguageButtons.setVisibility(View.INVISIBLE);
		} else {

		}
	}

	@Override
	public void onProgressChanged(SeekBar v, int progress, boolean isUser) {

		ca.savePreferences("playMusic", 1);

		if (progress == 2) {
			GOYSApplication.getInstance().changeLocale("ar");
			ca.savePreferences("langType", 1);
			ca.savePreferences("playMusic", 1);
			moveToHome();
		}
		if (progress == 0) {
			GOYSApplication.getInstance().changeLocale("en");
			ca.savePreferences("langType", 0);
			ca.savePreferences("playMusic", 1);
			moveToHome();
		}

	}

	private void moveToHome() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Intent i = new Intent(SplashActivity.this, MainActivity.class);
				Intent i = new Intent(SplashActivity.this, DemoActivity.class);
				
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
