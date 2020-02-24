package com.goys.android.app.eventcalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.squareup.picasso.Picasso;

/*
 * 
 * This class represents News Detail page screen
 * 
 */
public class EventCalendarDetailActivity extends ActionbarActivity implements
OnClickListener, ISettingNotifier {

	ImageView newsImage;
	TextView title;
	TextView content;
	TextView news_date;
	Button addtoCalendar;

	String title_value;
	String content_value;
	String news_date_value;
	String news_image_value;
	String news_itemid;
	
	String eventday,eventmonth,eventyear;
	int inteventday,inteventmonth,inteventyear;
	GregorianCalendar calDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		setActionbarTitle(getResources().getString(R.string.menu_event_calendar_details));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_event_calendar_details);

		((ScrollView) findViewById(R.id.eventDetailLyout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		((LinearLayout) findViewById(R.id.main_layout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		((LinearLayout) findViewById(R.id.content_layout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		actionbarUtil.notifier = this;

		newsImage = (ImageView) findViewById(R.id.event_detail_image);
		title = (TextView) findViewById(R.id.event_detail_title);
		content = (TextView) findViewById(R.id.event_detail_content);
		news_date = (TextView) findViewById(R.id.event_detail_date);

		Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fontawesome-webfont.ttf" );
		addtoCalendar = (Button) findViewById(R.id.btn_event_detail_addcalendar);
		addtoCalendar.setTypeface(font);
		addtoCalendar.setTextSize(17);
		addtoCalendar.setOnClickListener(this);
		
		Intent intentIncoming = getIntent();
		if (intentIncoming.hasExtra("eventcalendarTitle")) {
			title_value = intentIncoming.getStringExtra("eventcalendarTitle");
			content_value = intentIncoming.getStringExtra("eventcalendarDescription");
			news_date_value = intentIncoming.getStringExtra("eventcalendarDate");
			news_image_value = intentIncoming.getStringExtra("eventcalendarImageUrl");
			news_itemid = intentIncoming.getStringExtra("itemID");

			title.setText(intentIncoming.getStringExtra("eventcalendarTitle"));
			content.setText(Html.fromHtml(intentIncoming
					.getStringExtra("eventcalendarDescription")));
			news_date.setText(intentIncoming.getStringExtra("eventcalendarDate"));

			if (news_image_value != null && !news_image_value.equals(" ")
					&& !news_image_value.equals("")) {
				Picasso.with(getApplicationContext()).load(news_image_value)
						.fit().centerInside().error(R.drawable.noimage)
						.into(newsImage);
			} else {
				// if no image found or empty url then fix the noimage for view
				newsImage.setImageDrawable(getResources().getDrawable(
						R.drawable.noimage));
			}
		}

	}

	@Override
	public void OnClick() {
		// TODO Auto-generated method stub
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title_value);

		String urlToShare = "http://www.mys.gov.bh/en/media/Pages/EventDetails.aspx?ItemId="
				+ news_itemid;

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {

			urlToShare = "http://www.mys.gov.bh/ar/media/Pages/EventDetails.aspx?ItemId="
					+ news_itemid;
		}

		shareIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);

		startActivity(Intent.createChooser(shareIntent, getResources()
				.getString(R.string.sharing_alert_title)));
	}
	
	
	@Override
	public void onClick(View v) {
		Log.d("Event Detail", "Button is Click");
		switch (v.getId()) {

		case R.id.btn_event_detail_addcalendar:
			
			Log.d("Event Detail", "Button is Click");
			
			Intent calIntent = new Intent(Intent.ACTION_INSERT); 
			calIntent.setType("vnd.android.cursor.item/event");    
			calIntent.putExtra(Events.TITLE, title_value); 
			calIntent.putExtra(Events.EVENT_LOCATION, "Bahrain"); 
			calIntent.putExtra(Events.DESCRIPTION, title_value); 
			
			//Convert string to date
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
			Date convertedDate = new Date();
			
			calDate = new GregorianCalendar(2017, 7, 15);
			try {
		        convertedDate = dateFormat.parse(news_date_value);
		        eventday = (String)DateFormat.format("dd", convertedDate);
		        eventmonth = (String)DateFormat.format("MM", convertedDate);
		        eventyear = (String)DateFormat.format("yyyy", convertedDate);
		        //Convet to int
		        inteventday = Integer.parseInt(eventday);
		        inteventmonth = Integer.parseInt(eventmonth);
		        inteventyear = Integer.parseInt(eventyear);
		        calDate = new GregorianCalendar(inteventyear, inteventmonth - 1, inteventday);
		        Log.d("Event Detail", eventmonth);
		        Log.d("Event Detail", eventday);
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		    	Log.d("Event Detail", "Date Error");
		    }
			
			
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true); 
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 
			     calDate.getTimeInMillis()); 
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 
			     calDate.getTimeInMillis()); 
			 
			startActivity(calIntent);
			break;		
			
		default:
			break;
		}
		

	}
	
	
	
	
}
