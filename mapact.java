package com.example.atsproject;

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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mapact extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Marker marker;
    private static final int My_per_req_loc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapact);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(mapact.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(mapact.this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else {
                ActivityCompat.requestPermissions(mapact.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},My_per_req_loc);
            }
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sms");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);
                String[] val = value.split("\n");
                double latitude = Double.parseDouble(val[0]);
                double longitude = Double.parseDouble(val[1]);

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(latitude,longitude);

                if (marker != null){
                    marker.remove();
                    mMap.clear();
                    marker = null;
                }
                if (marker == null){
                    marker = mMap.addMarker(new MarkerOptions().position(sydney).title("your vehicle!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mapact.this, "failed to get location!!",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public  void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case My_per_req_loc:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(mapact.this, "thank you for permitting!!!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mapact.this, "pls give permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
