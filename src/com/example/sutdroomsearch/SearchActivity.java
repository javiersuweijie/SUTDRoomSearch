package com.example.sutdroomsearch;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.app.Activity;
import android.content.Intent;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		Recommendation[] locations = {
				new Recommendation("L1-S18"),
				new Recommendation("L4-R25"),
				new Recommendation("L2-S21"),
				new Recommendation("Simon Lui","L4-R3"),
				new Recommendation("Man Cheung","L3-R16"),
				new Recommendation("Peter Loh","L1-R47"),
				new Recommendation("Simon Hui","L2-S43"),
				new Recommendation("Wan Cheung","L3-S35")
		};
		
		AllMatchArrayAdapter<Recommendation> adapter = new AllMatchArrayAdapter<Recommendation>(this, android.R.layout.simple_dropdown_item_1line,locations);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.search_box);  
        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(1);
        
        autoComplete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.search_box);
            	autoComplete.showDropDown();
			}
		});
        
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.search_box);
				AllMatchArrayAdapter<Recommendation> adapter = (AllMatchArrayAdapter<Recommendation>)autoComplete.getAdapter();
				sendMessage(adapter.getItem(position).toString());
            }
        });
	}

	public void sendMessage(String message) {
		Intent i = new Intent(SearchActivity.this, MapActivity.class);
		i.putExtra("room_number", message);
		startActivity(i);
	}
	
	public void clearSearch(View view) {
		AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.search_box);
		autoComplete.setText("");
	}
}
