package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Email_Verification extends AppCompatActivity {

    private Button send, next;
    private TextView email;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private TextView count, countser; // daily count
    private int counts = 0;
    private int numyear = 0;
    private TextView countweekss, countmonthss, countyearss; // for riders
    private TextView countweekowner,countmonthowner, countyearowner; // for owners to be set 0 if no one will signup

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        String emails = mUser.getEmail();
        email.setText(emails);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        send = findViewById(R.id.confirmb);
        next = findViewById(R.id.nextlevel);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savetodatabase();
            }
        });

        count = (TextView) findViewById(R.id.counts);
        countser = (TextView) findViewById(R.id.counters);

        countweekss = (TextView) findViewById(R.id.counterweek);
        countweekowner = (TextView) findViewById(R.id.counterweeks);

        countmonthss = (TextView) findViewById(R.id.countermonth);
        countmonthowner = (TextView) findViewById(R.id.countermonths);

        countyearss = (TextView) findViewById(R.id.counteryear);
        countyearowner = (TextView) findViewById(R.id.yearly);

        email= findViewById(R.id.emailid);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });
        getTotalNumbers();
    }

    private void getTotalNumbers() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());


        // for riders
        // count for days//
        FirebaseDatabase.getInstance().getReference().child("Riders").orderByChild("date").equalTo(saveCurrentDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counts = (int) snapshot.getChildrenCount();
                    count.setText(String.valueOf(counts));
                } else {
                    count.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // for shop owners
        // count for daily
        FirebaseDatabase.getInstance().getReference().child("ShopOwners").orderByChild("date").equalTo(saveCurrentDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counts = (int) snapshot.getChildrenCount();
                    countser.setText(String.valueOf(counts));
                }
                else{
                    countser.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        gettotalweekly();

    }

    private void gettotalweekly() {

        Calendar calendar = Calendar.getInstance();



        //week//
        int weekOfYear = calendar.get(Calendar.WEEK_OF_MONTH);
        String Week = String.valueOf(weekOfYear);

        //year//
        SimpleDateFormat years = new SimpleDateFormat("yyyy");
        String Year = years.format(calendar.getTime());

        //month//
        SimpleDateFormat Month = new SimpleDateFormat("MMM");
        String months = Month.format(calendar.getTime());

        //count for weeks//
        FirebaseDatabase.getInstance().getReference().child("Riders").orderByChild("week").equalTo("Week " + Week + "/" + months + "/" + Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counts = (int) snapshot.getChildrenCount();
                    countweekss.setText(String.valueOf(counts));
                } else {
                    countweekss.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //count for weeks//
        FirebaseDatabase.getInstance().getReference().child("ShopOwners").orderByChild("week").equalTo( "Week " + Week + "/" + months + "/" + Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counts = (int) snapshot.getChildrenCount();
                    countweekowner.setText(String.valueOf(counts));
                }
                else{
                    countweekowner.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gettotalmonthly();
    }

    private void gettotalmonthly() {

        Calendar calendar = Calendar.getInstance();

        //year//
        SimpleDateFormat years = new SimpleDateFormat("yyyy");
        String Year = years.format(calendar.getTime());

        //month//
        SimpleDateFormat Month = new SimpleDateFormat("MMM");
        String months = Month.format(calendar.getTime());

        //count for months
        FirebaseDatabase.getInstance().getReference().child("Riders").orderByChild("month").equalTo(months + "/" + Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counts = (int) snapshot.getChildrenCount();
                    countmonthss.setText(String.valueOf(counts));
                }
                else{
                    countmonthss.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //count for months
        FirebaseDatabase.getInstance().getReference().child("ShopOwners").orderByChild("month").equalTo(months + "/" + Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counts = (int) snapshot.getChildrenCount();
                    countmonthowner.setText(String.valueOf(counts));
                }
                else{
                    countmonthowner.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gettotalyearly();
    }

    private void gettotalyearly() {

        Calendar calendar = Calendar.getInstance();

        //year//
        SimpleDateFormat years = new SimpleDateFormat("yyyy");
        String Year = years.format(calendar.getTime());

        //count for years
        FirebaseDatabase.getInstance().getReference().child("ShopOwners").orderByChild("year").equalTo(Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    numyear = (int) snapshot.getChildrenCount();
                    countyearowner.setText(String.valueOf(numyear));
                }
                else{
                    countyearowner.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //count for years
        FirebaseDatabase.getInstance().getReference("Riders").orderByChild("year").equalTo(Year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    numyear = (int) snapshot.getChildrenCount();
                    countyearss.setText(String.valueOf(numyear));
                }
                else{
                    countyearss.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    //save to admin counts for analytics
    private void savetodatabase() {
        Calendar calendar = Calendar.getInstance();


        //for weekly
        int weekOfYear = calendar.get(Calendar.WEEK_OF_MONTH);
        String Week = String.valueOf(weekOfYear);

        //year//
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String Year = year.format(calendar.getTime());

        //month//
        SimpleDateFormat Month = new SimpleDateFormat("MMM");
        String month = Month.format(calendar.getTime());

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        String numbers = count.getText().toString();
        String numbers1 = countser.getText().toString();

        String ridersweekly = countweekss.getText().toString();
        String ownersweekly = countweekowner.getText().toString();

        String ridersmonthly = countmonthss.getText().toString();
        String ownersmonthly = countmonthowner.getText().toString();

        String ridersyearly = countyearss.getText().toString();
        String ownersyearly = countyearowner.getText().toString();

        //for daily
        HashMap<String, Object> countMap = new HashMap<>();
        countMap.put("Count", numbers);
        countMap.put("Counters", numbers1);
        countMap.put("Ridersweekly", ridersweekly);
        countMap.put("Ownersweekly", ownersweekly);
        countMap.put("Ridersmontly",ridersmonthly);
        countMap.put("Ownersmonthly", ownersmonthly );
        countMap.put("Ridersyearly", ridersyearly );
        countMap.put("Ownersyearly", ownersyearly);



        FirebaseDatabase.getInstance().getReference().child("Admin").child("RidersCount").child(saveCurrentDate).updateChildren(countMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Admin").child("UserCount").child("week").child("Week" + Week + month).updateChildren(countMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseDatabase.getInstance().getReference().child("Admin").child("UserCount").child("Month").child(month + Year).updateChildren(countMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference().child("Admin").child("UserCount").child("Year").child(Year).updateChildren(countMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendVerificationCode() {
        mUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Email_Verification.this, "Verification Code Sent to" + mUser.getEmail(),Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Email_Verification.this, "Verification Code is failed to send",Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}