package com.goys.android.app.news;

import java.util.List;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
public class NewsDetailActivity extends ActionbarActivity implements
		ISettingNotifier {

	ImageView newsImage;
	TextView title;
	TextView content;
	TextView news_date;

	String title_value;
	String content_value;
	String news_date_value;
	String news_image_value;
	String news_itemid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		setActionbarTitle(getResources().getString(R.string.menu_news_details));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_green));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_news_details);

		((ScrollView) findViewById(R.id.newDetailLyout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		((LinearLayout) findViewById(R.id.main_layout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		((LinearLayout) findViewById(R.id.content_layout))
				.setBackgroundColor(getResources().getColor(R.color.white));

		actionbarUtil.notifier = this;

		newsImage = (ImageView) findViewById(R.id.news_detail_image);
		title = (TextView) findViewById(R.id.news_detail_title);
		content = (TextView) findViewById(R.id.news_detail_content);
		news_date = (TextView) findViewById(R.id.news_detail_date);

		Intent intentIncoming = getIntent();
		if (intentIncoming.hasExtra("newsTitle")) {
			title_value = intentIncoming.getStringExtra("newsTitle");
			content_value = intentIncoming.getStringExtra("newsDescription");
			news_date_value = intentIncoming.getStringExtra("newsDate");
			news_image_value = intentIncoming.getStringExtra("newsImageUrl");
			news_itemid = intentIncoming.getStringExtra("itemID");

			title.setText(intentIncoming.getStringExtra("newsTitle"));
			content.setText(Html.fromHtml(intentIncoming
					.getStringExtra("newsDescription")));
			news_date.setText(intentIncoming.getStringExtra("newsDate"));

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

		String urlToShare = "http://www.mys.gov.bh/en/media/pages/NewsDetails.aspx?ItemId="
				+ news_itemid;

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {

			urlToShare = "http://www.mys.gov.bh/ar/media/pages/NewsDetails.aspx?ItemId="
					+ news_itemid;
		}

		shareIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);

		startActivity(Intent.createChooser(shareIntent, getResources()
				.getString(R.string.sharing_alert_title)));
	}
}
