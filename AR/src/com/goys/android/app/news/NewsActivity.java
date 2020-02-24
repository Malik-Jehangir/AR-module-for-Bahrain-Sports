package com.goys.android.app.news;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
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
public class NewsActivity extends ActionbarActivity implements
		ResponseListener, ISettingNotifier {

	protected static final String TAG = "NewsActivity";
	CommonActions ca;
	List<News> items;
	NewsAdapter adapter;
	PullAndLoadListView lv_news;
	int startLimit = 1, endLimit = 12, diff = 10;
	boolean isLoadMore = false;
	ProgressBar pb;
	TextView tvBanner;
	GoysService gs;
	Boolean isActivityAvailable;

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
		
		
		Log.d("New Activity", "oncreate: " + GOYSApplication.getInstance().isEnglishLanguage());
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}
		
		setActionbarTitle(getResources().getString(R.string.menu_news));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_green));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_news);

		actionbarUtil.notifier = this;

		gs = new GoysService(this);
		gs.setResponseListener(this);

		lv_news = (PullAndLoadListView) findViewById(R.id.newsList);
		pb = (ProgressBar) findViewById(R.id.pbEvent);
		tvBanner = (TextView) findViewById(R.id.tvNewsBanner);

		lv_news.setVisibility(View.GONE);
		tvBanner.setVisibility(View.GONE);

		if (CommonActions.hasConnection(this)) {
			gs.callNewsServiceWithLanguage(NewsActivity.this, startLimit,
					endLimit, GOYSApplication.appLanguage);

			items = new ArrayList<News>();
			adapter = new NewsAdapter(this, items);
			lv_news.setAdapter(adapter);

		} else {
			pb.setVisibility(View.GONE);
			tvBanner.setVisibility(View.VISIBLE);
			tvBanner.setText(getResources().getString(
					R.string.networkConnectionRequired));
		}

		// Set a listener to be invoked when the list should be refreshed.
		lv_news.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// Do work to refresh the list here.

				startLimit = 1;
				endLimit = 12;
				isLoadMore = false;

				gs.callNewsServiceWithLanguage(NewsActivity.this, startLimit,
						endLimit, GOYSApplication.appLanguage);

			}
		});

		// set a listener to be invoked when the list reaches the end
		lv_news.setOnLoadMoreListener(new OnLoadMoreListener() {

			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here

				startLimit = endLimit + 1;
				endLimit = startLimit + diff;

				isLoadMore = true;
				gs.callNewsServiceWithLanguage(NewsActivity.this, startLimit,
						endLimit, GOYSApplication.appLanguage);

			}
		});

		ca = new CommonActions(this);

		lv_news.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Log.d(TAG, "items.size() " + items.size());
					News obj = items.get(position - 1);
					Intent i = new Intent(NewsActivity.this,
							NewsDetailActivity.class);

					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						i.putExtra("newsTitle", obj.getEnglishTitle());
						i.putExtra("newsDescription", obj.getEnglishBody());
					} else {
						i.putExtra("newsTitle", obj.getArabicTitle());
						i.putExtra("newsDescription", obj.getArabicBody());
					}

					i.putExtra("newsImageUrl", obj.getImageUrl());

					GoysLog.d(TAG, Integer.toString(obj.getId()));
					i.putExtra("itemID", Integer.toString(obj.getId()));

					try {

						DateFormat sdf = new SimpleDateFormat(
								"MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH);

						DateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy");
						i.putExtra("newsDate",
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

			GoysLog.e("goys_news_onresposne", NewsActivity.this + "");

			if (serviceId == AppConstants.NEWS_SERVICE_ID) {
				pb.setVisibility(View.GONE);

				if (isLoadMore) {
					lv_news.onLoadMoreComplete();

				} else {
					items = new ArrayList<News>();
					lv_news.onRefreshComplete();
				}

				lv_news.setVisibility(View.VISIBLE);
				if (responseObj != null && !responseObj.contains("Error")) {

					try {
						JSONArray jArray = new JSONArray(responseObj);
						lv_news.mProgressBarLoadMore
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

									News obj = new News(jObject);
									items.add(obj);
								}
							} // End Loop
						} else {
							isLoadMore = true;
							lv_news.mProgressBarLoadMore
									.setVisibility(View.GONE);
							lv_news.onLoadMoreComplete();
						}
					} catch (JSONException e) {
						GoysLog.e("JSONException", "Error: " + e.toString());
						CommonActions.alertDialogShow(
								NewsActivity.this,
								getResources().getString(R.string.note),
								getResources().getString(
										R.string.applicationundermaintenance));
					}

					adapter.notifyDataSetChanged();

					if (items.size() > 0) {

						if (isLoadMore) {

							adapter.notifyDataSetChanged();

						} else {
							adapter = new NewsAdapter(NewsActivity.this, items);
							lv_news.setAdapter(adapter);
						}

						tvBanner.setVisibility(View.GONE);

					} else {

						tvBanner.setVisibility(View.VISIBLE);
					}

				} else {
					CommonActions.alertDialogShow(
							NewsActivity.this,
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

			CommonActions.alertDialogShow(NewsActivity.this, getResources()

			.getString(R.string.alert_error_title), msg);

		}
	}

	@Override
	public void OnClick() {

	}
}
