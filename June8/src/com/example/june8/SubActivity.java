package com.example.june8;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends Activity {
		String lat;
		String lon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
    
		// ƒf[ƒ^‚Ìóæ
		Intent intent = getIntent();
        lat = intent.getStringExtra("Latitude");
        lon = intent.getStringExtra("Longitude");
        Kaerimichi(lat,lon);
	}
	
	private  void  Kaerimichi(String latitude,String longitude) {	
        	Intent intent = new Intent();
        	intent.setAction(Intent.ACTION_VIEW);
        	intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        	intent.setData(Uri.parse("http://maps.google.com/maps?saddr="+lat+","+lon+"&daddr=“Œ‹‰w&dirflg=r"));
        	startActivity(intent);
	}

}