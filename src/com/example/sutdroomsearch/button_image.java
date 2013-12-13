package com.example.sutdroomsearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class button_image extends Activity {
	Button level2_east;
	Button level3_east;
	Button level4_east;
	ImageView map;
	
	Integer[] imageIds={R.drawable.chunk1, R.drawable.chunk2, R.drawable.chunk3};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button_image);
		
		level2_east=(Button)findViewById(R.id.level2_east);
		level3_east=(Button)findViewById(R.id.level3_east);
		level4_east=(Button)findViewById(R.id.level4_east);
		map=(ImageView)findViewById(R.id.map_display);
		
		level2_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.setImageResource(imageIds[0]);
				Toast.makeText(getApplicationContext(), "Click on button 1", Toast.LENGTH_LONG).show();
			}
		});
		
		level3_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.setImageResource(imageIds[1]);
				Toast.makeText(getApplicationContext(), "Click on button 2", Toast.LENGTH_LONG).show();
			}
		});
		
		level4_east.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.setImageResource(imageIds[2]);
				Toast.makeText(getApplicationContext(), "Click on button 3", Toast.LENGTH_LONG).show();
			}
		});
	}

}
