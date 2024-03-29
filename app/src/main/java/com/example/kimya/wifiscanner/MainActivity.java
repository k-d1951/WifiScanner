package com.example.....wifiscanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.*;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends Activity {

        TextView mainText;
        WifiManager mainWifi;
        WifiReceiver receiverWifi;
        List<ScanResult> wifiList;
        StringBuilder sb = new StringBuilder();

        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            mainText = (TextView) findViewById(R.id.mainText);

            // Initiate wifi service manager
            mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            // Check for wifi is disabled
            if (mainWifi.isWifiEnabled() == false)
            {
                // If wifi disabled then enable it
                Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                        Toast.LENGTH_LONG).show();

                mainWifi.setWifiEnabled(true);
            }

            // wifi scaned value broadcast receiver
            receiverWifi = new WifiReceiver();

            // Register broadcast receiver
            // Broacast receiver will automatically call when number of wifi connections changed
            registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mainWifi.startScan();
            mainText.setText("Starting Scan...");
        }

        public boolean onCreateOptionsMenu(Menu menu) {
            menu.add(0, 0, 0, "Refresh");
            return super.onCreateOptionsMenu(menu);
        }

        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            mainWifi.startScan();
            mainText.setText("Starting Scan");
            return super.onMenuItemSelected(featureId, item);
        }

        protected void onPause() {
            unregisterReceiver(receiverWifi);
            super.onPause();
        }

        protected void onResume() {
            registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            super.onResume();
        }



        // Broadcast receiver class called its receive method
        // when number of wifi connections changed

        class WifiReceiver extends BroadcastReceiver {
            public double calculateDistance(double signalLevelInDb, double freqInMHz) {
                double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
                return Math.pow(10.0, exp);
            }
            // This method call when number of wifi connections changed
            public void onReceive(Context c, Intent intent) {

                sb = new StringBuilder();
                wifiList = mainWifi.getScanResults();
                sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");

                for(int i = 0; i < wifiList.size(); i++){

                    sb.append(new Integer(i+1).toString() + ". ");
                    sb.append(wifiList.get(i).SSID+"lol \n");
                    sb.append(wifiList.get(i).BSSID+"\n");
                    sb.append((wifiList.get(i)).toString());
                    sb.append("\nDistance:"+calculateDistance(wifiList.get(i).level, wifiList.get(i).frequency));

                    sb.append("\n\n");
                }

                mainText.setText(sb);
            }

        }
    }
   // @Override
    //protected void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    //}
//}
