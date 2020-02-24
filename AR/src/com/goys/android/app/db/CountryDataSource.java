package com.goys.android.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goys.android.app.db.model.Country;
import com.goys.android.app.util.AppConstants;

public class CountryDataSource {
	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private String[] countryTableColumns = { AppConstants.COLUMN_ID,
			AppConstants.COLUMN_COUNTRY };

	public CountryDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Country createCountry(String comment) {
		ContentValues values = new ContentValues();
		values.put(AppConstants.COLUMN_COUNTRY, comment);
		long insertId = database.insert(AppConstants.TABLE_COUNTRIES, null,
				values);
		Cursor cursor = database.query(AppConstants.TABLE_COUNTRIES,
				countryTableColumns, AppConstants.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Country newCountry = cursorToCountry(cursor);
		cursor.close();
		return newCountry;
	}

	public void deleteCountry(Country country) {
		int id = country.getId();
		System.out.println("Country deleted with id: " + id);
		database.delete(AppConstants.TABLE_COUNTRIES, AppConstants.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Country> getAllCountry() {
		List<Country> countries = new ArrayList<Country>();

		Cursor cursor = database.query(AppConstants.TABLE_COUNTRIES,
				countryTableColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Country country = cursorToCountry(cursor);
			countries.add(country);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return countries;
	}

	private Country cursorToCountry(Cursor cursor) {
		Country country = new Country();
		country.setId(cursor.getInt(0));
		country.setCountry(cursor.getString(1));
		return country;
	}
}
