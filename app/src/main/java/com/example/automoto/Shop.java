package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.Products;
import com.example.automoto.Model.Users;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Shop extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView addProduct;
    private ImageView searchItem;
    private String searchinput;
    ImageSlider imageSlider;
    private DatabaseReference product_ref;
    private LottieAnimationView mycarted;
    private DatabaseReference rvref;
    private int totalcount = 0;
    private float totalrv = 0;


    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);



        recyclerView = findViewById(R.id.productlist);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        imageSlider = findViewById(R.id.image_slider);

        mycarted = (LottieAnimationView) findViewById(R.id.my_cart);
        mycarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Shop.this, MyCart.class);
                startActivity(intent);
            }
        });


        searchItem = (ImageView) findViewById(R.id.item_search);
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shop.this, SearchItem.class);
                startActivity(intent);
            }
        });


        ArrayList<SlideModel> arrayList = new ArrayList<>();
        arrayList.add(new SlideModel(R.drawable.moto, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.slide2, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.slide3, ScaleTypes.FIT));
        imageSlider.setImageList(arrayList);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.shop);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), Main_Home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.booking:
                        startActivity(new Intent(getApplicationContext(), Booking.class));
                        overridePendingTransition(0, 0);
                        finish();
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



        product_ref = FirebaseDatabase.getInstance().getReference("Product");
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(product_ref.child("RidersView"),Products.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                        holder.productname.setText(model.getProductname());
                        holder.product_price.setText(model.getProductprice());
                        Picasso.get().load(model.getProductimage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(Shop.this, ProductDetails.class);
                                intent.putExtra("pdid", model.getProductID());
                                intent.putExtra("psid", model.getSid());
                                startActivity(intent);


                            }

                        });



                   FirebaseDatabase.getInstance().getReference("Item Ratings").child("RidersView").child(model.getProductID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {

                                    totalrv = 0;

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        FeebackRatings feebackRatings = snapshot1.getValue(FeebackRatings.class);
                                        float rating = Float.parseFloat(""+ feebackRatings.getRatingValue());
                                        totalrv = totalrv + rating;

                                    }

                                    long totalreview = snapshot.getChildrenCount();
                                    float avgRating = totalrv / totalreview;

                                    holder.product_rate.setText(String.format("%.1f", avgRating));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false );
                        ProductViewHolder holder = new ProductViewHolder(view);
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