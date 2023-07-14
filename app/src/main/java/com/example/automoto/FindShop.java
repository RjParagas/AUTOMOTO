package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Adapter.NearbyAdapter;
import com.example.automoto.Adapter.NearbyShopAdapter;
import com.example.automoto.Model.NearbyList;
import com.example.automoto.Model.NearbyShop;
import com.example.automoto.Model.UserProfile;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindShop extends AppCompatActivity implements OnMapReadyCallback {
    //for getting the latlong//
    FusedLocationProviderClient mFusedLocationClient;
    Location locations;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;

    Geocoder geocoder;

    private RecyclerView recyclerView ;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView rV;
    private RecyclerView.LayoutManager LM;

    NearbyAdapter nearbyAdapter;
    ArrayList<NearbyShop> nearbylist;

    NearbyShopAdapter nearbyShopAdapter;
    ArrayList<NearbyList> nshoplist;

    private ImageButton backBtn;
    private CircleImageView currUser;
    private DatabaseReference ProfRef, Shopref, nearbyref;
    private TextView homeuser;
    private Button all, nearby;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_shop);





        ProfRef = FirebaseDatabase.getInstance().getReference("Riders");

        homeuser = (TextView) findViewById(R.id.home_username);

        // all shop list
        recyclerView = findViewById(R.id.allshoplist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nearbylist = new ArrayList<>();
        nearbyAdapter = new NearbyAdapter(this, nearbylist);
        recyclerView.setAdapter(nearbyAdapter);

        backBtn = (ImageButton) findViewById(R.id.back_btnea);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(0, 0);
                finish();
            }
        });

        Shopref = FirebaseDatabase.getInstance().getReference("ShopOwners");
        Shopref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    NearbyShop nearbyShop = dataSnapshot.getValue(NearbyShop.class);
                    nearbylist.add(nearbyShop);

                }
                nearbyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        }); // end of all list



        // nearby list
        rV = findViewById(R.id.nearbylist);
        rV.setHasFixedSize(true);
        LM = new LinearLayoutManager(this);
        rV.setLayoutManager(LM);

        nshoplist = new ArrayList<>();
        nearbyShopAdapter = new NearbyShopAdapter(this, nshoplist);
        rV.setAdapter(nearbyShopAdapter);

        nearbyref = FirebaseDatabase.getInstance().getReference("ShopDistance");

        nearbyref.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("Distance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            NearbyList nearbyListed = dataSnapshot.getValue(NearbyList.class);
                            nshoplist.add(nearbyListed);

                        }
                        nearbyShopAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });

        //button change

        nearby = (Button) findViewById(R.id.findnearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View alllist = findViewById(R.id.allshoplist);
                alllist.setVisibility(View.GONE);

                View nearbies = findViewById(R.id.nearbylist);
                nearbies.setVisibility(View.VISIBLE);

                View btnall = findViewById(R.id.findall);
                btnall.setVisibility(View.VISIBLE);

                View btnnearby = findViewById(R.id.findnearby);
                btnnearby.setVisibility(View.GONE);



            }
        });

        all = (Button) findViewById(R.id.findall);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View alllist = findViewById(R.id.allshoplist);
                alllist.setVisibility(View.VISIBLE);

                View nearbies = findViewById(R.id.nearbylist);
                nearbies.setVisibility(View.GONE);

                View btnnearby = findViewById(R.id.findnearby);
                btnnearby.setVisibility(View.VISIBLE);

                View btnall = findViewById(R.id.findall);
                btnall.setVisibility(View.GONE);

            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        currUser = (CircleImageView) findViewById(R.id.profile_home);
        currUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindShop.this, Profile.class);
                startActivity(intent);
            }
        });

        getLastLocation();

        getUserDetails();

    }


    private void getUserDetails() {

        ProfRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if (userProfile != null) {
                    String profImg = userProfile.image;
                    homeuser.setText(userProfile.username);
                    Picasso.get().load(profImg).into(currUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locations = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(FindShop.this);
                }

            }
        });
    }


    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(locations.getLatitude(), locations.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        }
        googleMap.setMyLocationEnabled(true);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef
                .child("ShopOwners");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    //Toast.makeText(L_Location_Activity.this,"for",Toast.LENGTH_LONG).show();

                    String latitude_Display = ds
                            .child("latitude")
                            .getValue().toString();

                    String longitude_Display = ds
                            .child("longitude")
                            .getValue().toString();


                    String Shopnames = ds
                            .child("Shopname")
                            .getValue().toString();

                    String Shopadd = ds
                            .child("Address")
                            .getValue().toString();




                    String latLng = latitude_Display;
                    String latLng1 = longitude_Display;
                    String Storename = Shopnames;
                    String StoreAdd = Shopadd;



                    double latitude = Double.parseDouble(latLng);
                    double longitude = Double.parseDouble(latLng1);


                    // map.clear();
                    LatLng currentLocation = new LatLng( latitude, longitude );
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position( currentLocation );
                    //markerOptions.title("i'm here");
                    //map.addMarker( markerOptions );
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(Storename)
                            .snippet(StoreAdd));

                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Nullable
                        @Override
                        public View getInfoContents(@NonNull Marker marker) {

                            LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.shop_map_dialog_info, null);

                            TextView shopname = (TextView) view.findViewById(R.id.d_shopname);
                            shopname.setText(marker.getTitle());

                            TextView addressS = (TextView) view.findViewById(R.id.d_addrshop);
                            addressS.setText(marker.getSnippet());
                            return view;
                        }

                        @Nullable
                        @Override
                        public View getInfoWindow(@NonNull Marker marker) {
                            return null;
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersRef.addListenerForSingleValueEvent(eventListener);

    }
    //Getting current location

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            mLastLocation.getLatitude();
            mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }




    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}