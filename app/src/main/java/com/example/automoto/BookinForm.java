package com.example.automoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;


import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automoto.Model.ShopOwners;
import com.example.automoto.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.validation.Validator;


public class BookinForm extends AppCompatActivity{

    private String Bookingtype,Servicetype, Rname, Radd, RDet, Rcon, saveCurrentDate, saveCurrentTime;
    private TextInputLayout rName, rAdd, rCon;
    private EditText rDet ;
    private RadioGroup s_area;
    private RadioButton radioButton;
    Spinner spinner;
    List<String> items;
    private ImageView DriversLic, ShopImg;
    private Button backbtn;
    private Button Continue;
    private StorageReference LicenseRef;
    private DatabaseReference BookingRef;
    private String BookRandomKey, downloadImageUrl, BookShopOwnerID;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private ProgressDialog loadingBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookin_form);

        LicenseRef = FirebaseStorage.getInstance().getReference().child("Drivers License");

        BookShopOwnerID = getIntent().getStringExtra("Sid");

        rName = (TextInputLayout) findViewById(R.id.booking_name);
        rAdd = (TextInputLayout) findViewById(R.id.booking_address);
        rDet = (EditText) findViewById(R.id.booking_details);
        rCon = (TextInputLayout) findViewById(R.id.booking_contact);
        DriversLic = (ImageView) findViewById(R.id.booking_license);
        ShopImg = (ImageView) findViewById(R.id.shopimages);
        backbtn = (Button) findViewById(R.id.back_btn);
        Continue = (Button) findViewById(R.id.book_proceed);


        s_area = (RadioGroup) findViewById(R.id.Area);

        loadingBar = new ProgressDialog(this);

        // spinner 1
        spinner = (Spinner) findViewById(R.id.service_type);
        items = new ArrayList<>();
        items.add("Choose Service Type");
        items.add("Change Frame");
        items.add("Change Oil");
        items.add("Full Repair (All in One)");
        items.add("Tire repair");
        items.add("Vulcanizing");
        spinner.setAdapter(new ArrayAdapter<>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,items));


        DriversLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(1, 1);
                finish();
            }
        });

        getShopDetails(BookShopOwnerID);


    }

    private void getShopDetails(String bookShopOwnerID) {

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


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            DriversLic.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        int selectedID = s_area.getCheckedRadioButtonId();
        radioButton = findViewById(selectedID);
        Bookingtype = radioButton.getText().toString();
        Servicetype = spinner.getSelectedItem().toString();
        Rname = rName.getEditText().getText().toString();
        Radd = rAdd.getEditText().getText().toString();
        RDet = rDet.getText().toString();
        Rcon = rCon.getEditText().getText().toString();





        if (ImageUri == null){
            Toast.makeText(this, "Please Upload your Driver's License", Toast.LENGTH_SHORT).show();
        }

       else if (s_area.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please Select Booking Type", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (spinner.getSelectedItem().toString().trim().equals("Choose Service Type"))
        {
            Toast.makeText(this, "Please Select Service Type", Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
            return;

        }
        else if (Rname.isEmpty())
        {
            rName.setError("Fullname is required");
            rName.requestFocus();
            return;
        }
        else if (Radd.isEmpty())
        {
            rAdd.setError("Address is required");
            rAdd.requestFocus();

            return;
        }

        else if (Rcon.isEmpty())
        {
            rCon.setError("Contact is required");
            rCon.requestFocus();
            return;
        }
        else if (RDet.isEmpty())
        {
            rDet.setError("Contact is required");
            rDet.requestFocus();
            return;
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Booking Request Successfully");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        BookRandomKey = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final StorageReference filePath = LicenseRef.child(ImageUri.getLastPathSegment() + BookRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(BookinForm.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(BookinForm.this, "Drivers License uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(BookinForm.this, "got the Drivers License image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {

        final HashMap<String, Object> BookingMap = new HashMap<>();
        BookingMap.put("Sid", BookShopOwnerID);
        BookingMap.put("rid", BookRandomKey);
        BookingMap.put("date", saveCurrentDate);
        BookingMap.put("time", saveCurrentTime);
        BookingMap.put("concern", RDet);
        BookingMap.put("image", downloadImageUrl);
        BookingMap.put("bookingtype", Bookingtype);
        BookingMap.put("servicetype", Servicetype);
        BookingMap.put("ridername", Rname);
        BookingMap.put("rideraddress", Radd);
        BookingMap.put("ridercontact", Rcon);

        FirebaseDatabase.getInstance().getReference().child("Booking")
                .child("TheRiders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .child(BookShopOwnerID).updateChildren(BookingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseDatabase.getInstance().getReference("Booking")
                                    .child("Owners").child(BookShopOwnerID).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .updateChildren(BookingMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(BookinForm.this, Main_Home.class);
                                                startActivity(intent);

                                                loadingBar.dismiss();
                                                Toast.makeText(BookinForm.this, "Booking is added successfully..", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                loadingBar.dismiss();
                                                String message = task.getException().toString();
                                                Toast.makeText(BookinForm.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                            }
                                    });

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