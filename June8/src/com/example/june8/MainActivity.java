package com.example.june8;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

////////////////////////////////////////////////
//
//　位置情報の取得
//
////////////////////////////////////////////////

public class MainActivity extends Activity {
	String temp;
	String latitude;
	String longitude;

	private LocationManager locationManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonmail = (Button) findViewById(R.id.buttonmail);
		buttonmail.setOnClickListener(mailListener);
		Button buttonkaeri = (Button) findViewById(R.id.buttonkaeri);
		buttonkaeri.setOnClickListener(subListener);
		Button buttonnow = (Button) findViewById(R.id.buttonnow);
		buttonnow.setOnClickListener(mButtonnowListener);
	}

	private OnClickListener mButtonnowListener = new OnClickListener() {
		public void onClick(View v) {
			if (locationManager != null) {
				locationManager.removeUpdates(mLocationListener);
			}
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			// 3Gまたはwifiから位置情報を取得する設定
			boolean networkFlg = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			// GPSから位置情報を取得する設定
			boolean gpsFlg = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000L, 0,
					mLocationListener);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000L, 0, mLocationListener);
		}
	};

	private OnClickListener mailListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intents = new Intent(MainActivity.this, MailActivity.class);
			// データを送る
			intents.putExtra("Latitude", latitude);
			intents.putExtra("Longitude", longitude);
			intents.putExtra("temp", temp);
			startActivity(intents);
		}
	};

	private OnClickListener subListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intents = new Intent(MainActivity.this, SubActivity.class);
			// データを送る
			intents.putExtra("Latitude", latitude);
			intents.putExtra("Longitude", longitude);
			startActivity(intents);
		}
	};

	private LocationListener mLocationListener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			latitude = Double.toString(location.getLatitude());
			longitude = Double.toString(location.getLongitude());
			getReuest(latitude, longitude);
		}
	};

	@Override
	protected void onPause() {
		if (locationManager != null) {
			locationManager.removeUpdates(mLocationListener);
		}
		super.onPause();
	}

	private void getReuest(String latitude, String longitude) {
		// 逆ジオコーディングサービス
		// http://www.finds.jp/wsdocs/rgeocode/index.html.ja
		String requestURL = "http://www.finds.jp/ws/rgeocode.php?json&lat="
				+ latitude + "&lon=" + longitude;
		Task task = new Task();
		task.execute(requestURL);
	}

	protected class Task extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(params[0]);
			byte[] result = null;
			String rtn = "";
			try {
				HttpResponse response = client.execute(get);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
					result = EntityUtils.toByteArray(response.getEntity());
					rtn = new String(result, "UTF-8");
				}
			} catch (Exception e) {
			}
			return rtn;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject json = new JSONObject(result);

				// 逆ジオコーディングサービス
				String status = json.getString("status");
				if (status.equals("200")) {
					JSONObject res = json.getJSONObject("result");
					String pname = res.getJSONObject("prefecture").getString(
							"pname");
					String mname = res.getJSONObject("municipality").getString(
							"mname");
					String section = res.getJSONArray("local").getJSONObject(0)
							.getString("section");
					temp = pname + mname + section;
					Toast.makeText(getApplicationContext(), temp,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "error!",
							Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "error!!!",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}
}