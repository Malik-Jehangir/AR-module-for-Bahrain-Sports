package com.goys.android.app.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.goys.android.app.util.GoysLog;

public class DBHelper extends SQLiteOpenHelper {

	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.goys.android.app/databases/";

	private static String DB_NAME = "goys_db.sqlite";

	private static int DB_VERSION = 1;

	private SQLiteDatabase myDataBase;

	private Context myContext = null;

	public static String COLOUM_RESPONSE_EN = "response_en";
	public static String COLOUM_RESPONSE_AR = "response_ar";

//	// Matches Table.
//	public static String TABLE_MATCHES = "matches";
//	public static String COLOUM_MATCHES_MATCH_DATE = "match_date";
//	public static String COLOUM_MATCHES_COMPETITION_ID = "competition_id";
//
//	// Match Report Table.
//	public static String TABLE_MATCH_REPORT = "match_report";
//	public static String COLOUM_MATCH_REPORT_MATCH_ID = "match_id";
//
//	// Competition Table.
//	public static String TABLE_COMPETITION = "competition";
//
//	// National Team Table
//	public static String TABLE_NATIONAL_TEAM = "national_teams";
//
//	// Teams
//	public static String TABLE_TEAM = "teams";
//
//	// Clubs
//	public static String TABLE_CLUB = "clubs";
//	public static String COLOUM_CLUB_ID = "club_id";
//
//	// Players
//	public static String TABLE_PLAYERS = "players ";
//	public static String COLOUM_TEAM_ID = "team_id";
//
//	// Competition Details
//	public static String TABLE_COMPEITTION_DETAIL = "competition_detail";
//	public static String COLOUM_COMPETITION_ID = "competition_id";
//	public static String COLOUM_RESPONSE_TYPE = "response_type";
//
//	// News
//	public static String TABLE_NEWS = "news";
//
//	// Team Details
//	public static String TABLE_TEAMS_DETAILS = "team_detail";
//
//	// Player Details
//	public static String TABLE_PLAYER_DETAILS = "player_detail";
//	public static String COLOUM_PLAYER_ID = "player_id";
//
//	// Player Competitions Summary
//	public static String TABLE_PLAYER_COMPETITIONS_SUMMARY = "player_competitions_summary";
//	public static String COLOUM_SEASON_ID = "season_id";

	/*-------------------- Constructor--------------------- */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		myContext = context;

	}

	/*------------------Copy Database-------------------------------------*/

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.
			GoysLog.e("CHECK DATABSE LOG ERROR",
					"NO DATABASE FOUND, CREATING NEW DATABASE");

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public SQLiteDatabase openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	/*-------------------------------------------------------*/

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
