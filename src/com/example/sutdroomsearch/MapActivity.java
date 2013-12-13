package com.example.sutdroomsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends MainActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		// Generate bitmap		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_2nd, options);

		// Set touch image view
		TouchImageView map = (TouchImageView) findViewById(R.id.map);
		map.setImageBitmap(img);
		map.setMaxZoom(4f);
		map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TouchImageView mapView = (TouchImageView) v;
				Log.d("Info","Clicked image on: " + mapView.last.x + ", " + mapView.last.y);
				Matrix m = new Matrix();
				mapView.matrix.invert(m);
				float[] pts = {mapView.last.x, mapView.last.y};
				m.mapPoints(pts);
				Log.d("Info", "Mapped to real image: " + pts[0] + ", " + pts[1]);
			}
		});

		Button level2_east = (Button) findViewById(R.id.level2_east);
		Button level3_east = (Button) findViewById(R.id.level3_east);
		Button level4_east = (Button) findViewById(R.id.level4_east);

		level2_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				TouchImageView map = (TouchImageView) findViewById(R.id.map);
				map.setImageResource(R.drawable.east_2nd);
				Toast.makeText(getApplicationContext(), "Click on button 1", Toast.LENGTH_LONG).show();
			}
		});
		
		level3_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				TouchImageView map = (TouchImageView) findViewById(R.id.map);
				map.setImageResource(R.drawable.east_3rd);
				Toast.makeText(getApplicationContext(), "Click on button 2", Toast.LENGTH_LONG).show();
			}
		});
		
		level4_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				TouchImageView map = (TouchImageView) findViewById(R.id.map);
				map.setImageResource(R.drawable.east_4th);
				Toast.makeText(getApplicationContext(), "Click on button 3", Toast.LENGTH_LONG).show();
			}
		});
	}
}
