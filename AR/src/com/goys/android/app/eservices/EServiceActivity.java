package com.goys.android.app.eservices;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.MainActivity2;
import com.goys.android.app.R;
import com.goys.android.app.application_linking.AppLinkingActivity;
import com.goys.android.app.emigration.EmigrationAndVisaActivity;
import com.goys.android.app.maintenance.MaintenanceFormActivity;
import com.goys.android.app.sportsparticipation.SportsParticipationsForNationalClubsActivity;

/*
 * 
 * This class represents E-Service List page screen
 * 
 */

public class EServiceActivity extends ActionbarActivity implements
		OnClickListener, ISettingNotifier {

	ImageView btn_emigration_visa;
	ImageView btn_participation_national;
	ImageView btnMaintenanceForm;
	ImageView btnLetsRegister;
	ImageView btnFinance;
	ImageView btnYourVoice;
	//ImageView btnTest;
	

	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_eservice));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eservice);

		actionbarUtil.notifier = this;

		btn_emigration_visa = (ImageView) findViewById(R.id.btn_emigraiton_visa);
		btn_participation_national = (ImageView) findViewById(R.id.btn_particpation_national_clubs);
		btnMaintenanceForm = (ImageView) findViewById(R.id.btn_maintenance);
		btnLetsRegister = (ImageView) findViewById(R.id.btn_letsregister);
		btnFinance = (ImageView) findViewById(R.id.btn_finance);
		btnYourVoice = (ImageView) findViewById(R.id.btn_yourvoice);
		//btnTest = (ImageView) findViewById(R.id.btn_testbutton);
				
		btn_emigration_visa.setOnClickListener(this);
		btn_participation_national.setOnClickListener(this);
		btnMaintenanceForm.setOnClickListener(this);
		btnLetsRegister.setOnClickListener(this);
		btnFinance.setOnClickListener(this);
		btnYourVoice.setOnClickListener(this);
		//btnTest.setOnClickListener(this);
	};

	@Override
	public void onClick(View v) {
		Intent i = null;
		switch (v.getId()) {

		case R.id.btn_emigraiton_visa:
			i = new Intent(EServiceActivity.this,
					EmigrationAndVisaActivity.class);
			startActivity(i);
			break;

		case R.id.btn_particpation_national_clubs:
			i = new Intent(EServiceActivity.this,
					SportsParticipationsForNationalClubsActivity.class);
			startActivity(i);
			break;
			
		case R.id.btn_maintenance:
			i = new Intent(EServiceActivity.this,MaintenanceFormActivity.class);
			startActivity(i);
			break;
			
		case R.id.btn_letsregister:
			
			Resources res = getResources();
			String url = res.getString(R.string.link_letsregister);
			
			i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);
			break;
			
		case R.id.btn_finance:
			
			Resources res1 = getResources();
			String url1 = res1.getString(R.string.link_finance);
			
			i = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
			startActivity(i);
			break;
			
		case R.id.btn_yourvoice:
			
			Resources res2 = getResources();
			String url2 = res2.getString(R.string.link_yourvoice);
			
			i = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
			startActivity(i);
			break;
			
			
		/*case R.id.btn_testbutton:
			
			i = new Intent(EServiceActivity.this,MainActivity2.class);
			startActivity(i);
			break;
			*/
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void OnClick() {
		
	}
}
