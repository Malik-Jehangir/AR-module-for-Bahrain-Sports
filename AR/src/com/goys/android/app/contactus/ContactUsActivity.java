package com.goys.android.app.contactus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.goys.android.app.webview.myslocator;

/*
 * This class represents contact us page screen
 * */
public class ContactUsActivity extends ActionbarActivity implements
	ISettingNotifier {

	Button btn5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_contactus));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_orange));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		actionbarUtil.notifier = this;
		
		Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fontawesome-webfont.ttf" );
		 btn5 = (Button)findViewById(R.id.btn_contact_mys_locator);
		 btn5.setTypeface(font);
		 btn5.setTextSize(17);
		 
		 btn5.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                Intent Intent = new Intent(view.getContext(), myslocator.class);
	                view.getContext().startActivity(Intent);}
	            });
		
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
