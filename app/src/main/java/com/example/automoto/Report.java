package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.core.Repo;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Report extends AppCompatActivity {

    private ImageButton btnBack;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button send;
    private String userID;
    private ProgressDialog loadingBar;
    private String Fullname, saveCurrentDate, saveCurrentTime;
    private TextInputLayout sub;
    private EditText det;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        loadingBar = new ProgressDialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Riders");
        userID = user.getUid();

        sub = (TextInputLayout) findViewById(R.id.report_sub);
        det = (EditText) findViewById(R.id.report_details);

        send = (Button) findViewById(R.id.sendrep);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savetoDatabase();
            }
        });

        btnBack = (ImageButton) findViewById(R.id.backbtends);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Report.this, Settings.class));
                overridePendingTransition(0, 0);
            }
        });

        getUserDet();
    }

    private void getUserDet() {

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);

                if(userProfile !=null){
                    Fullname = userProfile.name;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Report.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void savetoDatabase() {

        loadingBar.setTitle("Updating Account Information");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        String Rider = "Riders";
        final HashMap<String, Object> ReportMap = new HashMap<>();
        ReportMap.put("Subject", sub.getEditText().getText().toString());
        ReportMap.put("Details", det.getText().toString());
        ReportMap.put("Name", Fullname);
        ReportMap.put("User Type", Rider);
        ReportMap.put("Rid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        ReportMap.put("Date", saveCurrentDate);
        ReportMap.put("Time", saveCurrentTime);

        FirebaseDatabase.getInstance().getReference().child("Admin")
                .child("Report").child(saveCurrentDate).child(saveCurrentTime)
                .updateChildren(ReportMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Admin").child("Report").child("Riders").push().updateChildren(ReportMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                DialogBox();
                            }
                            else
                            {
                                loadingBar.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(Report.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void DialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Report.this);
        builder.setTitle("Report Submitted.");
        builder.setMessage("Thank you for submitting your concern. We will send the update of the report you created on your email.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Report.this, Settings.class));
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }



}