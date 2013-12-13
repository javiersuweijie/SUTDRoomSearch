package com.example.sutdroomsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class Phonecall extends Activity{
	TextView num;
	TextView email;
	String email_view;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_layout);
		
		email_view="wanglijuan0531@gmail.com";

		num = (TextView) findViewById(R.id.numdisplay);
		email=(TextView) findViewById(R.id.email);
		num.setText("tel: "+"9005 0008");
		email.setText("Email: "+email_view);

		// add PhoneStateListener
		PhoneCallListener phoneCallListener = new PhoneCallListener();
		TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(phoneCallListener,PhoneStateListener.LISTEN_CALL_STATE);
		
		num.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
				phoneCallIntent.setData(Uri.parse((String) num.getText()));
				startActivity(phoneCallIntent);
			}
		});
		
		email.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uriText="mailto:"+email_view;
				Uri uri=Uri.parse(uriText);
				Intent sendIntent=new Intent(Intent.ACTION_SENDTO);
				sendIntent.setData(uri);
				startActivity(Intent.createChooser(sendIntent, "send email"));
			}
		});

	}

	// monitor phone call states
	private class PhoneCallListener extends PhoneStateListener {

		String TAG = "LOGGING PHONE CALL";

		private boolean phoneCalling = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(TAG, "OFFHOOK");

				phoneCalling = true;
			}

			// When the call ends launch the main activity again
			if (TelephonyManager.CALL_STATE_IDLE == state) {

				Log.i(TAG, "IDLE");

				if (phoneCalling) {

					Log.i(TAG, "restart app");

					// restart app
					Intent i = getBaseContext().getPackageManager().
					getLaunchIntentForPackage(getBaseContext().getPackageName());

					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

					phoneCalling = false;
				}

			}
		}
	}
}
