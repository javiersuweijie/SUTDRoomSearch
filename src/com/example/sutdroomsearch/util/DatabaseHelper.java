package com.example.sutdroomsearch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.sutdroomsearch.Recommendation;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.GetChars;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper sInstance = null;
	private static String DB_PATH = "/data/data/com.example.sutdroomsearch/databases/";
	private static final String DB_NAME = "development.db";
	private static final int DATABASE_VERSION = 1;
	private final Context myContext;
	private SQLiteDatabase db;
	private ArrayList<String[]> reco_list;
	
	
	/**
	 * Singleton pattern
	 * Creates/Retrieves an instance of the databasehelper
	 * The database is opened automatically
	 * @param context
	 * @return
	 */
	public static DatabaseHelper getInstance(Context context) {    
	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	  if (sInstance == null) {
	      sInstance = new DatabaseHelper(context);
	      try {
			sInstance.createDataBase();
		} catch (IOException e) {
			throw new Error("Error creating database");
		}
	  }
	  sInstance.openDatabase();
	  return sInstance;
	}
	     
  /**
   * Constructor should be private to prevent direct instantiation.
   * make call to static factory method "getInstance()" instead.
   */
	private DatabaseHelper(Context context) {
	  super(context, DB_NAME, null, DATABASE_VERSION);
	  this.myContext = context;
  	}
	
	public void createDataBase() throws IOException {
		if (checkDataBase()) {
			
		} else {
			getWritableDatabase();
			
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH+DB_NAME;
		File dbFile = new File(myPath);
		return dbFile.exists();
	}
	
	private void copyDataBase() throws IOException {

		InputStream input=null;
		try {
			input = myContext.getAssets().open("development.db");
		} catch (IOException e) {
			Log.i("Database","Error occurred when opening development.db");
		}
		String outFileName = DB_PATH + DB_NAME;
		OutputStream output = null;
		File file = new File(DB_PATH);
		Log.i("Database",Boolean.toString(file.isDirectory()));
		try {
			output = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
		while ((length = input.read(buffer))>0) {
			output.write(buffer, 0, length);
			}
		} catch (IOException e) {
			Log.i("Database", "Error caught when writing stream");
		}	
		output.flush();
		output.close();
		input.close();
	}
	
	public void openDatabase() throws SQLException {
		String path = DB_PATH + DB_NAME;
		this.db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	@Override
	public synchronized void close() {
		if (this.db != null) db.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Queries the database for all rooms and people
	 * @return ArrayList<String[]> {user_id, user_name, room_id, room_name}
	 */
	public ArrayList<String[]> getAllReco() {
		if (reco_list != null) return reco_list;
		ArrayList<String[]> rlist = new ArrayList<String[]>();
		Cursor room_name = db.rawQuery("SELECT _id,rname FROM locations", null);
		if (room_name.moveToFirst()) {
			do {
				Cursor user = db.rawQuery("SELECT _id,name FROM people WHERE location_id="+room_name.getString(0),null);
				String[] s = new String[4];
				if (user.moveToFirst()) {
					s[0] = user.getString(0);
					s[1] = user.getString(1);
					s[2] = room_name.getString(0);
					s[3] = room_name.getString(1);
				}
				else {
					s[0] = null;
					s[1] = null;
					s[2] = room_name.getString(0);
					s[3] = room_name.getString(1);
				}
				rlist.add(s);
			} while (room_name.moveToNext());
		}
		this.close();
		return rlist;
	}
	
	/**
	 * Query the database for a person by his/her id
	 * The method closes the database before returning
	 * @param id
	 * @return String[] {id, name, position, email, number, location_id}
	 */
	public String[] getPersonById(int id) {
		Cursor cu = db.rawQuery("SELECT _id,name,position,email,number,location_id FROM people WHERE _id="+id, null);
		String[] result = new String[6];
		if (cu.moveToFirst()) {
			result[0] = cu.getString(0);
			result[1] = cu.getString(1);
			result[2] = cu.getString(2);
			result[3] = cu.getString(3);
			result[4] = cu.getString(4);
			result[5] = cu.getString(5)==null?"-1": cu.getString(5);
			this.close();
			return result;
		}
		this.close();
		return null;
	}
	
	/**
	 * Queries the database for a person by his/her name
	 * The method closes the database before returning
	 * @param name
	 * @return String[] {id, name, position, email, number, location_id}
	 */
	public ArrayList<String[]> getPersonByName(String s) {
		String rawQuery = "SELECT _id,name,position,email,number,location_id FROM people WHERE name LIKE "+"\"%"+s+"%\";";
		Cursor cu = db.rawQuery(rawQuery, null);
		ArrayList<String[]> al = new ArrayList<String[]>();
		if (cu.moveToFirst()) {
			do {
			String[] result = new String[6];
			result[0] = cu.getString(0);
			result[1] = cu.getString(1);
			result[2] = cu.getString(2);
			result[3] = cu.getString(3);
			result[4] = cu.getString(4);
			result[5] = cu.getString(5)==null?"-1": cu.getString(5);
			al.add(result);
			} while (cu.moveToNext());
			this.close();
			return al;
		}
		this.close();
		return null;
	}
	
	/**
	 * Queries the database for a room by its name
	 * The method closes the database
	 * @param room_name (case insensitive)
	 * @return String[] { _id, xcoord, ycoord, level, rname, user_id}
	 */
	public String[] getRoomByName(String s) {
		String rawQuery = "SELECT _id,xcoord,ycoord,level,rname FROM locations WHERE rname LIKE "+"\"%"+s+"%\";";
		Cursor cu =  db.rawQuery(rawQuery, null);
		String[] result = new String[6];
		if (cu.moveToFirst()) {
			result[0] = cu.getString(0);
			result[1] = cu.getString(1);
			result[2] = cu.getString(2);
			result[3] = cu.getString(3);
			result[4] = cu.getString(4);
			Cursor cu2 = db.rawQuery("SELECT _id FROM people WHERE location_id="+result[0], null);
			result[5] = cu2.getString(5);
		}
		this.close();
		return result;
		
	}

	/**
	 * Queries the database for a froom by its id
	 * The method closes the database
	 * @param id
	 * @return String[] { _id, xcoord, ycoord, level, rname, user_id}
	 */
	public String[] getRoomById(int i) {
		Cursor cu = db.rawQuery("SELECT _id,xcoord,ycoord,level,rname,user_id FROM locations WHERE _id="+i, null);
		Cursor cu2 = db.rawQuery("SELECT _id FROM people WHERE location_id="+i, null);
		String[] result = new String[6];
		if (cu.moveToFirst()) {
			result[0] = cu.getString(0);
			result[1] = cu.getString(1);
			result[2] = cu.getString(2);
			result[3] = cu.getString(3);
			result[4] = cu.getString(4);
			result[5] = cu2.moveToFirst()?cu2.getString(0):"-1";
		}
		this.close();
		return result;
	}
	
	/**
	 * Gets all the room on a certain level
	 * @param level
	 * @return ArrayList<String[]> {_id,xcoord,ycoord,level}
	 */
	public ArrayList<String[]> getAllRoomsOnLevel(int level) {
		Cursor c = db.rawQuery("SELECT _id,xcoord,ycoord,level FROM locations WHERE level = " + level, null);
		ArrayList<String[]> al = new ArrayList<String[]>();
		if (c.moveToFirst()) {
			do {
				String[] each = new String[4];
				each[0] = c.getString(0);
				each[1] = c.getString(1);
				each[2] = c.getString(2);
				each[3] = c.getString(3);
				al.add(each);
			} while (c.moveToNext());
		}
		this.close();
		return al;
	}
}
