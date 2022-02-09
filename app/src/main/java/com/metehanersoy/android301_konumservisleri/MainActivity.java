package com.metehanersoy.android301_konumservisleri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(izinKontrol != PackageManager.PERMISSION_DENIED){

            com.metehanersoy.android301_konumservisleri.CustomLocationManager customLocationManager = CustomLocationManager.getInstance(getApplicationContext());
            customLocationManager.baslaKonumGuncellemesi();
            Log.e("Latitude",customLocationManager.getMevcutKonum().getLatitude()+"");

        }

   }


    public class CustomLocationManager {
        private static final long UZAKLIK_DEGISIMI = 20;
        private static final long SURE = 1000 * 30;
        private Location mMevcutKonum;
        private LocationManager mLocationManager;
        private com.metehanersoy.android301_konumservisleri.CustomLocationManager.KonumListener mKonumListener;
        private static com.metehanersoy.android301_konumservisleri.CustomLocationManager INSTANCE;


        private CustomLocationManager(Context context) {

            mLocationManager = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            mKonumListener = new com.metehanersoy.android301_konumservisleri.CustomLocationManager.KonumListener();
        }

        public static com.metehanersoy.android301_konumservisleri.CustomLocationManager getInstance(Context context) {
            if (INSTANCE == null) {
                INSTANCE = new com.metehanersoy.android301_konumservisleri.CustomLocationManager(context);
            }
            return INSTANCE;
        }

        public void baslaKonumGuncellemesi() {
            Criteria kriter = new Criteria();
            kriter.setAccuracy(Criteria.ACCURACY_COARSE);
            kriter.setAltitudeRequired(false);
            kriter.setSpeedRequired(false);
            kriter.setPowerRequirement(Criteria.POWER_MEDIUM);
            kriter.setCostAllowed(false);
            String bilgiSaglayici = mLocationManager.getBestProvider(kriter, true);
            if (bilgiSaglayici == null) {
                List<String> bilgiSaglayicilar = mLocationManager.getAllProviders();
                for (String tempSaglayici : bilgiSaglayicilar) {
                    if (mLocationManager.isProviderEnabled(tempSaglayici))
                        bilgiSaglayici = tempSaglayici;
                }
            }
            mLocationManager.requestLocationUpdates(bilgiSaglayici, SURE, UZAKLIK_DEGISIMI, mKonumListener);
        }

        public void durdurKonumGuncellemesi(){
            if (mLocationManager != null)
                mLocationManager.removeUpdates(mKonumListener);
        }
        public Location getMevcutKonum() {
            return mMevcutKonum;
        }

        //Inner class - Dahili sınıf
        private class KonumListener implements LocationListener {
            public void onLocationChanged(Location location) {
                mMevcutKonum = location;
            }
            public void onStatusChanged(String provider,
                                        int status, Bundle extras){
            }
            public void onProviderDisabled(String provider){
                System.out.println("Konum Kapatıldı!");
                Log.e("Konum","Konum Kapatıldı!");
            }
            public void onProviderEnabled(String provider) {
                System.out.println("Konum Açıldı!");
                Log.e("Konum","Konum Açıldı!");
            }
        }
    }


}