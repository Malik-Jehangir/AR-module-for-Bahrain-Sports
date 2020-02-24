package com.goys.android.app.util;

import java.util.List;

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

public class MyAttachmentAdapter extends ArrayAdapter<Attachment> {

	private Activity mContext;
	private List<Attachment> mAttachmentList;
	private ListView mListView;

	public MyAttachmentAdapter(View v, Context context, int resource,
			List<Attachment> list) {
		super(context, resource, list);

		mContext = (Activity) context;
		mAttachmentList = list;
		mListView = (ListView) v;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.attachment_list_item, parent,
				false);

		ImageButton deleteAttachment = (ImageButton) convertView
				.findViewById(R.id.deleteAttachment);

		TextView titleView = (TextView) convertView
				.findViewById(R.id.attachedFileName);

		String filePath = mAttachmentList.get(position).getFilePath();
		final String fileName = filePath.substring(
				filePath.lastIndexOf("/") + 1, filePath.length());
		titleView.setText(fileName);
		deleteAttachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mAttachmentList.remove(position);

				notifyDataSetChanged();

			}
		});
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

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
