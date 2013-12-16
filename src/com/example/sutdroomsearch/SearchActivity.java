package com.example.sutdroomsearch;

import java.util.ArrayList;

import com.example.sutdroomsearch.util.DatabaseHelper;

import android.os.Bundle;
import android.util.Log;
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
		
		DatabaseHelper dbh = DatabaseHelper.getInstance(getApplicationContext());
		ArrayList<String[]> reco_list = dbh.getAllReco();
		Recommendation[] locations = new Recommendation[reco_list.size()];
		int i=0;
		for (String[] rec:reco_list) {
			locations[i] = new Recommendation(rec[0], rec[1], rec[2], rec[3]);
			i++;
		}
		
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
				sendMessage(adapter.getItem(position).room_id);
            }
        });
	}

	public void sendMessage(int message) {
		Intent i = new Intent(SearchActivity.this, MapActivity.class);
		i.putExtra("room_id", message);
		startActivity(i);
	}
	
	public void clearSearch(View view) {
		AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.search_box);
		autoComplete.setText("");
	}
}
