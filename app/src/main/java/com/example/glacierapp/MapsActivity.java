package com.example.glacierapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, PopupMenu.OnMenuItemClickListener, GoogleMap.OnInfoWindowClickListener {

    DrawerLayout drawerLayout;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    LatLng latLng;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        drawerLayout = findViewById(R.id.drawer_layout);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.emotion_menu);
        popup.show();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                        Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {	            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnInfoWindowClickListener(this);
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size()-1);
                mLastLocation = location;

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                //move map camera
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }

    };
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item2) {
            Toast.makeText(this, "Happy", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Happy").icon(BitmapDescriptorFactory.fromResource(R.drawable.happy)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item3) {
            Toast.makeText(this, "Angry", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Angry").icon(BitmapDescriptorFactory.fromResource(R.drawable.angry)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item4) {
            Toast.makeText(this, "Embarrassed", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Embarrassed").icon(BitmapDescriptorFactory.fromResource(R.drawable.embarrased)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item5) {
            Toast.makeText(this, "Excited", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Excited" ).icon(BitmapDescriptorFactory.fromResource(R.drawable.excited)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item6) {
            Toast.makeText(this, "Sad", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Sad").icon(BitmapDescriptorFactory.fromResource(R.drawable.sad)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item7) {
            Toast.makeText(this, "Shy", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Shy").icon(BitmapDescriptorFactory.fromResource(R.drawable.shy)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item8) {
            Toast.makeText(this, "Surprised", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Surprised").icon(BitmapDescriptorFactory.fromResource(R.drawable.suprised)).draggable(true));

            return true;
        }
        if (item.getItemId() == R.id.item9) {
            Toast.makeText(this, "Worried", Toast.LENGTH_SHORT).show();
            Marker marker = mGoogleMap.addMarker(
                    new MarkerOptions().position(latLng).title("Worried").icon(BitmapDescriptorFactory.fromResource(R.drawable.worried)).draggable(true));

            return true;
        }
        return false;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }
            } else {
                // if not allow a permission, the application will exit
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        }
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickMyAccount(View view){
        redirectActivity(this, MyAccount.class);
    }

    public void ClickContact(View view){
        redirectActivity(this, contact.class);
    }

    public void ClickSupport(View view){
        redirectActivity(this, Support.class);
    }

    public void ClickLogOut(View view){
        logout(this);
    }

    public static void logout(Activity activity){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                redirectActivity(activity, Login.class);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    protected void Pause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

}

