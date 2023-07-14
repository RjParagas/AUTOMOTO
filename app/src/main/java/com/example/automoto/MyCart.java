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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.automoto.Model.CartList;
import com.example.automoto.Model.Products;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.MyCartViewHolder;
import com.example.automoto.Viewholder.ShopViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class MyCart extends AppCompatActivity {

    private DatabaseReference cartRef, shippedRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView myOrder;
    private ImageButton backBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        cartRef = FirebaseDatabase.getInstance().getReference().child("Customer Cart");



        recyclerView = findViewById(R.id.addedcartlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myOrder = (TextView) findViewById(R.id.my_order);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCart.this, MyShipped.class);
                startActivity(intent);
            }
        });

        backBtn = (ImageButton) findViewById(R.id.backbts);
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


        FirebaseRecyclerOptions<CartList> options = new FirebaseRecyclerOptions.Builder<CartList>()
                .setQuery(cartRef.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), CartList.class).build();

        FirebaseRecyclerAdapter<CartList, MyCartViewHolder> adapter =
                new FirebaseRecyclerAdapter<CartList, MyCartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyCartViewHolder holder, int position, @NonNull CartList model) {

                        holder.txtItemName.setText(model.getItemName());
                        holder.txtItemPrice.setText(model.getPrice());
                        holder.txtItemQty.setText(model.getQty());
                        holder.txtItemTotal.setText(model.getTotalPrice());
                        Picasso.get().load(model.getItemImage()).into(holder.ItemImage);
                        holder.proceedBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                HashMap<String, Object> saveData = new HashMap<>();

                                saveData.put("ItemImage", model.getItemImage());
                                saveData.put("ItemID", model.getItemID());
                                saveData.put("CartID", model.getCartID());
                                saveData.put("date", model.getDate());
                                saveData.put("time", model.getTime());
                                saveData.put("ItemName", model.getItemName());
                                saveData.put("Price", model.getPrice());
                                saveData.put("Qty", model.getQty());
                                saveData.put("TotalPrice", model.getTotalPrice());
                                saveData.put("ShopID",model.getShopID());
                                saveData.put("CustomerID", FirebaseAuth.getInstance().getCurrentUser().getUid());


                                shippedRef = FirebaseDatabase.getInstance().getReference("Item Orders");
                                shippedRef.child("CustomerRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(model.getCartID()).updateChildren(saveData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful()) {
                                                    FirebaseDatabase.getInstance().getReference("ShopOrders")
                                                            .child(model.getShopID()).child(model.getCartID())
                                                            .updateChildren(saveData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        Toast.makeText(MyCart.this,"Place Order Sucessfully", Toast.LENGTH_SHORT).show();
                                                                        cartRef.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getCartID()).removeValue();

                                                                    }
                                                                    else{
                                                                        String message = task.getException().toString();
                                                                        Toast.makeText(MyCart.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }


                                            }
                                        });

                            }
                        });

                        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cartRef.child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getCartID()).removeValue();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_layout, parent, false );
                        MyCartViewHolder holder = new MyCartViewHolder(view);
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