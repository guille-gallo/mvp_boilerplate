/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.antonioleiva.mvpexample.app.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antonioleiva.mvpexample.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.antonioleiva.mvpexample.app.R.id.map;

public class MainActivity extends AppCompatActivity implements MainView,
        AdapterView.OnItemClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        OnMapReadyCallback {

    private ListView listView;
    private ProgressBar progressBar;
    private MainPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    private Location mLocation;
    private LocationManager mLocationManager;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private BroadcastReceiver yourReceiver;
    private static final String ACTION_GPS = "android.location.PROVIDERS_CHANGED";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);




        progressBar = (ProgressBar) findViewById(R.id.progress);

        presenter = new MainPresenterImpl(this, new FindItemsInteractorImpl());

        /*mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void buildAlertMessageNoGps() {
        Context context = getApplicationContext();
        CharSequence text = "Por favor habilita el GPS para poder ubicarte";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        googleMap = map;

        createMarkers();
    }

    @Override
    public void onConnected(Bundle bundle) { }

    @Override
    public void onLocationChanged(Location location) {
        /*
        * -Ver ventana para cuadras a elegir, ver cálculo distancia.
        * -Sacar login activity,
        *
        * */

        // Personal location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_position)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    public void createMarkers() {
        //"Gomería Los Gringos"
        LatLng latLng2 = new LatLng(-32.969110, -60.642597);
        googleMap.addMarker(new MarkerOptions().position(latLng2).title("Gomería Los Gringos"));

        //"Neumáticos y Servicios Amante"
        LatLng latLng3 = new LatLng(-32.968575, -60.629303);
        googleMap.addMarker(new MarkerOptions().position(latLng3).title("Neumáticos y Servicios Amante"));

        //"Gomeria Santa Fe 2378"
        LatLng latLng4 = new LatLng(-32.941864, -60.655157);
        googleMap.addMarker(new MarkerOptions().position(latLng4).title("Gomeria Santa Fe 2378"));

        //"Gomería Rubén Cabral S.R.L."
        LatLng latLng5 = new LatLng(-32.951314, -60.672555);
        googleMap.addMarker(new MarkerOptions().position(latLng5).title("Gomería Rubén Cabral S.R.L."));

        //"Gomería Occidente"
        LatLng latLng6 = new LatLng(-32.937949, -60.653278);
        googleMap.addMarker(new MarkerOptions().position(latLng6).title("Gomería Occidente"));
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG, "Connection Suspended");
        System.out.println("Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        System.out.println("Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        // unregister the receiver
        if (yourReceiver != null) {
            unregisterReceiver(yourReceiver);
            yourReceiver = null;
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override protected void onResume() {
        super.onResume();
        presenter.onResume();

        checkGPS();
        registerReceiverGPS();
    }

    private void checkGPS() {
        /*
        * check if GPS location enabled
        * */
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }

    private void registerReceiverGPS() {
        if (yourReceiver == null) {
            // INTENT FILTER FOR GPS MONITORING
            final IntentFilter theFilter = new IntentFilter();
            theFilter.addAction(ACTION_GPS);
            yourReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String s = intent.getAction();
                        if (s != null) {
                            if (s.equals(ACTION_GPS)) {
                                checkGPS();
                            }
                        }
                    }
                }
            };
            registerReceiver(yourReceiver, theFilter);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        /*progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);*/
    }

    @Override public void hideProgress() {
        /*progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);*/
    }

    @Override public void setItems(List<String> items) {
        //listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }
}