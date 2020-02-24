package com.goys.android.app.application_linking;

import java.util.ArrayList;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.goys.android.app.R.id;
import com.goys.android.app.R.layout;
import com.goys.android.app.R.menu;
import com.goys.android.app.db.model.App;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.ResponseListener;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/*
 * Application linking activity where all the application list shows
 */
public class AppLinkingActivity extends ActionbarActivity implements
		OnItemClickListener, ISettingNotifier {

	ArrayList<App> appList;

	ListView lvApp;

	AppLinkingAdaptor adap;

	private static final String TAG = "AppLinkingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		setActionbarTitle(getResources().getString(
				R.string.menu_app_linking_form));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_purple));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_linking);
		actionbarUtil.notifier = this;

		initViews();
		initObj();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

	}

	private void initViews() {
		lvApp = (ListView) findViewById(R.id.lvApp);
		lvApp.setOnItemClickListener(this);
	}

	private void initObj() {

		appList = new ArrayList<App>();

		App app = new App();

		app = new App();
		app.setAppIcon(R.drawable.ic_tawasul);
		app.setAppId(1);
		app.setAppName(getResources().getString(
				R.string.app_tawasul_title));
		app.setAppPackage(getResources().getString(
				R.string.app_tawasul_packagename));
		appList.add(app);

		
		app = new App();
		app.setAppIcon(R.drawable.ic_arab_gulf_league);
		app.setAppId(2);
		app.setAppName(getResources().getString(
				R.string.app_arab_gulf_title));
		app.setAppPackage(getResources().getString(
				R.string.app_arab_gulf_league_packagename));
		appList.add(app);
		

		app = new App();
		app.setAppIcon(R.drawable.ic_islamiyat_bahrain);
		app.setAppId(3);
		app.setAppName(getResources().getString(R.string.app_islamiyat_title));
		app.setAppPackage(getResources().getString(
				R.string.app_islamiyat_packagename));
		appList.add(app);

		app = new App();
		app.setAppIcon(R.drawable.ic_traffic_services);
		app.setAppId(4);
		app.setAppName(getResources().getString(
				R.string.app_traffic_services_title));
		app.setAppPackage(getResources().getString(
				R.string.app_traffic_services_packagename));
		appList.add(app);

		app = new App();
		app.setAppIcon(R.drawable.ic_student_exam_results);
		app.setAppId(5);
		app.setAppName(getResources().getString(
				R.string.app_student_exam_result_title));
		app.setAppPackage(getResources().getString(
				R.string.app_student_exam_result_packagename));
		appList.add(app);

		app = new App();
		app.setAppIcon(R.drawable.ic_bahrain_cinema_schedule);
		app.setAppId(6);
		app.setAppName(getResources().getString(
				R.string.app_bahrain_cinema_schedule_title));
		app.setAppPackage(getResources().getString(
				R.string.app_bahrain_cinema_schedule_packagename));
		appList.add(app);
		
		app = new App();
		app.setAppIcon(R.drawable.ic_cinema_booking);
		app.setAppId(7);
		app.setAppName(getResources().getString(
				R.string.app_bahrain_cinema_booking_title));
		app.setAppPackage(getResources().getString(
				R.string.app_bahrain_cinema_booking_packagename));
		appList.add(app);

		app = new App();
		app.setAppIcon(R.drawable.ic_endomondo_sports_tracker);
		app.setAppId(8);
		app.setAppName(getResources().getString(
				R.string.app_endomondo_sports_tracker_title));
		app.setAppPackage(getResources().getString(
				R.string.app_endomondo_sports_tracker_packagename));
		appList.add(app);

		adap = new AppLinkingAdaptor(this, 0, appList);
		lvApp.setAdapter(adap);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == 6) {
			Log.d("bahrain Cinema booking", "bahrain Cinema booking");
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getResources().getString(
							R.string.app_bahrain_cinema_booking_packagename)));
			startActivity(browserIntent);

		} else {

			Intent intent = getPackageManager().getLaunchIntentForPackage(
					appList.get(position).getAppPackage());

			if (intent == null) {
				Uri uri = Uri.parse("market://search?q=pname:"
						+ appList.get(position).getAppPackage());
				// Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);

				Intent intent2 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id="
								+ appList.get(position).getAppPackage()));
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				try {
					startActivity(intent2);
				} catch (ActivityNotFoundException e2) {
					GoysLog.e(TAG, "No Store");
				}
			} else {
				startActivity(intent);
			}

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
