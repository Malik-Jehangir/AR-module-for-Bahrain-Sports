package com.goys.android.app.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.db.model.Country;

public class KeyValueSpinner implements SpinnerAdapter {

	Context context;
	ArrayList<Country> alCountry;
	LayoutInflater inflater;

	public KeyValueSpinner(Context context, ArrayList<Country> alCountry) {
		this.context = context;
		this.alCountry = alCountry;

		/*********** Layout inflator to call external xml layout () **********************/
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alCountry.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return alCountry.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Note:-Create this two method getIDFromIndex and getIndexByID
	public int getIDFromIndex(int Index) {
		return alCountry.get(Index).getId();
	}

	public int getIndexByID(int ID) {
		for (int i = 0; i < alCountry.size(); i++) {
			if (alCountry.get(i).getId() == ID) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public int getViewTypeCount() {
		//return android.R.layout.simple_spinner_item;
        return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		View row = inflater.inflate(R.layout.spinner_rows, parent, false);

		TextView textview = (TextView) row.findViewById(R.id.countryName);
		textview.setText(alCountry.get(position).getCountry());
		/*textview.setBackgroundColor(context.getResources().getColor(
				R.color.white));*/

		if (GOYSApplication.getInstance().isEnglishLanguage()) {
			textview.setPadding(5, 10, 0, 10);
		} else {
			textview.setPadding(0, 10, 5, 10);
		}
		return row;
	}
}