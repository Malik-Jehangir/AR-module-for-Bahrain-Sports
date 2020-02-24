/**
 * 
 */
package com.goys.android.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.MainActivity;
import com.goys.android.app.R;
import com.goys.android.app.util.CommonActions;

/**
 * @author saif.uddin
 * 
 * This class represents settings screen from where user can change language and 
 * turn music on/off.
 * 
 */
public class SettingsActivity extends ActionbarActivity implements
		OnClickListener {
	ImageButton ibEnglish, ibArabic;
	ImageButton onMusic, offMusic;
	CommonActions ca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_settings));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.white));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		

		((LinearLayout) findViewById(R.id.main_layout))
		.setBackgroundColor(getResources().getColor(R.color.white));


		initObj();
		initViews();
	}

	private void initObj() {
		ca = new CommonActions(this);
	}

	private void initViews() {

		ibEnglish = (ImageButton) findViewById(R.id.ibEnglish);
		ibArabic = (ImageButton) findViewById(R.id.ibArabic);

		//onMusic = (ImageButton) findViewById(R.id.onMusic);
		//offMusic = (ImageButton) findViewById(R.id.offMusic);

		ibEnglish.setOnClickListener(this);
		ibArabic.setOnClickListener(this);

		/*onMusic.setOnClickListener(this);
		offMusic.setOnClickListener(this);

		if (ca.getValueInt("playMusic", -1) == 0) {
			onMusic.setSelected(false);
			offMusic.setSelected(true);
		}

		if (ca.getValueInt("playMusic", -1) == 1) {
			onMusic.setSelected(true);
			offMusic.setSelected(false);
		}*/

		if (GOYSApplication.getInstance().isEnglishLanguage()) {
			ibEnglish.setSelected(true);
			ibArabic.setSelected(false);
		} else {
			ibEnglish.setSelected(false);
			ibArabic.setSelected(true);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ibEnglish:
			GOYSApplication.getInstance().changeLocale("en");
			ca.savePreferences("langType", 0);
			GOYSApplication.appLanguage = "en";
			intent = new Intent(SettingsActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;

		case R.id.ibArabic:
			GOYSApplication.getInstance().changeLocale("ar");
			ca.savePreferences("langType", 1);
			GOYSApplication.appLanguage = "ar";
			intent = new Intent(SettingsActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		/*case R.id.onMusic:
			ca.savePreferences("playMusic", 1);
			initViews();
			break;
		case R.id.offMusic:
			ca.savePreferences("playMusic", 0);
			initViews();
			break;*/
		default:
			break;
		}
	}

}
