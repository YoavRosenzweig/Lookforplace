package com.project.yoavr.lookforplace;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentB extends Fragment {
    double latitude;
    double longitude;
    public FragmentB() {
        // Required empty public constructor
    }

    public FragmentB(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_b, container, false);
//put Map on fragment B

        MapFragment mapFragment= new MapFragment();
        getFragmentManager().beginTransaction().add(R.id.mapcontiner , mapFragment ).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//mark the right location
                LatLng latLng= new LatLng(MainActivity.lan,MainActivity.lng);
                CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng, 15);

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                googleMap.moveCamera(update);
                //mark the new location after user Clicked on the icon
                LatLng latLng2= new LatLng(latitude,longitude);
                CameraUpdate update2= CameraUpdateFactory.newLatLngZoom(latLng2, 15);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng2)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                googleMap.moveCamera(update2);
            }
        });
        return view;

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.setting, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite) {
            Intent intent = new Intent(getActivity(), FavoriteList.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.mysetting) {
            Intent intent= new Intent(getActivity(), Mysetting.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
