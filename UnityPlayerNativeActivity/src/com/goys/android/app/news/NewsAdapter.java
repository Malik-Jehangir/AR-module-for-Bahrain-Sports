package com.goys.android.app.news;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goys.android.app.R;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.GoysLog;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends ArrayAdapter<News> {
	private static final String TAG = "NewsAdapter";
	private Activity context;
	private List<News> data;
	static LayoutInflater inflater = null;
	CommonActions ca;

	public NewsAdapter(Activity ctx, List<News> records) {
		super(ctx, R.layout.news_row, records);
		context = ctx;
		data = records;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ca = new CommonActions(ctx);
	}

	public int getCount() {
		return data.size();
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			vi = inflator.inflate(R.layout.news_row, null);
		}

		TextView title = (TextView) vi.findViewById(R.id.title);
		TextView artist = (TextView) vi.findViewById(R.id.description);
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image);
		News objNews = data.get(position);

		if (ca.getValueInt("langType", -1) == 0) {
			title.setText(objNews.getEnglishTitle());
			artist.setText(Html.fromHtml(objNews.getEnglishShortDescription()));
		} else if (ca.getValueInt("langType", -1) == 1) {
			title.setText(objNews.getArabicTitle());
			artist.setText(Html.fromHtml(objNews.getArabicShortDescription()));
		}
		if (objNews.getImageUrl() != null && !objNews.getImageUrl().equals(" ")
				&& !objNews.getImageUrl().equals("")) {
			Picasso.with(context).load(objNews.getImageUrl()).fit().centerCrop()
					.into(thumb_image);
		} else {
			// if no image found or empty url then fix the noimage for view
			thumb_image.setImageDrawable(context.getResources().getDrawable(
					R.drawable.noimage));

		}

		return vi;
	}
}