package com.example.atsproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mapact extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Marker marker1, marker2;
    LocationManager locationManager;
    Double latitude1, longitude1, latitude2, longitude2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapact);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(mapact.this, "Please switch on your gps for better performance", Toast.LENGTH_LONG).show();

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mapact.this, "Permissions are denied...pls give permission and login again!!", Toast.LENGTH_LONG).show();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mapact.this);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double mylat = location.getLatitude();
        double mylong = location.getLongitude();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("location").child("mylocation");
        databaseReference.setValue(mylat+"\n"+mylong);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location","status");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude","enable");
        Log.d("Longitude","enable");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude","disable");
        Log.d("Longitude","disable");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("location").child("sms");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);
                String[] val = value.split("\n");
                latitude1 = Double.parseDouble(val[0]);
                longitude1 = Double.parseDouble(val[1]);

                // Add a marker in Sydney and move the camera
                LatLng locate1 = new LatLng(latitude1,longitude1);

                if (marker1 != null){
                    marker1.remove();
                    mMap.clear();
                    marker1 = null;
                }
                if (marker1 == null){
                    marker1 = mMap.addMarker(new MarkerOptions().position(locate1).title("your vehicle!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locate1));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mapact.this, "failed to get location!!",Toast.LENGTH_LONG).show();
            }
        });

        databaseReference = firebaseDatabase.getReference("location").child("mylocation");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value2 = dataSnapshot.getValue(String.class);
                String[] val2 = value2.split("\n");
                latitude2 = Double.parseDouble(val2[0]);
                longitude2 = Double.parseDouble(val2[1]);

                // Add a marker in Sydney and move the camera
                LatLng locate2 = new LatLng(latitude2,longitude2);

                if (marker2 != null){
                    marker2.remove();
                    mMap.clear();
                    marker2 = null;
                }
                if (marker2 == null){
                    marker2 = mMap.addMarker(new MarkerOptions().position(locate2).title("your location!!!"));
                    marker2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locate2));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mapact.this, "failed to get current location!!",Toast.LENGTH_LONG).show();
            }
        });


    }
}
