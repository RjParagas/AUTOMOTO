package com.example.automoto.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.BookinForm;
import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.Main_Home;
import com.example.automoto.Model.NearbyShop;
import com.example.automoto.R;
import com.example.automoto.RouteShop;
import com.example.automoto.ShopDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {



    Context context;
    LocationManager locationManager;
    ArrayList<NearbyShop> nearby;
    String Distance;


    public NearbyAdapter(Context context, ArrayList<NearbyShop> list) {
        this.context = context;
        this.nearby = list;
    }

    @NonNull
    @Override
    public NearbyAdapter.NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_nearby_layout, parent, false);
        return new NearbyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull NearbyAdapter.NearbyViewHolder holder, int position) {

        NearbyShop nearbyShop = nearby.get(position);
        holder.nShop.setText(nearbyShop.getShopname());
        holder.nAddress.setText(nearbyShop.getAddress());
        Picasso.get().load(nearbyShop.getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetails.class);
                intent.putExtra("Uid", nearbyShop.getPid());
                context.startActivity(intent);
            }
        });

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Location currentLoc = new Location("locationA");
        currentLoc.setLatitude(latitude);
        currentLoc.setLongitude(longitude);

        double latitudes = Double.parseDouble(nearbyShop.getLatitude());
        double longitudes = Double.parseDouble(nearbyShop.getLongitude());

        Location destination = new Location("locationB");
        destination.setLatitude(latitudes);
        destination.setLongitude(longitudes);
        float distanceKm = currentLoc.distanceTo(destination);
        holder.distance.setText(String.format("%.2f", distanceKm / 1000));


        final HashMap<String, Object> NearbyMap = new HashMap<>();
        NearbyMap.put("Sid", nearbyShop.getPid());
        NearbyMap.put("rid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        NearbyMap.put("Distance", holder.distance.getText().toString());
        NearbyMap.put("Shopname", holder.nShop.getText().toString());
        NearbyMap.put("address", holder.nAddress.getText().toString());
        NearbyMap.put("latitude", nearbyShop.getLatitude());
        NearbyMap.put("longitude", nearbyShop.getLongitude());
        NearbyMap.put("image", nearbyShop.getImage());

        FirebaseDatabase.getInstance().getReference("ShopDistance").child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(nearbyShop.getPid()).updateChildren(NearbyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                }
            }
        });

        holder.directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Fuck = nearbyShop.getLatitude();
                String You = nearbyShop.getLongitude();
                DisplayTrack(Fuck, You);

                }
        });




    }

    private void DisplayTrack(String Fuck, String You) {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longituder = locations.getLongitude();
        double latituder = locations.getLatitude();




        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + "/" + Fuck + "," + You + "/" + locations );
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {

        return nearby.size();
    }




    public static class NearbyViewHolder extends RecyclerView.ViewHolder {

        public TextView nShop,nAddress,distance,distances;
        public ImageButton directions;
        public ItemClickListner listner;
        public ImageView imageView;


        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.nearby_img);
            directions = (ImageButton) itemView.findViewById(R.id.nearby_navigate);
            nShop = (TextView) itemView.findViewById(R.id.nearby_shopName);
            nAddress = (TextView) itemView.findViewById(R.id.nearby_address);
            distance = (TextView) itemView.findViewById(R.id.nearby_distance);


        }
    }


}
