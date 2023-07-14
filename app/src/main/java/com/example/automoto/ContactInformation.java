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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automoto.Model.UserProfile;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ContactInformation extends AppCompatActivity {

    private ImageButton btnBack;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button update;
    private String userID;
    private ProgressDialog loadingBar;
    private TextInputLayout email, phone;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);



        loadingBar = new ProgressDialog(this);

        email = (TextInputLayout) findViewById(R.id.user_email);
        phone = (TextInputLayout) findViewById(R.id.user_number);

        update = (Button) findViewById(R.id.proceedbtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatetoDatabase();
            }
        });

        btnBack = (ImageButton) findViewById(R.id.backbtends);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ContactInformation.this, UpdateAccountDetails.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        getUserDet();
    }



    private void getUserDet() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Riders");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if(userProfile !=null){
                    String Email = userProfile.email;
                    String Phone = userProfile.phoneNo;

                    email.getEditText().setText(Email);
                    phone.getEditText().setText(Phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ContactInformation.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void UpdatetoDatabase() {

        loadingBar.setTitle("Updating Contact");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        final HashMap<String, Object> ContactMap = new HashMap<>();
        ContactMap.put("phoneNo", email.getEditText().getText().toString());
        ContactMap.put("email", phone.getEditText().getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Riders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(ContactMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(getApplicationContext(), UpdateAccountDetails.class));
                    overridePendingTransition(0, 0);
                    loadingBar.dismiss();
                }
                else
                {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(ContactInformation.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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