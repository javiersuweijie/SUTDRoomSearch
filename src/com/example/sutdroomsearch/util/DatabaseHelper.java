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
	private ArrayList<Recommendation> reco_list;
	 
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
	
	public ArrayList<Recommendation> getAllReco() {
		if (reco_list != null) return reco_list;
		ArrayList<Recommendation> rlist = new ArrayList<Recommendation>();
		Recommendation r;
		Cursor room_name = db.rawQuery("SELECT rname,user_id FROM locations", null);
		
		if (room_name.moveToFirst()) {
			do {
				Cursor user = db.rawQuery("SELECT name FROM people WHERE _id="+room_name.getString(1),null);
				if (user.moveToFirst()) {
					r = new Recommendation(user.getString(0),room_name.getString(0));
				}
				else {
					r = new Recommendation(room_name.getString(0));
				}
				rlist.add(r);
			} while (room_name.moveToNext());
		}
		return rlist;
	}
	
	public String[] getPerson(int id) {
		Cursor cu = db.rawQuery("SELECT _id,name,position,email,number,location_id FROM people WHERE _id="+id, null);
		String[] result = new String[6];
		if (cu.moveToFirst()) {
			result[0] = cu.getString(0);
			result[1] = cu.getString(1);
			result[2] = cu.getString(2);
			result[3] = cu.getString(3);
			result[4] = cu.getString(4);
			result[5] = cu.getString(5)==null?"-1": cu.getString(5);
			return result;
		}
		return null;
	}
	
	public Cursor getPersonByName(String s) {
		String rawQuery = "SELECT * FROM people WHERE name LIKE "+"\"%"+s+"%\";";
		return db.rawQuery(rawQuery, null);
	}

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
			return result;
		}
		return null;
	}
	
	public ArrayList<String[]> getAllRooms() {
		Cursor c = db.rawQuery("SELECT _id,xcoord,ycoord,level,rname,user_id FROM locations", null);
		ArrayList<String[]> al = new ArrayList<String[]>();
		if (c.moveToFirst()) {
			do {
				String[] each = new String[3];
				each[0] = c.getString(0);
				each[1] = c.getString(1);
				each[2] = c.getString(2);
				each[3] = c.getString(3);
				each[4] = c.getString(4);
				each[5] = c.getString(5).equals("null")?"-1":c.getString(5);
				al.add(each);
			} while (c.moveToNext());
		}
		return al;
	}
}
