package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.automoto.Adapter.ItemAdapter;
import com.example.automoto.Adapter.ShopAdapter;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.ProductSearch;
import com.example.automoto.Model.ShopFeedback;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Model.ShopSearch;
import com.example.automoto.Model.Users;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.ShopViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Booking extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference ShopRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView ic_book;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ShopAdapter shopAdapter;
    ArrayList<ShopSearch> shoplist;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ShopRef = FirebaseDatabase.getInstance().getReference().child("ShopOwners");

        recyclerView = findViewById(R.id.companyList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        shoplist = new ArrayList<>();
        shopAdapter = new ShopAdapter(this, shoplist);
        recyclerView.setAdapter(shopAdapter);

        ic_book = (ImageView) findViewById(R.id.icon_book);
        ic_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booking.this, MyBookings.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), Shop.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), Main_Home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }
                return false;
            }
        });

        ShopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ShopSearch shopSearch = dataSnapshot.getValue(ShopSearch.class);
                    shoplist.add(shopSearch);
                }
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView = findViewById(R.id.search_shop);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search(newText);
                return false;
            }
        });


    }

    private void search(String newText) {

        ArrayList<ShopSearch> shoplisted = new ArrayList<>();

        for (ShopSearch object : shoplist){

            if (object.getShopname().toLowerCase().contains(newText.toLowerCase())){
                shoplisted.add(object);
            }

        }

        for (ShopSearch object : shoplist){

            if (object.getAddress().toLowerCase().contains(newText.toLowerCase())){
                shoplisted.add(object);
            }

        }


        shopAdapter = new ShopAdapter(this, shoplisted);
        recyclerView.setAdapter(shopAdapter);
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