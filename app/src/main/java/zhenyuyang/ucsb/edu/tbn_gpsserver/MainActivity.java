package zhenyuyang.ucsb.edu.tbn_gpsserver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {


    private Button button_clear;
    private GPSServer server;
    public TextView infoip, msg,info_location;
    public Location cuurenLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        info_location = (TextView) findViewById(R.id.info_location);
        server = new GPSServer(this);
        infoip.setText(server.getIpAddress()+":"+server.getPort());
        button_clear = (Button)findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setText("");
                server.reset();
            }
        });




        //handle the google Maps
        myMapFragment mapFragment = ((myMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map));
        mapFragment.setListener(new myMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {

            }
        });
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    //enable myLocationButton
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        //map.getUiSettings().setMyLocationButtonEnabled(true);
                    } else {
                        Log.i("manu", "Error - checkSelfPermission!!");
                    }

//                    //add markers
//                    if (passedInTown.getCategory() != null && !passedInTown.getCategory().isEmpty()) {
//                        tmi = new TownMapIcon(getApplicationContext(), passedInTown.getCategory(), false);
//                        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
//                                .title(passedInTown.getTitle())
//                                .snippet(passedInTown.getCategory())
//                                .icon(BitmapDescriptorFactory.fromBitmap(tmi.getIconBitmap())));
//                    }
//
//                    //camera animation
//                    if (map != null) {
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));  //add animation
//                    }
                }
            });

//            mapFragment.setListener(new myMapFragment.OnTouchListener() {
//                @Override
//                public void onTouch() {
//                    ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollView_detail);
//                    mScrollView.requestDisallowInterceptTouchEvent(true);
//                }
//            });
        } else {
            Log.i("manu", "Error - Map Fragment was null!!");
        }

// Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //setup LocationListener
        LocationListener locationListener = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                info_location.setText("my location = "+location.toString());
                cuurenLocation = location;
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
        };
        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        final int REQ_PERMISSION = 999;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(getApplicationContext(), "Need permissions of location.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_PERMISSION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
