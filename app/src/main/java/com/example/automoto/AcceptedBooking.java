package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.automoto.Model.Accepted;
import com.example.automoto.Model.RidersBooking;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Model.Users;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.AcceptedViewHolder;
import com.example.automoto.Viewholder.BookingViewHolder;
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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AcceptedBooking extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myAccepted;
    private ImageButton backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_booking);


        recyclerView = findViewById(R.id.myacceptedlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backBtn = (ImageButton) findViewById(R.id.backbtns);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedBooking.this, Booking.class);
                startActivity(intent);
                overridePendingTransition(1, 1);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bookingNav);

        bottomNavigationView.setSelectedItemId(R.id.accept);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.req:
                        startActivity(new Intent(getApplicationContext(), MyBookings.class));
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

        myAccepted = FirebaseDatabase.getInstance().getReference("Booking Accepted");
        FirebaseRecyclerOptions<Accepted> options =
                new FirebaseRecyclerOptions.Builder<Accepted>()
                        .setQuery(myAccepted.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()),Accepted.class)
                        .build();

        FirebaseRecyclerAdapter<Accepted, AcceptedViewHolder> adapter =
                new FirebaseRecyclerAdapter<Accepted, AcceptedViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position, @NonNull Accepted model) {

                        holder.Servicetype.setText(model.getService());
                        holder.bookDate.setText(model.getDate());
                        holder.bookStatus.setText(model.getStatus());

                        holder.chat_wshopped.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AcceptedBooking.this, Chat.class);
                                intent.putExtra("Aid", model.getSid());

                                FirebaseDatabase.getInstance().getReference("ShopOwners").child(model.getSid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    ShopOwners shopOwners = snapshot.getValue(ShopOwners.class);
                                                    intent.putExtra("ShopName", shopOwners.getShopname());
                                                    intent.putExtra("ShopImage", shopOwners.getImage());
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                FirebaseDatabase.getInstance().getReference("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Users users = snapshot.getValue(Users.class);
                                                    intent.putExtra("RiderName", users.getName());
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_layout, parent, false );
                        AcceptedViewHolder holder = new AcceptedViewHolder(view);
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