package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Adapter.ServicesAdapter;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.Services;
import com.example.automoto.Model.ShopFeedback;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Model.UserProfile;
import com.example.automoto.Model.Users;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.FeedbackViewHolder;
import com.example.automoto.Viewholder.ShopFeedbackHolder;
import com.example.automoto.Viewholder.ShopViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class ShopDetails extends AppCompatActivity {

    private ImageView shopImage;
    private String UserID, saveCurrentDate, saveCurrentTime, comfeed, comratetext, comratevalue;
    private TextView shopName, shopDesc, shopPrice, ShopOwner, ShopAddress, ShopAvail, RateCount, RateCount2, totalreviews, txtratings, notverified;
    private EditText feedfucks;
    private RatingBar ratingfuck, ratingBar;
    private Button Booknow, BtnFeedfuck;
    private ImageButton back;
    private String ShopOwnerID, StoreName;
    private Float RatingValue;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView recyclerView1;
    private int totalrv = 0;
    private int totalcount = 0;
    private float totalrvs = 0;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ShopRefBook, shopRate, databaseReference;
    ArrayList<Services> servlist;
    ServicesAdapter servicesAdapter;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);


        ShopOwnerID = getIntent().getStringExtra("Uid");

        ShopRefBook = FirebaseDatabase.getInstance().getReference().child("ShopOwners");

        recyclerView = findViewById(R.id.book_reviews);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView1 = (RecyclerView) findViewById(R.id.services_list);
        layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        servlist = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(this, servlist);
        recyclerView1.setAdapter(servicesAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Services")
                .child("TheRiders").child(ShopOwnerID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Services services = dataSnapshot.getValue(Services.class);
                    servlist.add(services);
                }
                servicesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        shopImage = (ImageView) findViewById(R.id.shop_image);
        shopName = (TextView) findViewById(R.id.shop_name);
        shopDesc= (TextView) findViewById(R.id.shop_desc);
        shopPrice = (TextView) findViewById(R.id.shop_price);
        ShopOwner = (TextView) findViewById(R.id.shop_owner);
        ShopAddress = (TextView) findViewById(R.id.shop_address);
        notverified = (TextView) findViewById(R.id.themessage);
        txtratings = (TextView) findViewById(R.id.shop_textrate);
        RateCount = (TextView) findViewById(R.id.rating_value);
        RateCount2 = (TextView) findViewById(R.id.rating_value2);
        ShopAvail = (TextView) findViewById(R.id.shop_Avail);
        totalreviews = (TextView) findViewById(R.id.shop_reviewst);
        feedfucks = (EditText) findViewById(R.id.add_feedfucks);
        ratingfuck = (RatingBar) findViewById(R.id.riders_frate);
        ratingBar = (RatingBar) findViewById(R.id.shop_rate);
        back = (ImageButton) findViewById(R.id.backbts);

        ratingfuck.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                //RATING NUMBERS AND TEXT//

                RatingValue = ratingBar.getRating();

                if (RatingValue<=1 && RatingValue>0)
                    RateCount.setText("Very Bad ");
                RateCount2.setText(""+ RatingValue);

                if (RatingValue<=2 && RatingValue>1)
                    RateCount.setText("Bad " );
                RateCount2.setText(""+ RatingValue);

                if (RatingValue<=3 && RatingValue>2)
                    RateCount.setText("Good ");
                RateCount2.setText(""+ RatingValue);

                if (RatingValue<=4 && RatingValue>3)
                    RateCount.setText("Very Good ");
                RateCount2.setText(""+ RatingValue);

                if (RatingValue<=5 && RatingValue>4)
                    RateCount.setText("Excellent ");
                RateCount2.setText(""+ RatingValue);


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(1, 1);
                finish();
            }
        });

        BtnFeedfuck = (Button) findViewById(R.id.submit_ffeed);

        BtnFeedfuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateFeedback();
                feedfucks.setText(null);
                ratingfuck.setRating(0);
            }
        });

        Booknow = (Button) findViewById(R.id.book_now);

        getShopDetails(ShopOwnerID);

        getUserdetails();

    }

    private void getUserdetails() {

        FirebaseDatabase.getInstance().getReference().child("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if (userProfile.Status.equals("Verified")){

                    notverified.setVisibility(View.GONE);
                    Booknow.setEnabled(true);

                }else if (userProfile.Status.equals("Not Verified")){

                    notverified.setVisibility(View.VISIBLE);
                    Booknow.setEnabled(false);

                }
                else if (userProfile.Status.equals("Pending")) {

                    notverified.setVisibility(View.VISIBLE);
                    Booknow.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ValidateFeedback() {
        comfeed = feedfucks.getText().toString();

        if (comfeed.isEmpty())
        {
            feedfucks.setError("Feedback Comment is required");
            feedfucks.requestFocus();
            return;
        }

        else
        {
            SaveFeedback();
        }

    }

    private void SaveFeedback() {


        comratetext = RateCount.getText().toString();
        comratevalue = RateCount2.getText().toString();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final HashMap<String, Object> FeedbackMap = new HashMap<>();
        FeedbackMap.put("UserID", UserID);
        FeedbackMap.put("ShopID", ShopOwnerID);
        FeedbackMap.put("Comment", comfeed);
        FeedbackMap.put("RatingText", comratetext);
        FeedbackMap.put("RatingValue", comratevalue);
        FeedbackMap.put("Date", saveCurrentDate);
        FeedbackMap.put("Time", saveCurrentTime);


        FirebaseDatabase.getInstance().getReference().child("Shop Ratings").child("RidersView")
                .child(ShopOwnerID).child(saveCurrentDate + saveCurrentTime).updateChildren(FeedbackMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ShopDetails.this, "Your Feedback Has Been Posted Successfully", Toast.LENGTH_SHORT).show();

                        } else {


                        }
                    }
                });

    }

    private void getShopDetails(String ShopOwnerID) {

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("ShopOwners");
        shopRef.child(ShopOwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopOwners shopOwners = snapshot.getValue(ShopOwners.class);

                    shopName.setText(shopOwners.getShopname());
                    shopDesc.setText(shopOwners.getDescription());
                    shopPrice.setText(shopOwners.getPrice());
                    ShopOwner.setText(shopOwners.getName());
                    ShopAvail.setText(shopOwners.getAvailable());
                    ShopAddress.setText(shopOwners.getAddress());
                    Picasso.get().load(shopOwners.getImage()).into(shopImage);

                    ShopOwners owners = snapshot.getValue(ShopOwners.class);
                    Bookshop(owners, snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Shop Ratings").child("RidersView").child(ShopOwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalrv = (int) snapshot.getChildrenCount();
                    totalreviews.setText(Integer.toString(totalrv));


                }
                else {
                    totalreviews.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Shop Ratings").child("RidersView").child(ShopOwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    totalrvs = 0;

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        FeebackRatings feebackRatings = snapshot1.getValue(FeebackRatings.class);
                        float rating = Float.parseFloat(""+ feebackRatings.getRatingValue());
                        totalrvs = totalrvs + rating;

                    }

                    long totalreview = snapshot.getChildrenCount();
                    float avgRating = totalrvs / totalreview;

                    txtratings.setText(String.format("%.1f", avgRating));
                    ratingBar.setRating(avgRating);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void Bookshop(ShopOwners owners, String key) {
        String shopID = key;
        Booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopDetails.this, BookForm.class);
                intent.putExtra("Sid", shopID);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        shopRate = FirebaseDatabase.getInstance().getReference("Shop Ratings");
        FirebaseRecyclerOptions<ShopFeedback> options =
                new FirebaseRecyclerOptions.Builder<ShopFeedback>()
                        .setQuery(shopRate.child("RidersView").child(ShopOwnerID), ShopFeedback.class).build();

        FirebaseRecyclerAdapter<ShopFeedback, ShopFeedbackHolder> adapter =
                new FirebaseRecyclerAdapter<ShopFeedback, ShopFeedbackHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ShopFeedbackHolder holder, int position, @NonNull ShopFeedback model) {

                        holder.feedbackCom.setText(model.getComment());
                        holder.feedbackDate.setText(model.getDate());
                        holder.feedbackTime.setText(model.getTime());
                        holder.feedbackText.setText(model.getRatingText());
                        holder.feedbackRating.setRating(Float.parseFloat(model.getRatingValue()));

                        FirebaseDatabase.getInstance().getReference("Riders").child(model.getUserID())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Users user = snapshot.getValue(Users.class);
                                            holder.feedbackUser.setText(user.getUsername());
                                            Picasso.get().load(user.getImage()).into(holder.userImg);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }

                    @NonNull
                    @Override
                    public ShopFeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_ratings, parent, false );
                        ShopFeedbackHolder holder = new ShopFeedbackHolder(view);
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