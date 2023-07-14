package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Form1 extends AppCompatActivity implements OnMapReadyCallback {

    private String Bookingtype,Servicetype, Rname, Radd, RDet, Rcon, saveCurrentDate, saveCurrentTime;
    private String BookRandomKey, BookShopOwnerID;
    private TextInputLayout rName, rAdd, rCon;
    private EditText rDet ;
    private ImageView ShopImg;
    private ProgressBar stepsize;
    private ProgressDialog loadingBar;
    private Button backbtn;
    private Button prev, next;
    private DatabaseReference BookingRef, servicesref;
    Spinner spinner;


    //for getting the latlong//
    FusedLocationProviderClient mFusedLocationClient;
    Location locations;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;

    Geocoder geocoder;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);


        BookShopOwnerID = getIntent().getStringExtra("Sid");

        ShopImg = (ImageView) findViewById(R.id.shopimages);
        rName = (TextInputLayout) findViewById(R.id.booking_name);
        rAdd = (TextInputLayout) findViewById(R.id.booking_address);
        rDet = (EditText) findViewById(R.id.booking_details);
        rCon = (TextInputLayout) findViewById(R.id.booking_contact);
        backbtn = (Button) findViewById(R.id.back_btn);
        next = (Button) findViewById(R.id.nextlevel2);
        prev = (Button) findViewById(R.id.nextlevel1);
        loadingBar = new ProgressDialog(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);

        // spinner 1
        spinner = (Spinner) findViewById(R.id.service_type);
        getSpinnerService();


        stepsize = (ProgressBar) findViewById(R.id.progressbarform1);
        stepsize.setProgress(60);
        stepsize.setMax(100);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShopDetails.class).putExtra("Uid", BookShopOwnerID));
                overridePendingTransition(0, 0);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BookForm.class).putExtra("Sid",BookShopOwnerID) );
                overridePendingTransition(0, 0);
                finish();

            }
        });

        getshopDetails(BookShopOwnerID);
        getLastLocation();
    }

    private void getSpinnerService() {


        servicesref = FirebaseDatabase.getInstance().getReference("Services").child("TheRiders").child(BookShopOwnerID);
        servicesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: snapshot.getChildren()) {
                    String areaName = areaSnapshot.child("Service").getValue(String.class);
                    areas.add(areaName);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Form1.this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, areas);
                areasAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getshopDetails(String bookShopOwnerID) {
        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("ShopOwners");
        shopRef.child(bookShopOwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopOwners shopOwners = snapshot.getValue(ShopOwners.class);

                    Picasso.get().load(shopOwners.getImage()).into(ShopImg);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ValidateProductData()
    {

        Bookingtype = "onShop";
        Servicetype = spinner.getSelectedItem().toString();
        Rname = rName.getEditText().getText().toString();
        Radd = rAdd.getEditText().getText().toString();
        RDet = rDet.getText().toString();
        Rcon = rCon.getEditText().getText().toString();






        if (spinner.getSelectedItem().toString().trim().equals("Choose Service Type"))
        {
            Toast.makeText(this, "Please Select Service Type", Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
            return;

        }
        else if (Rname.isEmpty())
        {
            rName.setError("Fullname is required");
            rName.requestFocus();
            return;
        }
        else if (Radd.isEmpty())
        {
            rAdd.setError("Address is required");
            rAdd.requestFocus();

            return;
        }

        else if (Rcon.isEmpty())
        {
            rCon.setError("Contact is required");
            rCon.requestFocus();
            return;
        }
        else if (RDet.isEmpty())
        {
            rDet.setError("Contact is required");
            rDet.requestFocus();
            return;
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Booking Form");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        BookRandomKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SaveProductInfoToDatabase();
    }

    private void SaveProductInfoToDatabase()
    {
        String Status = "Not Accepted";
        final HashMap<String, Object> BookingMap = new HashMap<>();
        BookingMap.put("Sid", BookShopOwnerID);
        BookingMap.put("rid", BookRandomKey);
        BookingMap.put("date", saveCurrentDate);
        BookingMap.put("time", saveCurrentTime);
        BookingMap.put("concern", RDet);
        BookingMap.put("Status", Status);
        BookingMap.put("bookingtype", Bookingtype);
        BookingMap.put("servicetype", Servicetype);
        BookingMap.put("ridername", Rname);
        BookingMap.put("rideraddress", Radd);
        BookingMap.put("ridercontact", Rcon);
        BookingMap.put("latitude", latitudeTextView.getText().toString());
        BookingMap.put("longitude", longitTextView.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Booking")
                .child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(BookShopOwnerID).updateChildren(BookingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseDatabase.getInstance().getReference("Booking")
                                    .child("Owners").child(BookShopOwnerID).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .updateChildren(BookingMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(getApplicationContext(), Form3.class).putExtra("Fid", BookShopOwnerID));
                                                overridePendingTransition(0, 0);
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                loadingBar.dismiss();
                                                String message = task.getException().toString();
                                                Toast.makeText(Form1.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

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
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
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
    }

    public void onMapReady(GoogleMap googleMap) {

        GoogleMap mMap = googleMap;

        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);
        mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);
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
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
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