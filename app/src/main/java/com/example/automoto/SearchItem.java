package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.automoto.Adapter.ItemAdapter;
import com.example.automoto.Model.ProductSearch;
import com.example.automoto.Model.Products;
import com.example.automoto.Utility.NetworkChangeListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchItem extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ItemAdapter itemAdapter;
    ArrayList<ProductSearch> itemlist;
    SearchView searchView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        recyclerView = findViewById(R.id.searchlist);
        databaseReference = FirebaseDatabase.getInstance().getReference("Product").child("RidersView");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        itemlist = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemlist);
        recyclerView.setAdapter(itemAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ProductSearch products = dataSnapshot.getValue(ProductSearch.class);
                    itemlist.add(products);
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView = findViewById(R.id.search_item);
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

        ArrayList<ProductSearch> itemlisted = new ArrayList<>();

        for (ProductSearch object : itemlist){

            if (object.getProductname().toLowerCase().contains(newText.toLowerCase())){
                itemlisted.add(object);
            }
        }


        itemAdapter = new ItemAdapter(this, itemlisted);
        recyclerView.setAdapter(itemAdapter);

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
