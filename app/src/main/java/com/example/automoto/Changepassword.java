package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Changepassword extends AppCompatActivity {

    private TextInputEditText emails;
    private Button proceed;
    private FirebaseAuth auth;
    private ProgressBar shit;
    private ImageView back;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        back = (ImageView) findViewById(R.id.btnbacked);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(1, 1);
                finish();
            }
        });

        emails = (TextInputEditText) findViewById(R.id.ForgotEmail);
        proceed = (Button) findViewById(R.id.forgotBtn);
        auth = FirebaseAuth.getInstance();
        shit = (ProgressBar) findViewById(R.id.progress_bars);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emails.getText().toString().trim();

        if(email.isEmpty()){
            emails.setError("Email Is Required");
            emails.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emails.setError("Please Provide valid email");
            emails.requestFocus();
            return;
        }

        shit.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(Changepassword.this, Login.class);
                    Toast.makeText(Changepassword.this, "Please Check your email", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Changepassword.this, "Something's wrong", Toast.LENGTH_LONG).show();
                }
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