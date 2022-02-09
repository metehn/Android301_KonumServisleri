package com.metehanersoy.android301_konumservisleri;

import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CustomLocationManager {
    private static final long UZAKLIK_DEGISIMI = 20;
    private static final long SURE = 1000 * 30;
    private Location mMevcutKonum;
    private LocationManager mLocationManager;
    private KonumListener mKonumListener;
    private static CustomLocationManager INSTANCE;

    private CustomLocationManager(Context context){
        mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        mKonumListener = new KonumListener();
    }

    public static CustomLocationManager getInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = new CustomLocationManager(context);
        }
        return INSTANCE;
    }

    public void baslaKonumGuncellemesi(){
        Criteria kriter = new Criteria();
        kriter.setAccuracy(Criteria.ACCURACY_COARSE);
        kriter.setAltitudeRequired(false);
        kriter.setSpeedRequired(false);
        kriter.setPowerRequirement(Criteria.POWER_MEDIUM);
        kriter.setCostAllowed(false);
        String bilgiSaglayici =
                mLocationManager.getBestProvider(kriter, true);
        if (bilgiSaglayici == null){
            List<String> bilgiSaglayicilar =
                    mLocationManager.getAllProviders();
            for (String tempSaglayici : bilgiSaglayicilar){
                if (mLocationManager.isProviderEnabled(tempSaglayici))
                    bilgiSaglayici = tempSaglayici;
            }
        }

        //kriterlere uyan bir LocationManager al
        mLocationManager.requestLocationUpdates(
                bilgiSaglayici,
                SURE,
                UZAKLIK_DEGISIMI,
                mKonumListener);
    }

    public void durdurKonumGuncellemesi(){
        if (mLocationManager != null)
            mLocationManager.removeUpdates(mKonumListener);
    }
    public Location getMevcutKonum() {
        return mMevcutKonum;
    }

    //Inner class - Dahili sınıf
    private class KonumListener implements LocationListener{
        public void onLocationChanged(Location location) {
            mMevcutKonum = location;
        }
        public void onStatusChanged(String provider,
                                    int status, Bundle extras){}
        public void onProviderDisabled(String provider){}
        public void onProviderEnabled(String provider) {}
    }
}
