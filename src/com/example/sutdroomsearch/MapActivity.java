package com.example.sutdroomsearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Hide info pane by default
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.GONE);
		
		// Display level 2 map by default
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_2nd, options);
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		map.setImageBitmap(img);
		map.setMaxZoom(4f);
	}

	public void showL2Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_2nd, options);
		map.setImageBitmap(img);
	}

	public void showL3Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_3rd, options);
		map.setImageBitmap(img);
	}

	public void showL4Map(View view) {
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_4th, options);
		map.setImageBitmap(img);
	}

	public void showRoomInfo(View view) {
		TouchImageView mapView = (TouchImageView) view;
		Log.d("Info","Clicked image on: " + mapView.last.x + ", " + mapView.last.y);
		Matrix m = new Matrix();
		mapView.matrix.invert(m);
		float[] pts = {mapView.last.x, mapView.last.y};
		m.mapPoints(pts);
		Log.d("Info", "Mapped to real image: " + pts[0] + ", " + pts[1]);

		// Set name
		TextView info_name = (TextView) findViewById(R.id.info_name);
		info_name.setText("Mapped to real image: " + pts[0] + ", " + pts[1]);
		
		// Show info pane
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.VISIBLE);
	}

	public void startSearch(View view) {
		Intent i = new Intent(MapActivity.this, SearchActivity.class);
		startActivity(i);
	}

	public void clearSearch(View view) {
		// Hide info pane
		LinearLayout info_pane = (LinearLayout) findViewById(R.id.info_pane);
		info_pane.setVisibility(View.GONE);
	}
}
