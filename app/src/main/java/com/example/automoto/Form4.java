package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Model.RidersBooking;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Form4 extends AppCompatActivity {

    private TextView bname,badd,bcont,bdet,btype,bsype,bdate,btime;
    private ImageView ShopImg, driversLic;
    private String BookShopOwnerID;
    private Button level1, level2;
    private ProgressBar stepsize;
    private ProgressDialog loadingBar;
    private Button backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form4);
        BookShopOwnerID = getIntent().getStringExtra("Fid");

        bname = (TextView) findViewById(R.id.b_name);
        badd = (TextView) findViewById(R.id.b_add);
        bcont = (TextView) findViewById(R.id.b_contact);
        bdet = (TextView) findViewById(R.id.b_complaint);
        btype = (TextView) findViewById(R.id.b_btype);
        bsype = (TextView) findViewById(R.id.b_stype);
        bdate = (TextView) findViewById(R.id.b_date);
        btime = (TextView) findViewById(R.id.b_time);
        loadingBar = new ProgressDialog(this);
        ShopImg = (ImageView) findViewById(R.id.shopimages);

        stepsize = (ProgressBar) findViewById(R.id.progressbarfinal);
        stepsize.setProgress(100);
        stepsize.setMax(100);

        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShopDetails.class).putExtra("Uid", BookShopOwnerID));
                overridePendingTransition(0, 0);
                finish();
            }
        });

        level1 = (Button) findViewById(R.id.nextlevel1);
        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(0, 0);
                finish();
            }
        });

        level2 = (Button) findViewById(R.id.nextlevel2);
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SuccessForm.class));
                overridePendingTransition(2, 2);

            }
        });

        getFormDetails(BookShopOwnerID);

        getshopdetails(BookShopOwnerID);
    }

    private void getshopdetails(String bookShopOwnerID) {

        DatabaseReference shopRefs = FirebaseDatabase.getInstance().getReference().child("ShopOwners");
        shopRefs.child(bookShopOwnerID).addValueEventListener(new ValueEventListener() {
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

    private void getFormDetails(String bookShopOwnerID) {

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Booking");
        shopRef.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(bookShopOwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    RidersBooking ridersBooking = snapshot.getValue(RidersBooking.class);

                    bname.setText(ridersBooking.getRidername());
                    badd.setText(ridersBooking.getRideraddress());
                    bcont.setText(ridersBooking.getRidercontact());
                    bdet.setText(ridersBooking.getConcern());
                    btype.setText(ridersBooking.getBookingtype());
                    bsype.setText(ridersBooking.getServicetype());
                    bdate.setText(ridersBooking.getDate());
                    btime.setText(ridersBooking.getTime());

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