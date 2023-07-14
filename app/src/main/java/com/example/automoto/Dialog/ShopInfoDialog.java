package com.example.automoto.Dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.automoto.Main_Home;
import com.example.automoto.Model.ShopOwners;
import com.example.automoto.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShopInfoDialog extends BottomSheetDialogFragment{


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);


        View view = View.inflate(getContext(), R.layout.shop_map_dialog_info, null);
        dialog.setContentView(view);
        TextView shopname = (TextView) view.findViewById(R.id.d_shopname);
        TextView addressS = (TextView) view.findViewById(R.id.d_addrshop);

        String Shopname = getArguments().getString("StoreAdd");
        shopname.setText(Shopname);

    }

}

