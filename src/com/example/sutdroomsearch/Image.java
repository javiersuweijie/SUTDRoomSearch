package com.example.sutdroomsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Image extends MainActivity{
	TouchImageView map;
	private int[] mapOffsetXY = new int[2];

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
		TouchImageView map = new TouchImageView(this);
        map.setImageBitmap(bm);
		map.setMaxZoom(4f);
		setContentView(map);
		
		map.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				TouchImageView mapView = (TouchImageView) v;
				// TODO Auto-generated method stub
				double touchX = event.getX() - mapOffsetXY[0];
				double touchY = event.getY() - mapOffsetXY[1];
				Log.i("info","Touched! X:"+ touchX + " Y: " + touchY);
				return false;
			}
		});
	}
	
	/**
	 * Hacky way of finding out the location of the map with respect to the screen
	 * We will use these values to translate co-ordinates that we get when it is touched.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		this.map.getLocationOnScreen(mapOffsetXY);
	}
}