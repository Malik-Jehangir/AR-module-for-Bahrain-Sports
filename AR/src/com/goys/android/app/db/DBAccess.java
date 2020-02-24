package com.goys.android.app.db;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.GoysLog;

public class DBAccess {

	private static SQLiteDatabase database;

	private DBHelper dbHelper;

	private String TAG = "DBAccess";

	public DBAccess(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() {
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}

		database = dbHelper.openDataBase();

	}

	public void close() {
		dbHelper.close();
		database.close();
	}

	public ArrayList<String> selectCountries(int langType) {
		open();
		ArrayList<String> listObj = new ArrayList<String>();
		String query = "SELECT * FROM " + AppConstants.TABLE_COUNTRIES;
		GoysLog.d(TAG, "Select Countries");
		try {
			Cursor c = database.rawQuery(query, null);
			c.moveToFirst();

			if (c.getCount() > 0) {

				while (!c.isAfterLast()) {

					// Country country = new Country();
					// country.setId(c.getString(0));
					// country.setCountry(c.getString(1));
					listObj.add(c.getString(1));
					// System.out.println(country);

					c.moveToNext();
				}
			}

		} catch (Exception e) {

			if (e != null) {
				GoysLog.e(TAG, e.getMessage() + "");
			}
		} finally {
			close();
		}

		return listObj;
	}

}
