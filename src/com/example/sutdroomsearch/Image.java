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
		
		ImageView view=(ImageView)findViewById(R.id.imageView1);
		//String path="/storage/emulated/0/east_2nd.png";
		String path="/mnt/emmc/DCIM/100MEDIA/east_2nd.png";
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=2;
		Bitmap bm=BitmapFactory.decodeFile(path,options);
		view.setImageBitmap(bm);
		
		main=(FrameLayout)findViewById(R.id.b1);
        main.addView(new dot(this,20,120,25));
        
        TouchImageView img=new TouchImageView(this);
        img.setImageBitmap(bm);
		img.setMaxZoom(4f);
		setContentView(img);
		
	}
	
}
