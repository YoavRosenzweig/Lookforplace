package com.project.yoavr.lookforplace;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements LocationListener,FragmnetChanger {

    SharedPreferences preferences;
    Location currentLocation;
    LocationManager locationManager;
    public static boolean isLarge;
    public static double lan;
    public static double lng;
    public static boolean KM;
    IfBatteryCharged resiver;
    FragmentB fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    if(isLargeDevice())
    {
        isLarge=true;
    }
       //start the GPS servise for Catch the location of the user
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        final Handler handler = new Handler();

        Runnable getLocationByNetwork = new Runnable() {
            @Override
            public void run() {
                if (currentLocation == null) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, MainActivity.this);
                }

            }
        };
        handler.postDelayed(getLocationByNetwork, 5000);

        //add Fragment A do the main Activity
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        FragmentA fragmentA = new FragmentA();
        getFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, fragmentA).commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==12)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                Toast.makeText(this, "you must allow permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.mysetting){
        Intent intent= new Intent(MainActivity.this, Mysetting.class);
        startActivity(intent);
        }
        if (item.getItemId() == R.id.favorite) {
            Intent intent = new Intent(this, FavoriteList.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
        resiver = new IfBatteryCharged();

        //cheak if the Battery is chargeing
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(resiver, ifilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop the servise from looking new location and battery
        locationManager.removeUpdates(this);
        unregisterReceiver(resiver);
    }

    @Override
    public void onLocationChanged(Location location) {
        //after the GPS Caught open Fragment B with the corrected location
        currentLocation = location;
        lan = location.getLatitude();
        lng = location.getLongitude();

        fragmentB = new FragmentB(MainActivity.lan, MainActivity.lng);
        //cheak if Device "Large" or not
        if (isLargeDevice()) {
            getFragmentManager().beginTransaction().add(R.id.rightCotainer, fragmentB).commit();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
//cheak if the user  open with tablet or phone
    private boolean isLargeDevice() {
        isLarge = false;
        LinearLayout rightLayout = (LinearLayout) findViewById(R.id.rightCotainer);
        if (rightLayout != null) {
            isLarge = true;
        }
        return isLarge;
    }

    @Override
    public void changeFragments(double lan, double lng) {

       // after got result from api and showed results on adapter
        // the user presses on icon and got the new location of place
        // change Fragment B with new  location
        FragmentB fragmentB = new FragmentB(lan,lng);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("changeFrags");
        if(!isLargeDevice()) {
            transaction.replace(R.id.activity_main, fragmentB).commit();
        }
        else
        {
            transaction.replace(R.id.rightCotainer,fragmentB).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getFragmentManager().getBackStackEntryCount()>0)
        {

            getFragmentManager().popBackStack();
        }

    }

 // function that change Bitmap to string
    public static String BitmapToBase64(Bitmap image){
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String temp=Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }

        //// function that change Stinng to Bitmap
    public static Bitmap StringTOBItmap(String input) {
        try {
            byte[] encodeByte = Base64.decode(input, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
