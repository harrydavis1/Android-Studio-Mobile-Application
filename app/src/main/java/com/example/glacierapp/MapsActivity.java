package com.example.glacierapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String currentDateTime = dateFormat.format(new Date()); // Find todays date


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
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Happy");
            emotiontext.setTextColor(Color.parseColor("#FDD835"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Happy").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.happy)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Angry");
            emotiontext.setTextColor(Color.parseColor("#E53935"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Angry").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.angry)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }

        if (item.getItemId() == R.id.item4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Embarrassed");
            emotiontext.setTextColor(Color.parseColor("#5E35B1"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Embarrassed").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.embarrased)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Excited");
            emotiontext.setTextColor(Color.parseColor("#43A047"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Excited").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.excited)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Sad");
            emotiontext.setTextColor(Color.parseColor("#3949AB"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Sad").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.sad)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item7) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Shy");
            emotiontext.setTextColor(Color.parseColor("#D81B60"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Shy").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.shy)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Surprised");
            emotiontext.setTextColor(Color.parseColor("#FF03DAC5"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Surprised").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.suprised)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        if (item.getItemId() == R.id.item9) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            View mView = inflater.inflate(R.layout.markerdesc, null);
            final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);
            final EditText desctext = (EditText)mView.findViewById(R.id.descText);
            emotiontext.setText("Worried");
            emotiontext.setTextColor(Color.parseColor("#E53935"));
            builder.setView(mView).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String descmessage = (desctext.getText().toString() + " - " + currentDateTime);
                    Marker marker = mGoogleMap.addMarker(
                            new MarkerOptions().position(latLng).title("Worried").snippet(descmessage).icon(BitmapDescriptorFactory.fromResource(R.drawable.worried)).draggable(true));
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MapsActivity.this);
        LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.markerdesc, null);
        final EditText emotiontext = (EditText)mView.findViewById(R.id.emotiontext);

        emotiontext.setText(marker.getTitle());
        final EditText desctext = (EditText)mView.findViewById(R.id.descText);

        builder.setView(mView).setPositiveButton("Apply Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String descString = desctext.getText().toString();
                marker.setSnippet("");
                marker.setSnippet(descString + " : " + currentDateTime);
            }
        })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    }
                });
        builder.show();
    }

    public void searchLocation(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.locationSearch);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
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

