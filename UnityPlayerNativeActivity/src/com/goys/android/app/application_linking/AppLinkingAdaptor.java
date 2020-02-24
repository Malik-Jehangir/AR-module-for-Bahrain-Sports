package com.goys.android.app.application_linking;

import java.util.ArrayList;

import com.goys.android.app.R;
import com.goys.android.app.db.model.App;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppLinkingAdaptor extends ArrayAdapter<App>  {

	private ArrayList<App> appList;

	private Context context;

	private ViewHolder holder;
	
	
	public AppLinkingAdaptor(Context context, int textViewResourceId,
			ArrayList<App> appList) {
		super(context, textViewResourceId, appList);

		this.appList = appList;
		this.context = context;

	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = ((Activity) context).getLayoutInflater().inflate(
					R.layout.item_app, null);
			holder = new ViewHolder();

			holder.tvAppTitle = (TextView) convertView
					.findViewById(R.id.tvAppTitle);

			holder.ivAppIcon = (ImageView) convertView
					.findViewById(R.id.ivAppIcon);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvAppTitle.setText(appList.get(position).getAppName());
		holder.ivAppIcon.setBackgroundResource(appList.get(position)
				.getAppIcon());

		return convertView;
	}

	private class ViewHolder {
		TextView tvAppTitle;
		ImageView ivAppIcon;
	}

	
	
	
}
