package com.example.sutdroomsearch.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper sInstance = null;
	private static String DB_PATH = "/data/data/SUTDRoomSearch/databases/";
	private static final String DB_NAME = "development";
	private static final int DATABASE_VERSION = 1;
	private final Context myContext;
	private SQLiteDatabase db;
	 
	public static DatabaseHelper getInstance(Context context) {    
	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	  if (sInstance == null) {
	      sInstance = new DatabaseHelper(context.getApplicationContext());
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
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH+DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			//database does not exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB !=null ? true:false;
	}
	
	private void copyDataBase() throws IOException {
		InputStream input = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream output = new FileOutputStream(outFileName);
		
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer))>0) {
			output.write(buffer, 0, length);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public Cursor getPerson(int id) {
		return db.query("people", new String[] {"_id"},"_id=?",new String[] {String.valueOf(id)},null,null,null,null);
	}
}
