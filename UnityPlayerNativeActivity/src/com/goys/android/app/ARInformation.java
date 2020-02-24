package com.goys.android.app;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.goys.android.app.webview.myslocator;


public class ARInformation extends ActionbarActivity implements
ISettingNotifier{

	Button btnAR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_arinformaiton));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_orange));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ar_information);
		actionbarUtil.notifier = this;
		
		Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fontawesome-webfont.ttf" );
		btnAR = (Button)findViewById(R.id.btn_ar_information);
		 btnAR.setTypeface(font);
		 btnAR.setTextSize(17);
		 
		 btnAR.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                Intent Intent = new Intent(view.getContext(), UnityPlayerActivity.class);
	                view.getContext().startActivity(Intent);}
	            });
		
	}
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	@Override
	public void OnClick() {		
	}
}
