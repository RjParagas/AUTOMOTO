package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Rating;
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

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.Products;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Model.User;
import com.example.automoto.Model.UserProfile;
import com.example.automoto.Model.Users;
import com.example.automoto.Utility.NetworkChangeListener;
import com.example.automoto.Viewholder.FeedbackViewHolder;
import com.example.automoto.Viewholder.ProductViewHolder;
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

public class ProductDetails extends AppCompatActivity {


    private String UserID,ShopID, ProductID, Comment, RatingsText,RatingsValue, saveCurrentDate,saveCurrentTime;
    private ImageView productImage, product_img;
    private TextView pdname, pdprice, pdowner, pdcategory, pdstock, pdwarranty, pddetails, RateCount, RateCount2, totalreview, pdloc, textRating, notverified;
    private EditText feedbackCom;
    private Button AddCom;
    private RatingBar feedbackRate, totalRatings;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LottieAnimationView addTocart;
    private ImageButton back;
    private FirebaseAuth firebaseAuth;
    ImageSlider imageSlider;
    private DatabaseReference pdrefBook ,itemRate;
    private Float RatingValue;
    private int totalrv = 0;
    private int totalcount = 0;
    private float totalrvs = 0;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ProductID = getIntent().getStringExtra("pdid");

        ShopID = getIntent().getStringExtra("psid");

        pdrefBook = FirebaseDatabase.getInstance().getReference().child("Product");

        recyclerView = findViewById(R.id.feedbacks);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pdname = (TextView) findViewById(R.id.pd_name);
        pdprice = (TextView) findViewById(R.id.pd_price);

        pdowner = (TextView) findViewById(R.id.pd_shopname);
        pdloc = (TextView) findViewById(R.id.pd_shoploc);
        pdcategory = (TextView) findViewById(R.id.pd_category);
        pdwarranty = (TextView) findViewById(R.id.pd_warranty);
        pddetails = (TextView) findViewById(R.id.pd_details);
        textRating = (TextView) findViewById(R.id.text_rating);
        addTocart = (LottieAnimationView) findViewById(R.id.pd_add);
        AddCom = (Button) findViewById(R.id.comment_add);
        feedbackCom = (EditText) findViewById(R.id.riders_fcom);
        feedbackRate = (RatingBar) findViewById(R.id.riders_frate);
        totalRatings = (RatingBar) findViewById(R.id.product_rate);
        notverified = (TextView) findViewById(R.id.themessage);
        RateCount = (TextView) findViewById(R.id.rating_value);
        RateCount2 = (TextView) findViewById(R.id.rating_value2);
        totalreview = (TextView) findViewById(R.id.r_totalrv);
        product_img = (ImageView) findViewById(R.id.pd_shopimg) ;
        back = (ImageButton) findViewById(R.id.backbts);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(0, 0);
                finish();
            }
        });

        feedbackRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
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

        AddCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validatecomment();
                feedbackCom.setText(null);
                feedbackRate.setRating(0);
            }
        });

        getProductDetails(ProductID);

        getUserDetails();

    }

    private void getUserDetails() {

        FirebaseDatabase.getInstance().getReference().child("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if (userProfile.Status.equals("Verified")){

                    notverified.setVisibility(View.GONE);
                    addTocart.setEnabled(true);

                }else if (userProfile.Status.equals("Not Verified")){

                    notverified.setVisibility(View.VISIBLE);
                    addTocart.setEnabled(false);

                }
                else if (userProfile.Status.equals("Pending")){

                    notverified.setVisibility(View.VISIBLE);
                    addTocart.setEnabled(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Validatecomment() {

        Comment = feedbackCom.getText().toString();

        if (Comment.isEmpty())
        {
            feedbackCom.setError("Feedback Comment is required");
            feedbackCom.requestFocus();
            return;
        }

        else
        {
            SaveFeedback();
        }

    }

    private void getProductDetails(String productID) {

        pdrefBook.child("RidersView").child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    pdname.setText(products.getProductname());
                    pdprice.setText(products.getProductprice());
                    pdcategory.setText(products.getCategory());
                    pdwarranty.setText(products.getWarranty());
                    pddetails.setText(products.getDescription());

                    imageSlider = findViewById(R.id.image_sliderss);
                    ArrayList<SlideModel> arrayList = new ArrayList<>();
                    arrayList.add(new SlideModel(products.getProductimage(), ScaleTypes.FIT));
                    arrayList.add(new SlideModel(products.getProductimage1(), ScaleTypes.FIT));
                    arrayList.add(new SlideModel(products.getProductimage2(), ScaleTypes.FIT));
                    arrayList.add(new SlideModel(products.getProductimage3(), ScaleTypes.FIT));
                    imageSlider.setImageList(arrayList);

                    Products getItem = snapshot.getValue(Products.class);
                    ProductItem(getItem, snapshot.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("ShopOwners").child(ShopID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ShopOwners shopOwners = snapshot.getValue(ShopOwners.class);

                    pdowner.setText(shopOwners.getShopname());
                    pdloc.setText(shopOwners.getAddress());
                    Picasso.get().load(shopOwners.getImage()).into(product_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Item Ratings").child("RidersView").child(ProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalrv = (int) snapshot.getChildrenCount();
                    totalreview.setText(Integer.toString(totalrv));


                }
                else {
                    totalreview.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Item Ratings").child("RidersView").child(ProductID).addValueEventListener(new ValueEventListener() {
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

                    textRating.setText(String.format("%.1f", avgRating));
                    totalRatings.setRating(avgRating);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void ProductItem(Products getItem, String key) {

        String ItemID = key;
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, ProductForm.class);
                intent.putExtra("Apid", ItemID);
                startActivity(intent);
            }
        });
    }


    private void SaveFeedback() {

        RatingsText = RateCount.getText().toString();
        RatingsValue = RateCount2.getText().toString();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final HashMap<String, Object> FeedbackMap = new HashMap<>();
        FeedbackMap.put("UserID", UserID);
        FeedbackMap.put("ShopID", ShopID);
        FeedbackMap.put("ProductID", ProductID);
        FeedbackMap.put("Comment", Comment);
        FeedbackMap.put("RatingText", RatingsText);
        FeedbackMap.put("RatingValue", RatingsValue);
        FeedbackMap.put("Date", saveCurrentDate);
        FeedbackMap.put("Time", saveCurrentTime);


        FirebaseDatabase.getInstance().getReference().child("Item Ratings").child("RidersView")
                .child(ProductID).child(saveCurrentDate + saveCurrentTime).updateChildren(FeedbackMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ProductDetails.this, "Your Feedback Has Been Posted Successfully", Toast.LENGTH_SHORT).show();

                        } else {


                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        itemRate = FirebaseDatabase.getInstance().getReference("Item Ratings");
        FirebaseRecyclerOptions<FeebackRatings> options =
                new FirebaseRecyclerOptions.Builder<FeebackRatings>()
                .setQuery(itemRate.child("RidersView").child(ProductID), FeebackRatings.class).build();

        FirebaseRecyclerAdapter<FeebackRatings, FeedbackViewHolder> adapter =
                new FirebaseRecyclerAdapter<FeebackRatings, FeedbackViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull FeebackRatings model) {

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
                    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedbacks, parent, false );
                        FeedbackViewHolder holder = new FeedbackViewHolder(view);
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