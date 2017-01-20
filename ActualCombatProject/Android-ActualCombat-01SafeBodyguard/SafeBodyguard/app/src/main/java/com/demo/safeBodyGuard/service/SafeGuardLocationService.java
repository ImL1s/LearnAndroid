package com.demo.safeBodyGuard.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.demo.safeBodyGuard.utils.MPermissionUtil;

/**
 * Created by iml1s-macpro on 2017/1/19.
 */

public class SafeGuardLocationService extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("debug", "onLocationService onCreate()");

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);


        if (Build.VERSION.SDK_INT > 23)
        {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestSingleUpdate(criteria, new MoveLocationListener(), null);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "需要權限！", Toast.LENGTH_LONG).show();
                MPermissionUtil.showTipsDialog(getApplicationContext());
            }
        }
        else
        {
//            locationManager.requestSingleUpdate(criteria, new MoveLocationListener(), null);
            locationManager.requestLocationUpdates(0, 0, criteria, new MoveLocationListener(), null);
        }

    }


    class MoveLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556", null, "Longitude: " + longitude + " latitude: " + latitude, null, null);
            Log.d("debug", "Longitude: " + longitude + " latitude: " + latitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }
    }
}
