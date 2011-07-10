package com.wifi.information;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wifi.information.NetworkAccess;

public class main extends Activity implements OnClickListener{
	
	String wifiInformationString = null;
	TextView tv = null;
	TextView ping = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView) findViewById(R.id.wifiInformation);
        tv.setScrollContainer(true);
        tv.setText(populateWiFiInformation());
        
        ping = (TextView) findViewById(R.id.pingGoogle);
        ping.setText(pingGoogle());
        
        Button eMailBtn = (Button) findViewById(R.id.eMailBtn);
        eMailBtn.setOnClickListener(this);
        
        Button refreshBtn = (Button) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this);
    }

	private String populateWiFiInformation() {
		StringBuilder sb = new StringBuilder();
		
		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMan.getConnectionInfo();
		
		sb.append("SSID = " + getSSIDSafe(wifiInfo) + "\n");
		sb.append("BSSID = " + wifiInfo.getBSSID() + "\n");
		sb.append("IP Address = " + getWiFiIp(wifiInfo) + "\n");
		sb.append("Network ID = " + wifiInfo.getNetworkId() + "\n");
		sb.append("Rssi(Signal Strength) = " + wifiInfo.getRssi() + "\n");
		sb.append("Link Speed(Mbps) = " + wifiInfo.getLinkSpeed() + "\n");
		sb.append("WiFi MAC = " + wifiInfo.getMacAddress() + "\n");
		
		TelephonyManager telMan = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		sb.append("\n\n");
		sb.append("3G Information\n");
		sb.append("Connection Type: " + telMan.getNetworkType() + "\n");
		sb.append("Country: " + telMan.getNetworkCountryIso().toUpperCase() + "\n");
		sb.append("Network Name: " + telMan.getNetworkOperatorName() + "\n");
		sb.append("3G Data State: " + telMan.getDataState() + "\n");
		
		wifiInformationString = sb.toString();
		
		return sb.toString();
	}
	
	private String pingGoogle(){
		NetworkAccess net = new NetworkAccess();
		try{
			String ret = net.executeHttpGet();
			if(ret.equalsIgnoreCase("SocketException")){
				return ret;
			}else if(ret.equalsIgnoreCase("")){
				return "Unknown Exception. See Log";
			} else {
				return "Pinging Google: Sucessfully received a response from Google.";
			}
			
		} catch (Exception ex){
			Log.d("NetworkConnectivity", "Error: " + ex);
			return "There was an exception of type: " + ex.getLocalizedMessage();
		}
	}

	private String getWiFiIp(WifiInfo wifiInfo) {
		int ipAddress = wifiInfo.getIpAddress();
		
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff), 
											(ipAddress >> 8 & 0xff),
											(ipAddress >> 16 & 0xff),
											(ipAddress >> 24 & 0xff));
	}

	private String getSSIDSafe(WifiInfo wifiInfo){
		try{
			return wifiInfo.getSSID();
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.eMailBtn){
			Log.d("WiFiInfo", "E-Mail button pressed");
			
			Intent eMailIntent = new Intent(android.content.Intent.ACTION_SEND);
			
			eMailIntent.putExtra(android.content.Intent.EXTRA_TEXT, wifiInformationString);
			eMailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My WiFi Information");
			eMailIntent.setType("plain/text");
			startActivity(Intent.createChooser(eMailIntent, "E-Mail"));
		}
		
		if(v.getId() == R.id.refreshBtn){
			Log.d("WiFiInfo", "Refresh button pressed");
			
			tv.setText(populateWiFiInformation());
			ping.setText(pingGoogle());
		}
		
	}
}