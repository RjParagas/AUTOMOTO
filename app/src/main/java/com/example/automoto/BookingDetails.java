package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automoto.Model.RidersBooking;
import com.example.automoto.Model.ShopOwners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookingDetails extends AppCompatActivity {


    private TextView name,contact,location,btype,stype,stats,dates,shopname;
    private ImageView shopimage;
    private ImageButton backss;
    private String BID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        BID = getIntent().getStringExtra("Bid");

        name = (TextView) findViewById(R.id.rname);
        contact = (TextView) findViewById(R.id.rcont);
        location = (TextView) findViewById(R.id.curloc);
        btype = (TextView) findViewById(R.id.bookingtype);
        stype = (TextView) findViewById(R.id.servicetype);
        stats = (TextView) findViewById(R.id.bstatus);
        dates = (TextView) findViewById(R.id.bdate);
        shopname = (TextView) findViewById(R.id.shopnames);

        shopimage = (ImageView) findViewById(R.id.shopimg);

        backss = (ImageButton) findViewById(R.id.backbtns);
        backss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingDetails.this, MyBookings.class);
                startActivity(intent);
                overridePendingTransition(1, 1);
                finish();
            }
        });

        getDetails();

    }

    private void getDetails() {
        FirebaseDatabase.getInstance().getReference().child("Booking").child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(BID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RidersBooking ridersBooking = snapshot.getValue(RidersBooking.class);

                        if (snapshot.exists()){

                            name.setText(ridersBooking.getRidername());
                            contact.setText(ridersBooking.getRidercontact());
                            location.setText(ridersBooking.getRideraddress());
                            btype.setText(ridersBooking.getBookingtype());
                            stype.setText(ridersBooking.getServicetype());
                            stats.setText(ridersBooking.getStatus());
                            dates.setText(ridersBooking.getDate());

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        getShopDetails();
    }

    private void getShopDetails() {

        FirebaseDatabase.getInstance().getReference().child("ShopOwners").child(BID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShopOwners shopOwners = snapshot.getValue(ShopOwners.class);

                if (snapshot.exists()){
                    shopname.setText(shopOwners.getShopname());
                    Picasso.get().load(shopOwners.getImage()).into(shopimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}