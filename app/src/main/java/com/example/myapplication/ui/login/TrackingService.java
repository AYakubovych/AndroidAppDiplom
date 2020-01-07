package com.example.myapplication.ui.login;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TrackingService extends IntentService {

    private String DATA_SEND_URL = "http://10.0.2.2:8080/ayakubovych_war/android_data_update";

    private LocationListener locationListener;
    private LocationManager locationManager;
    private FullData fullData;

    public TrackingService(){
        super("TrackingService");
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        fullData = FullData.getFullData();


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                fullData.setLongitude(location.getLongitude());
                fullData.setLatitude(location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);



        while (true) {

            synchronized (this) {
                try {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    //Network
                    fullData.setLatitude(location.getLatitude());
                    fullData.setLongitude(location.getLongitude());
                }catch (NullPointerException e){
                    try {
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        //GPS
                        fullData.setLatitude(location.getLatitude());
                        fullData.setLongitude(location.getLongitude());
                    }catch (NullPointerException z){
                        try {
                            Location location = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
                            //Passive
                            fullData.setLatitude(location.getLatitude());
                            fullData.setLongitude(location.getLongitude());
                        }catch (NullPointerException c){
                        }
                    }
                }

                //fullData send
                dataSend(fullData.getUrlSufix());

                //wait
                try {
                    wait(1000);
                } catch (Exception e) {
                }
            }
        }
    }


    private void dataSend(String sufix){
        String urlsufix = sufix;

        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(DATA_SEND_URL + urlsufix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result;
            result = bufferedReader.readLine();
            if(result != null) {
                System.out.println("response: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
