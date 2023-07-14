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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.automoto.Model.RidersBooking;

import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.BookingViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class MyBookings extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myRef;
    private ImageButton backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);




        recyclerView = findViewById(R.id.mybookinglist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backBtn = (ImageButton) findViewById(R.id.backbtns);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyBookings.this, Booking.class);
                startActivity(intent);
                overridePendingTransition(1, 1);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bookingNav);

        bottomNavigationView.setSelectedItemId(R.id.req);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.accept:
                        startActivity(new Intent(getApplicationContext(), AcceptedBooking.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;

            }

        });

    }

    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();




        myRef = FirebaseDatabase.getInstance().getReference("Booking");
        FirebaseRecyclerOptions<RidersBooking> options =
                new FirebaseRecyclerOptions.Builder<RidersBooking>()
                .setQuery(myRef.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()),RidersBooking.class)
                        .build();

        FirebaseRecyclerAdapter<RidersBooking, BookingViewHolder> adapter =
                new FirebaseRecyclerAdapter<RidersBooking, BookingViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull RidersBooking model) {

                        holder.Servicetype.setText(model.getBookingtype());
                        holder.bookDate.setText(model.getDate());
                        holder.bookStatus.setText(model.getStatus());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyBookings.this, BookingDetails.class);
                                intent.putExtra("Bid", model.getSid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_booking_layout, parent, false );
                        BookingViewHolder holder = new BookingViewHolder(view);
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