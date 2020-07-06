package com.practice.kenny.remotecart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class StoreDetails extends AppCompatActivity implements View.OnClickListener{
    private FusedLocationProviderClient fusedLocationClient;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        Button useCurrentLocation = findViewById(R.id.useBtn);
        Button saveAdress = findViewById(R.id.searchBtn);


        saveAdress.setOnClickListener(this);
        useCurrentLocation.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Add Address");

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        String menuId = menuItem.getTitle().toString();
                        DrawerNav navigator = new DrawerNav();
                        navigator.DrawerNavClick(StoreDetails.this, menuId);
                        return true;
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.useBtn:
                checkLocationPermission();
                break;
            case R.id.searchBtn:
                final EditText streetText = findViewById(R.id.street);
                final EditText cityText = findViewById(R.id.city);
                final EditText stateText = findViewById(R.id.province);
                final EditText postalText = findViewById(R.id.zipCode);

                String street = streetText.getText().toString();
                String city = cityText.getText().toString();
                String state = stateText.getText().toString();
                String zip = postalText.getText().toString();

                if(TextUtils.isEmpty(street)) {
                    streetText.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(city)) {
                    cityText.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(state)) {
                    stateText.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(zip)) {
                    postalText.setError("Cannot be empty");
                    return;
                }
                AdressQuery();
                break;
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("This app needs location access")
                        .setMessage("Grant Access")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(StoreDetails.this,
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
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                // Logic to handle location object
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(StoreDetails.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String address = addresses.get(0).getThoroughfare(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String postalCode = addresses.get(0).getPostalCode();

                                    final EditText streetText = findViewById(R.id.street);
                                    final EditText cityText = findViewById(R.id.city);
                                    final EditText stateText = findViewById(R.id.province);
                                    final EditText postalText = findViewById(R.id.zipCode);

                                    streetText.setText(address);
                                    cityText.setText(city);
                                    stateText.setText(state);
                                    postalText.setText(postalCode);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(StoreDetails.this, "Please enter address fields manually",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void AdressQuery(){
        final EditText streetText = findViewById(R.id.street);
        final EditText cityText = findViewById(R.id.city);
        final EditText stateText = findViewById(R.id.province);
        final EditText postalText = findViewById(R.id.zipCode);
        final EditText aptText = findViewById(R.id.aptNumb);

        String street = streetText.getText().toString();
        String city = cityText.getText().toString();
        String state = stateText.getText().toString();
        String zip = postalText.getText().toString();
        String apt = aptText.getText().toString();
        String userID = SessionState.getLoggedInIDUser(this);
        String lat = "0";
        String longitude = "0";

        if(apt == null) {
            apt = "";
        }

        DBAdapter mDBAdapter = new DBAdapter(StoreDetails.this);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        long result = mDBAdapter.AddAddress(apt, street, state, zip, city, lat, longitude, userID);
        if (result == -1) {
            Toast.makeText(this, "Address not added.", Toast.LENGTH_LONG).show();
        } else {
            if (mDBAdapter.CheckAdress(userID) != null) {
                mDBAdapter.close();
                startActivity(new Intent(this, AdressList.class));
            } else {
                mDBAdapter.close();
                startActivity(new Intent(this, StoreList.class));
            }

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
