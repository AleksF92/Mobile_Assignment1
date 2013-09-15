package com.experimental.lediitest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	//Declear table and fields
	public static final String DB_TABLE = "statistics";
	public static final String DB_FILE = DB_TABLE + ".db";
	public static final String DBR_ID = "id";
	public static final String DBR_CLICKS = "clicks";
	public static final String DBR_LOWEST = "lowest";
	public static final String DBR_HIGHEST = "highest";
	
	//The SQL query for creating the table
	private static final String query = "CREATE TABLE statistics ("
		+ DBR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DBR_CLICKS + " INTEGER, "
		+ DBR_LOWEST + " INTEGER, "
		+ DBR_HIGHEST + " INTEGER"
		+ ")";
	
	public MySQLiteHelper(Context context) {
		super(context, DB_FILE, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		//Execute creation query
		database.execSQL(query);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Warning that content will be cleared
		Log.w(MySQLiteHelper.class.getName(),
		"Upgrading database from version " + oldVersion + " to "
		+ newVersion + ", which will destroy all old data");
		
		//Deletes the current table and creates a new empty one
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		onCreate(db);
	}
	
	public void updateStats(int clicks, int lowest, int highest) {
		//Get database reference
		SQLiteDatabase db = this.getWritableDatabase();
		
		//Group up content to store in the database
		ContentValues stats = new ContentValues();
		stats.put(DBR_CLICKS, clicks);
		stats.put(DBR_LOWEST, lowest);
		stats.put(DBR_HIGHEST, highest);
		
		//Clear the database and renew it
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		onCreate(db);
		
		//Insert the new data
		db.insert(DB_TABLE, null, stats);
		
		//Done with database
		db.close();
	}
	
	public int[] getStats() {
		int[] results = new int[3];
		
		//Get database reference
		SQLiteDatabase database = this.getReadableDatabase();
		
		//Select query, get all content
		String selectQuery = "SELECT * FROM " + DB_TABLE;
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			//Found something, load the data
			results[0] = cursor.getInt(1);
			results[1] = cursor.getInt(2);
			results[2] = cursor.getInt(3);
		} else {
			//No elements found, return default
			results[0] = -1;
			results[1] = -1;
			results[2] = -1;
		}
		
		return results;
	}
}