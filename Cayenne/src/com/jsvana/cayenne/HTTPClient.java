package com.jsvana.cayenne;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HTTPClient {
	public static HTTPClient INSTANCE = new HTTPClient();
	
	private HttpClient _httpClient = null;
	
	private HTTPClient() {
		_httpClient = new DefaultHttpClient();
		ClientConnectionManager mgr = _httpClient.getConnectionManager();
	    HttpParams params = _httpClient.getParams();
	 
	    _httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, 
	            mgr.getSchemeRegistry()), params);
	}
	
	public HttpClient getHTTPClient() {
		return _httpClient;
	}
}
