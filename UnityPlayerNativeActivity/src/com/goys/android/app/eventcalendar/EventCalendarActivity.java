package com.goys.android.app.eventcalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.ISettingNotifier;
import com.goys.android.app.R;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ResponseListener;
import com.goys.android.app.widgets.PullAndLoadListView;
import com.goys.android.app.widgets.PullAndLoadListView.OnLoadMoreListener;
import com.goys.android.app.widgets.PullToRefreshListView.OnRefreshListener;

/*
 * This class represents News List page screen
 * 
 */
public class EventCalendarActivity extends ActionbarActivity implements
		ResponseListener, ISettingNotifier {

	protected static final String TAG = "EventCalendarActivity";
	CommonActions ca;
	List<EventCalendar> items;
	EventCalendarAdapter adapter;
	PullAndLoadListView lv_eventcalendar;
	int startLimit = 1, endLimit = 12, diff = 10;
	boolean isLoadMore = false;
	ProgressBar pb;
	TextView tvBanner;
	GoysService gs;
	Boolean isActivityAvailable;
	String language=Locale.getDefault().getLanguage();

	@Override
	protected void onPause() {
		super.onPause();
		isActivityAvailable = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityAvailable = true;
		Log.d("News Activity", "onresume");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		Log.d("Event Activity", "oncreate: " + GOYSApplication.getInstance().isEnglishLanguage());
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_event_calendar));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_eventcalendar);
		//setContentView(R.layout.fragment_news);

		actionbarUtil.notifier = this;

		gs = new GoysService(this);
		gs.setResponseListener(this);
		

		//lv_eventcalendar = (PullAndLoadListView) findViewById(R.id.EventCalendarPull);
		lv_eventcalendar = (PullAndLoadListView) findViewById(R.id.EventCalendarPull);
		pb = (ProgressBar) findViewById(R.id.pbEventCalendar);
		tvBanner = (TextView) findViewById(R.id.tvEventCalendarBanner);

		
		lv_eventcalendar.setVisibility(View.GONE);
		tvBanner.setVisibility(View.GONE);

		if (CommonActions.hasConnection(this)) {
			
			gs.callEventCalendarService(this, GOYSApplication.appLanguage);
			items = new ArrayList<EventCalendar>();
			
			adapter = new EventCalendarAdapter(this, items);
			lv_eventcalendar.setAdapter(adapter);

		} else {
			pb.setVisibility(View.GONE);
			tvBanner.setVisibility(View.VISIBLE);
			tvBanner.setText(getResources().getString(
					R.string.networkConnectionRequired));
		}

		// Set a listener to be invoked when the list should be refreshed.
		lv_eventcalendar.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// Do work to refresh the list here.

				startLimit = 1;
				endLimit = 12;
				isLoadMore = false;

				gs.callEventCalendarService(EventCalendarActivity.this, GOYSApplication.appLanguage);

			}
		});

		// set a listener to be invoked when the list reaches the end
		lv_eventcalendar.setOnLoadMoreListener(new OnLoadMoreListener() {

			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here

				startLimit = endLimit + 1;
				endLimit = startLimit + diff;

				isLoadMore = true;
				gs.callEventCalendarService(EventCalendarActivity.this, GOYSApplication.appLanguage);

			}
		});

		ca = new CommonActions(this);

		
		lv_eventcalendar.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Log.d(TAG, "items.size() " + items.size());
					EventCalendar obj = items.get(position - 1);
					Intent i = new Intent(EventCalendarActivity.this,
							EventCalendarDetailActivity.class);

					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						i.putExtra("eventcalendarTitle", obj.getEnglishTitle());
						i.putExtra("eventcalendarDescription", obj.getEnglishShortDescription());
					} else {
						i.putExtra("eventcalendarTitle", obj.getArabicTitle());
						i.putExtra("eventcalendarDescription", obj.getArabicShortDescription());
					}

					i.putExtra("eventcalendarImageUrl", obj.getImageUrl());

					GoysLog.d(TAG, Integer.toString(obj.getId()));
					i.putExtra("itemID", Integer.toString(obj.getId()));

					try {

						DateFormat sdf = new SimpleDateFormat(
								"MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH);

						DateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy");
						i.putExtra("eventcalendarDate",
								sdf2.format(sdf.parse(obj.getStartDate())));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					startActivity(i);

				} catch (IndexOutOfBoundsException e) {

					GoysLog.e("IndexOutOfBoundsException", e.getMessage());
				}

			}
		});
		

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onResponse(int serviceId, String responseObj) {

		if (isActivityAvailable) {

			GoysLog.e("goys_eventcalendar_onresposne", EventCalendarActivity.this + "");

			if (serviceId == AppConstants.EVENTCALENDAR_SERVICE_ID) {
				pb.setVisibility(View.GONE);

				if (isLoadMore) {
					lv_eventcalendar.onLoadMoreComplete();

				} else {
					items = new ArrayList<EventCalendar>();
					lv_eventcalendar.onRefreshComplete();
				}

				lv_eventcalendar.setVisibility(View.VISIBLE);
				if (responseObj != null && !responseObj.contains("Error")) {

					try {
						JSONArray jArray = new JSONArray(responseObj);
						lv_eventcalendar.mProgressBarLoadMore
								.setVisibility(View.VISIBLE);
						if (jArray.length() > 0) {
							for (int i = 0; i < jArray.length(); i++) {

								if (!jArray.isNull(i)) { // check null for worst
															// case.
									GoysLog.d(
											TAG,
											"index " + i + " "
													+ jArray.getJSONObject(i));
									JSONObject jObject = jArray
											.getJSONObject(i);

									EventCalendar obj = new EventCalendar(jObject);
									items.add(obj);
									
									//Check The date
									
									//Iterate to Items
									//Log.d("Event Detail", "Start Iterate Items");
									SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
									Date convertedDate = new Date();
									try {
										convertedDate = dateFormat.parse(obj.getStartDate());
										Date currentDT = new Date();
										
										if(new Date().compareTo(convertedDate) < 0)
										{
											//check if == 10 days
											int dtdiff = ((int)((currentDT.getTime()/(24*60*60*1000))
													-(int)(convertedDate.getTime()/(24*60*60*1000))));
											Log.d("Event Detail",String.valueOf(dtdiff) + " " + obj.getEnglishTitle());
											String s = getResources().getString(R.string.event_notification_add);
											
											// dtdiff < 0
											if(dtdiff == -10){
												if(language.equals("en")){
													addNotification(s + obj.getEnglishTitle() + ") - " + obj.getStartDate() ,i);
												}else{
													addNotification(s + obj.getArabicTitle() + ") - " + obj.getStartDate(),i);
												}	
											}	
										}
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							} // End Loop
						} else {
							isLoadMore = true;
							lv_eventcalendar.mProgressBarLoadMore
									.setVisibility(View.GONE);
							lv_eventcalendar.onLoadMoreComplete();
						}
					} catch (JSONException e) {
						GoysLog.e("JSONException", "Error: " + e.toString());
						CommonActions.alertDialogShow(
								EventCalendarActivity.this,
								getResources().getString(R.string.note),
								getResources().getString(
										R.string.applicationundermaintenance));
					}

					adapter.notifyDataSetChanged();

					if (items.size() > 0) {

						if (isLoadMore) {

							adapter.notifyDataSetChanged();

						} else {
							adapter = new EventCalendarAdapter(EventCalendarActivity.this, items);
							lv_eventcalendar.setAdapter(adapter);
						}

						tvBanner.setVisibility(View.GONE);

					} else {

						tvBanner.setVisibility(View.VISIBLE);
					}

				} else {
					CommonActions.alertDialogShow(
							EventCalendarActivity.this,
							getResources()
									.getString(R.string.alert_error_title),
							getResources().getString(
									R.string.applicationundermaintenance));

				}
			}
		}

	}

	@Override
	public void onError(int responseCode, String msg) {

		if (isActivityAvailable) {

			pb.setVisibility(View.GONE);
			tvBanner.setVisibility(View.VISIBLE);
			tvBanner.setText(getResources().getString(
					R.string.applicationundermaintenance));

			CommonActions.alertDialogShow(EventCalendarActivity.this, getResources()

			.getString(R.string.alert_error_title), msg);

		}
	}

	@Override
	public void OnClick() {

	}
	
	 private void addNotification(String nTitle, int nid) {
	    	int notificationId = 101 + nid;
	    	Log.d("Event Detail", "Start Notification");
	        Intent notificationIntent = new Intent(this, EventCalendarActivity.class);
	        PendingIntent viewPendingIntent =
	                PendingIntent.getActivity(this, 0, notificationIntent, 0);
	        
	        
	        //Building notification layout
	        NotificationCompat.Builder notificationBuilder =
	                new NotificationCompat.Builder(this)
	                        .setSmallIcon(R.drawable.ic_launcher)
	                        .setContentTitle(nTitle)
	                        .setContentText(nTitle)
	                        .setContentIntent(viewPendingIntent);

	     // instance of the NotificationManager service
	        NotificationManagerCompat notificationManager =
	                NotificationManagerCompat.from(this);
	     
	        // Build the notification and notify it using notification manager.
	        notificationManager.notify(notificationId, notificationBuilder.build());
	     }
	
}
