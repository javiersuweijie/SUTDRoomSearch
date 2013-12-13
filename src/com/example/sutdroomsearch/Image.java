package com.example.sutdroomsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Image extends MainActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map1);
		
		// Generate bitmap		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.east_2nd, options);

		// Set image for view
		ImageView view = (ImageView) findViewById(R.id.imageView1);		
		view.setImageBitmap(bm);
        
        // Set touch image view
		TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(bm);
		img.setMaxZoom(4f);
		setContentView(img);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TouchImageView mapView = (TouchImageView) v;
			    Log.d("Info","Clicked image on: " + mapView.last.x + ", " + mapView.last.y);
			    Matrix m = new Matrix();
			    mapView.matrix.invert(m);
			    float[] pts = {mapView.last.x, mapView.last.y };
			    m.mapPoints(pts);
			    Log.d("Info", "Mapped to real image: " + pts[0] + ", " + pts[1]);
			}
			
		});
	}
}
