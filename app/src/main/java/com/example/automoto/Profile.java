package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Model.User;
import com.example.automoto.Model.UserProfile;
import com.example.automoto.Utility.NetworkChangeListener;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button Edit, backBTN,btnver;
    private ImageButton settings;
    private TextView tbook, tcart,torder,fullver,pendver;
    private CardView justver;
    private int totalrv = 0;
    private Button test;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        tbook = (TextView) findViewById(R.id.total_booking);
        tcart = (TextView) findViewById(R.id.total_cart);
        torder = (TextView) findViewById(R.id.total_order);
        pendver = (TextView) findViewById(R.id.pend);
        fullver = (TextView) findViewById(R.id.verif);
        btnver = (Button) findViewById(R.id.btnverify);
        justver = (CardView) findViewById(R.id.cardshit);
        backBTN = (Button) findViewById(R.id.p_back);
        Edit = (Button) findViewById(R.id.p_edit);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().getUid();
                startActivity(new Intent(Profile.this, EditProfile.class));
            }
        });

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Drivers_license.class));
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Profile.this, Main_Home.class));
                overridePendingTransition(0, 0);
            }
        });

        settings = (ImageButton) findViewById(R.id.p_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Settings.class);
                startActivity(intent);
            }
        });



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Riders");
        userID = user.getUid();

        final ImageView profileImageView = (ImageView) findViewById(R.id.profile_img);
        final TextView usernameTextView = (TextView) findViewById(R.id.p_username);
        final TextView fullnameTextView = (TextView) findViewById(R.id.p_fullname);
        final TextView phoneTextView = (TextView) findViewById(R.id.p_phone);
        final TextView emailTextView = (TextView) findViewById(R.id.p_email);
        final TextView addressTextView = (TextView) findViewById(R.id.p_home);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if(userProfile !=null){
                    String profImg = userProfile.image;
                    String fullName =userProfile.name;
                    String email = userProfile.email;
                    String Phone = userProfile.phoneNo;
                    String username = userProfile.username;
                    String Address = userProfile.address;


                    fullnameTextView.setText(fullName);
                    emailTextView.setText(email);
                    phoneTextView.setText(Phone);
                    addressTextView.setText(Address);
                    usernameTextView.setText(username);
                    Picasso.get().load(profImg).into(profileImageView);
                }

                if (userProfile.Status.equals("Verified")){

                    justver.setVisibility(View.GONE);
                    fullver.setVisibility(View.VISIBLE);
                }
                else if (userProfile.Status.equals("Not Verified")){
                    justver.setVisibility(View.VISIBLE);
                    fullver.setVisibility(View.GONE);
                }
                else if (userProfile.Status.equals("Pending")){
                    justver.setVisibility(View.GONE);
                    fullver.setVisibility(View.GONE);
                    pendver.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Profile.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });

        getTotal();

    }

    private void getTotal() {

        FirebaseDatabase.getInstance().getReference("Customer Cart").child("TheRiders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalrv = (int) snapshot.getChildrenCount();
                    tcart.setText(Integer.toString(totalrv));


                }
                else {
                    tcart.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Item Orders").child("CustomerRiders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalrv = (int) snapshot.getChildrenCount();
                    torder.setText(Integer.toString(totalrv));


                }
                else {
                    tcart.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Booking Accepted").child("TheRiders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalrv = (int) snapshot.getChildrenCount();
                    tbook.setText(Integer.toString(totalrv));
                }
                else {
                    tbook.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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