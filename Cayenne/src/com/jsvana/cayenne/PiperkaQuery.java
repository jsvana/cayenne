package com.jsvana.cayenne;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class PiperkaQuery extends AsyncTask<String, Void, String> {
	private HttpClient _httpClient;
	private String _data;
	private Callback _callback;
	
	public interface Callback {
		public void callback(String data);
	}
	
	public void setCallback(Callback callback) {
		_callback = callback;
	}
	
	public String doInBackground(String... args) {
		_httpClient = HTTPClient.INSTANCE.getHTTPClient();
		
		HttpGet httpget = new HttpGet(args[0]);
            
        HttpResponse response = null;
        
		try {
			response = _httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (response == null) {
			return "";
		}
        
        try {
			_data = EntityUtils.toString(response.getEntity());
			response.getEntity().consumeContent();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
        
		return _data;
	}
	
	public void onPostExecute(String result) {
		_callback.callback(result);
	}
}