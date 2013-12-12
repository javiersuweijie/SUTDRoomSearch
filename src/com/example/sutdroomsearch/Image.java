package com.example.sutdroomsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Image extends MainActivity{
	FrameLayout main;
	
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
	}
	
}
