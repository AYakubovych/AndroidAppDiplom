package ddns.net.src.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

import java.util.concurrent.atomic.AtomicBoolean;

import ddns.net.src.activity.LogOutActivity;
import ddns.net.src.entities.LocationData;
import ddns.net.src.properties.RabbitMQProperties;

/**
* This service receives location data and sends it to RabbitMQ
 * DELAY_TIME_IN_MILLIS responsible for the time between sending data
*/

public class TrackingService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private LocationListener locationListener;
    private LocationManager locationManager;
    public static AtomicBoolean stopped = new AtomicBoolean(false);

    private final int DELAY_TIME_IN_MILLIS = RabbitMQProperties.DELAY_TIME_MIN * 1000 * 60;

    private LocationRabbitDataSource rabbitDataSource = new LocationRabbitDataSource();

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, LogOutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Tracer")
                .setContentText("You are tracked")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        System.out.println("Before runnable");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                System.out.println("Thread start");
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DELAY_TIME_IN_MILLIS / 3, 0, locationListener);

                //Location get and sent loop
                    synchronized (this) {
                        System.out.println("Before loop: " + !stopped.get() );
                        while (!stopped.get()) {
                        System.out.println("Loop start");

                        System.out.println(LogOutActivity.isTracking);

                        LocationData locationData = getLocationData(locationManager);

                        System.out.println(locationData);

                        rabbitDataSource.sendLocation(locationData);

                        //delay for loop
                        try {
                            wait(DELAY_TIME_IN_MILLIS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        new Thread(runnable).start();


        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private LocationData getLocationData(LocationManager locationManager){

        Location location;
        LocationData locationData = new LocationData();

        try {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationData.setLatitude(location.getLatitude());
            locationData.setLongitude(location.getLongitude());
        }catch (NullPointerException e){
            try {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationData.setLatitude(location.getLatitude());
                locationData.setLongitude(location.getLongitude());
            }catch (NullPointerException e1){
                try {
                    location = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
                    locationData.setLatitude(location.getLatitude());
                    locationData.setLongitude(location.getLongitude());
                }catch (NullPointerException e2){
                    Toast.makeText(getApplicationContext(), "Can't get your location!", Toast.LENGTH_SHORT).show();
                    e2.printStackTrace();
                }
            }
        }
        return locationData;
    }

}
