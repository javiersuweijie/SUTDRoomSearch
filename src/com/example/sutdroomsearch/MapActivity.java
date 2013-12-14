package com.example.sutdroomsearch;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import android.annotation.SuppressLint;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Hide info pane by default
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.GONE);
		
		// Display level 2 map by default
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		map.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		showL2Map(map);
		map.setMaxZoom(8f);
	}

	/**
	 * Show level 2 map
	 * @param view
	 */
	public void showL2Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);

		try {
			SVG svg = SVG.getFromResource(this, R.drawable.level_2);
			Drawable drawable = new PictureDrawable(svg.renderToPicture());
			map.setImageDrawable(drawable);			
		} catch (SVGParseException e) { }

		// TODO: highlight button to show that this is the current map
	}

	/**
	 * Show level 3 map
	 * @param view
	 */
	public void showL3Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		
		try {
			SVG svg = SVG.getFromResource(this, R.drawable.level_3);
			Drawable drawable = new PictureDrawable(svg.renderToPicture());
			map.setImageDrawable(drawable);			
		} catch (SVGParseException e) { }

		// TODO: highlight button to show that this is the current map
	}

	/**
	 * Show level 4 map
	 * @param view
	 */
	public void showL4Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);

		try {
			SVG svg = SVG.getFromResource(this, R.drawable.level_4);
			Drawable drawable = new PictureDrawable(svg.renderToPicture());
			map.setImageDrawable(drawable);			
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
		int roomId = 0;
		int personId = 0;

		if (roomId != 0 && personId != 0) {
			// Highlight room
			highlightRoom(roomId);

			// Show person info
			showPersonInfo(personId);
		}
	}

	/**
	 * Highlight a room on the map
	 * @param id: id of room to highlight
	 */
	public void highlightRoom(int id) {

	}

	/**
	 * Display a person's info in details pane
	 * @param id: id of person's info to display
	 */
	public void showPersonInfo(int id) {
		// Set name
		TextView info_name = (TextView) findViewById(R.id.info_name);

		// Set room
		TextView info_room = (TextView) findViewById(R.id.info_room);
		
		// Set phone number
		TextView info_phone = (TextView) findViewById(R.id.info_phone);
		
		// Set e-mail address
		TextView info_email = (TextView) findViewById(R.id.info_email);
		
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
