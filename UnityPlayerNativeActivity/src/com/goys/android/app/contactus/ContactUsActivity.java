package com.goys.android.app.contactus;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
	TextView twhome, twyic, twscc, twmmyc;
	String language=Locale.getDefault().getLanguage();
	
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
		TextView txtv1 = (TextView)findViewById(R.id.iconlocation);
		TextView txtv2 = (TextView)findViewById(R.id.iconlocation1);
		TextView txtv3= (TextView)findViewById(R.id.iconlocation2);
		TextView txtv4 = (TextView)findViewById(R.id.iconlocation3);
		txtv1.setTypeface(font);
		txtv1.setTextSize(17);
		txtv2.setTypeface(font);
		txtv2.setTextSize(17);
		txtv3.setTypeface(font);
		txtv3.setTextSize(17);
		txtv4.setTypeface(font);
		txtv4.setTextSize(17);
		
		/*
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
		 */
		 
		 //Addresses
		 twhome = (TextView) findViewById(R.id.hqbutton);
		 twhome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "https://www.google.com.bh/maps/place/General+Organization+for+Youth+%26+Sports/@26.2407303,50.5317664,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49a540037f248b:0xc4ad59f43b228f9d!8m2!3d26.2407303!4d50.5339551?hl=en";
				
				if(language.equals("ar")){
					url = "https://www.google.com.bh/maps/place/General+Organization+for+Youth+%26+Sports/@26.2407303,50.5317664,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49a540037f248b:0xc4ad59f43b228f9d!8m2!3d26.2407303!4d50.5339551?hl=ar";
				}
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(url));
				 startActivity(i);
			}
		}); 
		 
		 //Addresses
		 twyic = (TextView) findViewById(R.id.yicbutton);
		 twyic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "https://www.google.com.bh/maps/place/Youth+Innovation+Center/@26.2033721,50.5867276,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49af2921da2469:0x82d7fe2eef1cdfc!8m2!3d26.2033721!4d50.5889163?hl=en";
				if(language.equals("ar")){
					url = "https://www.google.com.bh/maps/place/Youth+Innovation+Center/@26.2033721,50.5867276,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49af2921da2469:0x82d7fe2eef1cdfc!8m2!3d26.2033721!4d50.5889163?hl=ar";
				}
				Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(url));
				 startActivity(i);
			}
		}); 
		 
		//Addresses
		 twscc = (TextView) findViewById(R.id.sccbutton);
		 twscc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "https://www.google.co.in/maps/place/Salman+cultural+center/@26.222344,50.5894077,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49af4a2f461b7f:0x7dce3b2e8743b550!8m2!3d26.222344!4d50.5915964";
				if(language.equals("ar")){
					url = "https://www.google.co.in/maps/place/Salman+cultural+center/@26.222344,50.5894077,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49af4a2f461b7f:0x7dce3b2e8743b550!8m2!3d26.222344!4d50.5915964";
				}
				Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(url));
				 startActivity(i);
			}
		}); 
		 
		//Addresses
		 twmmyc = (TextView) findViewById(R.id.mmycbutton);
		 twmmyc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String url = "https://www.google.com.bh/maps/place/Muharraq+Model+Youth+Centre/@26.2712605,50.6014146,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49a67102c0c21b:0x77b85ef1695a7e80!8m2!3d26.2712605!4d50.6036033?hl=en";
				if(language.equals("ar")){
					url = "https://www.google.com.bh/maps/place/Muharraq+Model+Youth+Centre/@26.2712605,50.6014146,17z/data=!3m1!4b1!4m5!3m4!1s0x3e49a67102c0c21b:0x77b85ef1695a7e80!8m2!3d26.2712605!4d50.6036033?hl=ar";
				}
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(url));
				 startActivity(i);
			}
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
