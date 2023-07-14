package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.automoto.Model.CartList;
import com.example.automoto.Model.OrderList;
import com.example.automoto.Model.RidersBooking;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.BookingViewHolder;
import com.example.automoto.Viewholder.MyOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyShipped extends AppCompatActivity {

    private DatabaseReference itemshippedRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shipped);


        itemshippedRef = FirebaseDatabase.getInstance().getReference().child("Item Orders");

        recyclerView = findViewById(R.id.myshippedlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backBtn = (ImageButton) findViewById(R.id.backbtss);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(1, 1);
                finish();
            }
        });

    }

    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();


        itemshippedRef = FirebaseDatabase.getInstance().getReference().child("Item Orders");
        FirebaseRecyclerOptions<CartList> options =
                new FirebaseRecyclerOptions.Builder<CartList>()
                        .setQuery(itemshippedRef.child("CustomerRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), CartList.class)
                        .build();

        FirebaseRecyclerAdapter<CartList, MyOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<CartList, MyOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position, @NonNull CartList model) {

                        holder.ItemName.setText(model.getItemName());
                        holder.ItemTotal.setText(model.getTotalPrice());
                        holder.ItemQty.setText(model.getQty());
                        holder.txtStatus.setText(model.getStatus());
                        Picasso.get().load(model.getItemImage()).into(holder.ItemImage);

                    }

                    @NonNull
                    @Override
                    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shipped_layout, parent, false );
                        MyOrderViewHolder holder = new MyOrderViewHolder(view);
                        return  holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


}