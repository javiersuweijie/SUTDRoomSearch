package com.example.sutdroomsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MapActivity extends MainActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		// Generate bitmap		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.east_2nd, options);

		// Create touch image view
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
	}
}
