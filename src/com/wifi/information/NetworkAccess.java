package com.wifi.information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
/*
 * This class makes calls to the internet. Make all calls to this 
 * class in a seperate thread or an async thread.
 */

import android.util.Log;

public class NetworkAccess {
	
		private final static String TAG = "NetworkAccess";
		
		public String executeHttpGet() throws Exception{
			BufferedReader in = null;
			
			try{
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI("http://72.14.204.99")); // Ping google.com
				HttpResponse response = client.execute(request);
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.seperator");
				
				while((line = in.readLine()) != null){
					String temp = line + NL;
					sb.append(temp);
				}
				
				in.close();
				
				return sb.toString();
			} catch (SocketException e) {
				Log.d(TAG, "SocketException: " + e);
				return "SocketException";
			} catch (IOException e){
				Log.d(TAG, "IOException: " + e);
				return null;
			}finally {
				if(in != null){
					try{
						in.close();
					} catch (IOException ex){
						Log.d(TAG, "Error closing stream reader");
						Log.d(TAG, ex.getStackTrace().toString());
					}
				}	
				
			}
	}
	
	

}
