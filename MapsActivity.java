package com.example.receivesms;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Marker marker1, marker2;
    private LocationManager locationManager;
    Double mylat = 0.0, mylong = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,MapsActivity.this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        assert location != null;
        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mylat = location.getLatitude();
        mylong = location.getLongitude();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("location");
        databaseReference.setValue(mylat+"\n"+mylong);
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
                LatLng locate1 = new LatLng(latitude,longitude);

                if (marker1 != null){
                   marker1.remove();
                   mMap.clear();
                   marker1 = null;
               }
                if (marker1 == null){
                    marker1 = mMap.addMarker(new MarkerOptions().position(locate1).title("your vehicle!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locate1));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MapsActivity.this, "failed to get location!!",Toast.LENGTH_LONG).show();
            }
        });

        databaseReference = firebaseDatabase.getReference("location");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value2 = dataSnapshot.getValue(String.class);
                String[] val2 = value2.split("\n");
                double latitude = Double.parseDouble(val2[0]);
                double longitude = Double.parseDouble(val2[1]);

                // Add a marker in Sydney and move the camera
                LatLng locate2 = new LatLng(latitude,longitude);

                if (marker2 != null){
                    marker2.remove();
                    mMap.clear();
                    marker2 = null;
                }
                if (marker2 == null){
                    marker2 = mMap.addMarker(new MarkerOptions().position(locate2).title("your vehicle!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locate2));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MapsActivity.this, "failed to get current location!!",Toast.LENGTH_LONG).show();
            }
        });

    }
}
