package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Model.Products;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Model.UserProfile;
import com.example.automoto.Utility.NetworkChangeListener;
import com.firebase.ui.database.FirebaseIndexArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ProductForm extends AppCompatActivity {

    private String imageitem, itemname, itemprice, itemqty, date, time, ownerid, price;
    private String ItemRandomID, ShopItem;
    private TextView pitemname, pitemprice, qtyitem, itemshopid, fprice, furl, locationdeliver;
    private ImageView itemimage;
    private DatabaseReference pdrefCart;
    private ImageButton addbtn, minusbtn, back;
    private StorageReference storageCart;
    private Button addtocart;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private double cost = 0;
    private double finalCost = 0;
    private int quantity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);



        ShopItem = getIntent().getStringExtra("Apid");
        pdrefCart = FirebaseDatabase.getInstance().getReference().child("Product");

        storageCart = FirebaseStorage.getInstance().getReference().child("Product Images");

        pitemname = (TextView) findViewById(R.id.fp_name);
        pitemprice = (TextView) findViewById(R.id.f_price);
        itemshopid = (TextView) findViewById(R.id.f_shopID);
        qtyitem = (TextView) findViewById(R.id.fp_qty);
        furl = (TextView) findViewById(R.id.f_imageurl);
        itemimage = (ImageView) findViewById(R.id.selected_item);
        addbtn = (ImageButton) findViewById(R.id.f_add);
        minusbtn = (ImageButton) findViewById(R.id.f_minus);
        addtocart = (Button) findViewById(R.id.fp_proceed);
        fprice = (TextView) findViewById(R.id.finalPrice);
        back = (ImageButton) findViewById(R.id.backbts);
        locationdeliver = (TextView) findViewById(R.id.riderlocation);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(0, 0);
                finish();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateOrder();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost = finalCost + cost;
                quantity++;

                fprice.setText(new DecimalFormat("#").format(finalCost));
                qtyitem.setText(""+ quantity);
            }
        });

        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity>1){
                    finalCost = finalCost - cost ;
                    quantity --;

                    fprice.setText(new DecimalFormat("#").format(finalCost));
                    qtyitem.setText("" + quantity);
                }
            }
        });


        getSelectedItem(ShopItem);

        getuserdetails();

    }

    private void getuserdetails() {
        FirebaseDatabase.getInstance().getReference().child("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if (snapshot.exists()){

                    locationdeliver.setText(userProfile.address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getSelectedItem(String shopItem) {

        pdrefCart.child("RidersView").child(ShopItem).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    itemshopid.setText(products.getSid());
                    pitemname.setText(products.getProductname());
                    pitemprice.setText(products.getProductprice());
                    furl.setText(products.getProductimage());
                    fprice.setText("" + finalCost);
                    qtyitem.setText("" + quantity);
                    Picasso.get().load(products.getProductimage()).into(itemimage);
                    price = products.getProductprice();
                    cost = Double.parseDouble(price.replaceAll("₱", ""));
                    finalCost = Double.parseDouble(price.replaceAll("₱", ""));
                    quantity = 1;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void validateOrder() {

        imageitem = furl.getText().toString();
        itemname = pitemname.getText().toString();
        itemprice = pitemprice.getText().toString();
        itemqty = qtyitem.getText().toString();
        ownerid = itemshopid.getText().toString();
        price = fprice.getText().toString();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
        time = currentTime.format(calendar.getTime());

        ItemRandomID = date + time;

        SaveProductInfoToDatabase();
    }

    private void SaveProductInfoToDatabase() {

        final HashMap<String, Object> AddToCartMap = new HashMap<>();
        AddToCartMap.put("ItemImage", imageitem);
        AddToCartMap.put("ItemID", ShopItem);
        AddToCartMap.put("CartID", ItemRandomID);
        AddToCartMap.put("date", date);
        AddToCartMap.put("time", time);
        AddToCartMap.put("ItemName", itemname);
        AddToCartMap.put("Price", itemprice);
        AddToCartMap.put("Qty", itemqty);
        AddToCartMap.put("TotalPrice", price);
        AddToCartMap.put("ShopID",ownerid);



        FirebaseDatabase.getInstance().getReference().child("Customer Cart")
                .child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(ItemRandomID).updateChildren(AddToCartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {


                            Intent intent = new Intent(ProductForm.this, Shop.class);
                            startActivity(intent);

                            Toast.makeText(ProductForm.this, "Item Added to Cart successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(ProductForm.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }

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