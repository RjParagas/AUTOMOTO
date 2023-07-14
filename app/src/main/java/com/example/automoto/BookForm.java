package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookForm extends AppCompatActivity {

    private ProgressBar stepsize;
    private RadioButton radioButton1, radioButton2;
    private ImageView ShopImg;
    private String BookShopOwnerID;
    private Button backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        BookShopOwnerID = getIntent().getStringExtra("Sid");
        ShopImg = (ImageView) findViewById(R.id.shopimages);

        stepsize = (ProgressBar) findViewById(R.id.progressbar);
        stepsize.setProgress(20);
        stepsize.setMax(100);

        radioButton1 = (RadioButton) findViewById(R.id.onShop);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Form1.class).putExtra("Sid", BookShopOwnerID));
                overridePendingTransition(0, 0);

            }
        });

        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShopDetails.class).putExtra("Uid", BookShopOwnerID));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        radioButton2 = (RadioButton) findViewById(R.id.onPlace);
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Form2.class).putExtra("Sid", BookShopOwnerID));
                overridePendingTransition(0, 0);

            }
        });

        getshopDetails(BookShopOwnerID);
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