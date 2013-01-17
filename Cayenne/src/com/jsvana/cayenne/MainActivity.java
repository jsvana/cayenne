package com.jsvana.cayenne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        
        ((EditText)findViewById(R.id.username)).setText(username);
        ((EditText)findViewById(R.id.password)).setText(password);
        
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		new Login().execute("");
        	}
        });
    }
    
    private class Login extends AsyncTask<String, Void, String> {
    	private String _data;
    	private HttpClient _httpClient;
    	
    	@Override
    	protected String doInBackground(String... params) {
	    	String username = ((EditText)findViewById(R.id.username)).getText().toString();
	    	String password = ((EditText)findViewById(R.id.password)).getText().toString();
	    	
	    	if ("".equals(username) || "".equals(password)) {
	    		return null;
	    	}
	    	
	    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	    	
	    	Editor edit = prefs.edit();
	    	edit.putString("username", username);
	    	edit.putString("password", password);
	    	edit.commit();
	    	
	    	_httpClient = HTTPClient.INSTANCE.getHTTPClient();
	        HttpPost httppost = new HttpPost("http://piperka.net/updates.html");
	        
	        try {
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	            nameValuePairs.add(new BasicNameValuePair("user", username));
	            nameValuePairs.add(new BasicNameValuePair("passwd_clear", password));
	            nameValuePairs.add(new BasicNameValuePair("action", "Login"));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            // Execute HTTP Post Request
	            _httpClient.execute(httppost);
	            
	            // TODO: Open new activity, passing httpclient
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return _data;
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		Intent comicsPane = new Intent(getBaseContext(), ComicListActivity.class);
    		startActivity(comicsPane);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
