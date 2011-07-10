package com.wifi.information;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle xtras = intent.getExtras();
		Boolean somethingWentWrong = false;
		
		if(xtras.getBoolean("EXTRA_NO_CONNECTIVITY")){
			Log.d("WiFi Info", "No network to fall back to.");
		}else if( xtras.getBoolean("FAILOVER_CONNECTION")){
			Log.d("WiFi Info", "Attempting to fall back to another network...");
		}
		
		//TODO: Do this nicer
		try{
			Log.d("Wifi Info", xtras.getString("EXTRA_EXTRA_INFO"));
		}catch (NullPointerException e){
			Log.d("Wifi Info", "None");
		}
		
		try{
			WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
			String macAddress = wifiInfo.getMacAddress();
			
			if(macAddress == null || macAddress.equalsIgnoreCase("")){
				somethingWentWrong = true;
			}
		}catch (Exception e){
			somethingWentWrong = true;
		}
		
		if(somethingWentWrong){
			NotificationManager notify = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			int icon = R.drawable.sad;
			String tickerText = "Your MAC address is null!!!";
			long when = System.currentTimeMillis();
			
			String notificationTitle = "WiFi Info";
			String notificationDescription = "Your MAC address is null!!!";
			
			Intent notificationIntent = new Intent(context, main.class);
			notificationIntent.putExtra("EXTRA_CLEAR_NOTIFICATION", true);
			
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			
			Notification n = new Notification(icon, tickerText, when);
			n.setLatestEventInfo(context, notificationTitle, notificationDescription, contentIntent);
			
			notify.notify(0, n);
		}
		
	}

}
