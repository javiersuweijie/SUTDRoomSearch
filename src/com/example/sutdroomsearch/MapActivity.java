package com.example.sutdroomsearch;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends Activity {
	private int level;
	static SVG level2=null;
	static SVG level3=null;
	static SVG level4=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map);
		// Hide info pane by default
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.GONE);
		
		// Display level 2 map by default
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		map.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		handleIntentExtras();
	}
	
	/**
	 * Finds the person and the location associated with room number from search.
	 */
	public void handleIntentExtras() {
		Integer room = this.getIntent().getIntExtra("room_id",-1);
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		if(room != -1) {
			Location location = Location.getLocationById(room,getApplicationContext());
			Person person = location.person;
			switch ((int)location.level) {
			case 2:
				showL2Map(map);
				break;
			case 3:
				showL3Map(map);
				break;
			case 4:
				showL4Map(map);
				break;
			default:
				break;
			}
			highlightRoom(location);
			showPersonInfo(person);
		}
		else showL2Map(map);
		map.setMaxZoom(20f);
	}
	
	/**
	 * Show level 2 map
	 * @param view
	 */
	public void showL2Map(View view) {
		long start_time = System.currentTimeMillis();
		this.level = 2;
		TouchImageView map = (TouchImageView) findViewById(R.id.map);

		try {
			if (level2==null) level2 = SVG.getFromResource(this, R.drawable.level_2);
			Log.i("Time","Time to get SVG resource: "+(System.currentTimeMillis()-start_time));
			Drawable drawable = new PictureDrawable(level2.renderToPicture());
			map.setImageDrawable(drawable);		
			Log.i("Time","Time to draw SVG: "+(System.currentTimeMillis()-start_time));
			//clear pointer and info panel, when change to different level's map
			map.hidePin();
			LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
			info_pane.setVisibility(View.INVISIBLE);
		} catch (SVGParseException e) { }

		// TODO: highlight button to show that this is the current map
	}

	/**
	 * Show level 3 map
	 * @param view
	 */
	public void showL3Map(View view) {
		this.level = 3;
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		
		try {
			if (level3==null) level3 = SVG.getFromResource(this, R.drawable.level_3);
			Drawable drawable = new PictureDrawable(level3.renderToPicture());
			map.setImageDrawable(drawable);	
			//clear pointer and info panel, when change to different level's map
			map.hidePin();
			LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
			info_pane.setVisibility(View.INVISIBLE);
		} catch (SVGParseException e) { }

		// TODO: highlight button to show that this is the current map
	}

	/**
	 * Show level 4 map
	 * @param view
	 */
	public void showL4Map(View view) {
		this.level = 4;
		TouchImageView map = (TouchImageView) findViewById(R.id.map);

		try {
			if (level4==null) level4 = SVG.getFromResource(this, R.drawable.level_4);
			Drawable drawable = new PictureDrawable(level4.renderToPicture());
			map.setImageDrawable(drawable);	
			//clear pointer and info panel, when change to different level's map
			map.hidePin();
			LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
			info_pane.setVisibility(View.INVISIBLE);
		} catch (SVGParseException e) { }

		// TODO: highlight button to show that this is the current map
	}

	/**
	 * Select room by tapping on map
	 * @param view
	 */
	public void selectRoom(View view) {
		// Get tap coordinates
		TouchImageView mapView = (TouchImageView) view;
		Log.d("Info","Clicked image on: " + mapView.last.x + ", " + mapView.last.y);
		Matrix m = new Matrix();
		mapView.matrix.invert(m);
		float[] pts = {mapView.last.x, mapView.last.y};
		m.mapPoints(pts);
		Log.d("Info", "Mapped to real image: " + pts[0] + ", " + pts[1]);

		// Map tap coordinates to room/person
		
		Location room = Location.findClosestLocationTo(pts[0], pts[1], this.level, this.getApplicationContext());
		Person person = room.person;

		if (room != null) {
			// Highlight room
			highlightRoom(room);

			if(person != null) // Show person info
			showPersonInfo(person);
			
			//clear out info panel when there is no one use the room
			if(person==null){
				LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
				info_pane.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Highlight a room on the map
	 * @param id: id of room to highlight
	 */
	public void highlightRoom(Location room) {
		TouchImageView mapView = (TouchImageView) findViewById(R.id.map); 
		mapView.movePinRelative(room.x, room.y);
	}

	/**
	 * Display a person's info in details pane
	 * @param id: id of person's info to display
	 */
	public void showPersonInfo(Person person) {
		if(person == null) return;
		// Set name
		TextView info_name = (TextView) findViewById(R.id.info_name);
		info_name.setText(person.name);

		// Set room
		TextView info_room = (TextView) findViewById(R.id.info_room);
		info_room.setText(person.position);
		
		// Set phone number
		TextView info_phone = (TextView) findViewById(R.id.info_phone);
		info_phone.setText(person.number);
		
		// Set e-mail address
		TextView info_email = (TextView) findViewById(R.id.info_email);
		info_email.setText(person.email);
		
		// Show info pane
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.VISIBLE);
	}

	public void callPerson(View view) {
		TextView textView = (TextView) view;
		String uriStr = "tel:" + textView.getText().toString();
		Uri uri = Uri.parse(uriStr);
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setData(uri);
		startActivity(i);
	}

	public void emailPerson(View view) {
		TextView textView = (TextView) view;
		String uriStr = "mailto:" + textView.getText().toString();
		Uri uri = Uri.parse(uriStr);
		Intent i = new Intent(Intent.ACTION_SENDTO);
		i.setData(uri);
		startActivity(Intent.createChooser(i, "Send e-mail"));
	}

	/**
	 * Switch to the search activity
	 * @param view
	 */
	public void switchToSearch(View view) {
		Intent i = new Intent(MapActivity.this, SearchActivity.class);

		// TODO: add current value of search box to intent
		
		startActivity(i);
	}

	/**
	 * Clear the current search/room selection
	 * @param view
	 */
	public void clearSearch(View view) {
		// Clear search box
		
		// Hide info pane
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.GONE);
	}
}
