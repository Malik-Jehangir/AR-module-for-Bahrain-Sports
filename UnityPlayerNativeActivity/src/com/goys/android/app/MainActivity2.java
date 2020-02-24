package com.goys.android.app;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toolbar;

public class MainActivity2  extends ActionbarActivity implements
OnClickListener, ISettingNotifier{

	public static DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private ActionBarDrawerToggle mDrawerToggle;
	
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		actionbarUtil.notifier = this;
		//Setup Actionbar /Toolbar
		
		
		
	
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	public void onClick(View v) {
	
	}
	
	@Override
	public void OnClick() {
		
	}
	

}






    
