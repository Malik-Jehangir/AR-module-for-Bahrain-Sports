package com.goys.android.app.tvstream;

import java.net.URLEncoder;
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

public class TvStreamActivity extends ActionbarActivity implements 
OnItemClickListener, ISettingNotifier { 

	ArrayList<App> appList;
	
	ListView lvApp;
	
	TvStreamAdaptor adap;
	
	private static final String TAG = "TvStreamActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		if(!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_tv_streaming_form));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_green));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_livestream_list);
		actionbarUtil.notifier = this;

		initViews();
		initObj();
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		if(!GOYSApplication.getInstance().isEnglishLanguage()){
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(GOYSApplication.appLanguage);
		}
	}

	private void initViews(){
		lvApp = (ListView) findViewById(R.id.lvTvStream);
		lvApp.setOnItemClickListener(this);
	}
	
	private void initObj(){
		appList = new ArrayList<App>();
		
		App app = new App();
		
		/*
		app = new App();
		app.setAppIcon(R.drawable.ic_bna_livestream);
		app.setAppId(1);
		app.setAppName(getResources().getString(R.string.app_bna_live_title));
		app.setAppPackage(getResources().getString(R.string.app_bna_live_packagename));
		appList.add(app);
		*/
		
		app = new App();
		app.setAppIcon(R.drawable.ic_sports_livestream);
		app.setAppId(1);
		app.setAppName(getResources().getString(R.string.app_btv_sports_live_title));
		app.setAppPackage("rtsp://185.105.4.106:1935/live/Bahrain%20Sports");
		appList.add(app);
		
		adap = new TvStreamAdaptor(this,0,appList);
		lvApp.setAdapter(adap);
		
	}
	
	
	@Override
	public void OnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		/*if(position == 0){
			Log.d("BNA Live","BNA Live");
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getResources().getString(R.string.app_bahrain_cinema_booking_packagename)));
			startActivity(i);
		}*/
		
		Intent intent = getPackageManager().getLaunchIntentForPackage(
				appList.get(position).getAppPackage());
		
		if(intent == null){
			//Uri uri = Uri.parse(appList.get(position).getAppPackage());
			String uri = appList.get(position).getAppPackage();
			
			Intent i = new Intent(Intent.ACTION_VIEW);
			//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			i.setData(Uri.parse(uri));
			
			try{
				startActivity(i);
			}catch (ActivityNotFoundException e2){
				GoysLog.e(TAG, "No Store");
			}
			
		}else{
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		finish();
	}
	
	


}
