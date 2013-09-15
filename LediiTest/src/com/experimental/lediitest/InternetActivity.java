package com.experimental.lediitest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InternetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_internet);
		
		//Allow internet in main thread
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.internet, menu);
		return true;
	}
	
	public void onLoad(View view) {
		//When the load-button is clicked
		loadFromInternet();
	}
	
	public void onSwapToUi(View view) {
		finish();
	}
	
	private boolean isEmpty(EditText etText) {
	    return etText.getText().toString().trim().length() == 0;
	}
	
	public String getInnerHtml(String htmlelem) {
		//Returns whatever is between html-tags
		String tmp = htmlelem.intern();
        int start = tmp.indexOf(">") + 1;
        int end = tmp.indexOf("</");
        return (tmp.substring(start, end));
	}
	
    public void loadFromInternet() {
    	EditText urltext = (EditText) findViewById(R.id.txt_InputHtml);
    	TextView webcontent = (TextView) findViewById(R.id.txt_WebContent);
    	
    	try {
    		String link = "";
    		String prefix = "http://";
    		if (isEmpty(urltext) == false) {
    			link = prefix + urltext.getText().toString();
    		} else {
    			link = prefix + "www.hig.no";
    		}
    		
    		System.out.println("Connecting to " + link);
    		
    		URL url = new URL(link);
    		HttpURLConnection web = (HttpURLConnection) url.openConnection();
    		InputStream is = web.getInputStream();
    		
    		BufferedReader reader = null;
    		try {
    			reader = new BufferedReader(new InputStreamReader(is));
    			String result = "";
    			while ((result = reader.readLine()) != null) {
    				//System.out.println(result);
    				if (result.contains("<title>")) {
    					System.out.println(result);
    					webcontent.setText(getInnerHtml(result.substring(result.indexOf("<title>"))));
    				}
    			}
    		} catch (IOException e) {
    			Log.e("HTTP:Content", "Error in http content " + e.toString());
    			webcontent.setText("Error: Could not read the document!");
    		} finally {
    			if (reader != null) {
    				try {
    					reader.close();
    				} catch (IOException e) {
    					Log.e("HTTP:Close", "Could not close the document " + e.toString());
    					webcontent.setText("Error: Could not close the document!");
    				}
    			}
    		}
    	} catch (Exception e) {
    		Log.e("HTTP:Connect", "Error in http connection " + e.toString());
    		webcontent.setText("Error: Unable to connect!");
    	}
    }
	
}
