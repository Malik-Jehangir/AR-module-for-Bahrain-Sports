package com.android.afilechooser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.goys.android.app.R;
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.sportsparticipation.SportsParticipationsForNationalClubsActivity;

public class AttachementListArrayAdapter extends ArrayAdapter<Attachment> {

	private final Activity actv;

	private ListView attachedFileslistview;

	private final ArrayList<Attachment> modelsArrayList;

	public AttachementListArrayAdapter(View view, Activity actvity,
			ArrayList<Attachment> modelsArrayList) {

		super(actvity, R.layout.attachment_list_item, modelsArrayList);
		this.actv = actvity;
		this.attachedFileslistview = (ListView) view;
		this.modelsArrayList = modelsArrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) actv
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;

		rowView = inflater
				.inflate(R.layout.attachment_list_item, parent, false);

		ImageButton deleteAttachment = (ImageButton) rowView
				.findViewById(R.id.deleteAttachment);

		TextView titleView = (TextView) rowView
				.findViewById(R.id.attachedFileName);

		String filePath = modelsArrayList.get(position).getFilePath();
		final String fileName = filePath.substring(
				filePath.lastIndexOf("/") + 1, filePath.length());
		titleView.setText(fileName);
		deleteAttachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				modelsArrayList.remove(position);

				notifyDataSetChanged();

				if (actv instanceof SportsParticipationsForNationalClubsActivity) {
					((SportsParticipationsForNationalClubsActivity) actv)
							.notifyModels(attachedFileslistview);
				}

			}
		});
		return rowView;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(attachedFileslistview);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
