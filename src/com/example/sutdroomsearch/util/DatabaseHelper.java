package com.example.sutdroomsearch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper sInstance = null;
	private static String DB_PATH = "/data/data/com.example.sutdroomsearch/databases/";
	private static final String DB_NAME = "development.db";
	private static final int DATABASE_VERSION = 1;
	private final Context myContext;
	private SQLiteDatabase db;
	 
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
		/*
		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			//database does not exist yet.
			Log.i("Database", "Database does not exist yet");
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB !=null ? true:false;
		*/
	}
	
	private void copyDataBase() throws IOException {
		Log.i("Database", "Copying Database");
		Log.i("Database", Arrays.toString(myContext.getAssets().list("")));

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
	
	public Cursor getPerson(int id) {
		return db.query("people", new String[] {"_id, name, location_id"},"_id=?",new String[] {String.valueOf(id)},null,null,null,null);
	}
}
