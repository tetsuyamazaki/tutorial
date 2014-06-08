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

public class MailActivity extends Activity {
		String lat;
		String lon;
		String temp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
    
		// データの受取
		Intent intent = getIntent();
        lat = intent.getStringExtra("Latitude");
        lon = intent.getStringExtra("Longitude");
        temp = intent.getStringExtra("temp");
        googleMap(lat,lon);
	}
	private  void  googleMap(String latitude,String longitude) {
		String urlStrng = "https://www.google.co.jp/maps?q="+lat;
		urlStrng += "," + lon;
		// 地図をメールで送る
		Uri uri=Uri.parse("mailto:t.yamayaki@gmail.com"); 
  		Intent intent=new Intent(Intent.ACTION_SENDTO,uri); 
    	intent.putExtra(Intent.EXTRA_SUBJECT,temp+"にいます"); 
    	intent.putExtra(Intent.EXTRA_TEXT,urlStrng); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    	//////////////////////////////////////////
    	startActivity(intent); 
	}

}
	

        