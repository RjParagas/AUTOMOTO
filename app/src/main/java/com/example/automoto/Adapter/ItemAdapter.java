package com.example.automoto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.ProductSearch;
import com.example.automoto.Model.Products;
import com.example.automoto.ProductDetails;
import com.example.automoto.R;
import com.example.automoto.Shop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    ArrayList<ProductSearch> itemlist;
    private int totalcount = 0;
    private float totalrv = 0;


    public ItemAdapter(Context context, ArrayList<ProductSearch> list) {
        this.context = context;
        this.itemlist = list;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ProductSearch products = itemlist.get(position);
        holder.productname.setText(products.getProductname());
        holder.product_price.setText(products.getProductprice());
        Picasso.get().load(products.getProductimage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("pdid", products.getProductID());
                intent.putExtra("psid", products.getSid());
                context.startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference("Item Ratings").child("RidersView").child(products.getProductID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    totalrv = 0;

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        FeebackRatings feebackRatings = snapshot1.getValue(FeebackRatings.class);
                        float rating = Float.parseFloat("" + feebackRatings.getRatingValue());
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

    @Override
    public int getItemCount() {
        return itemlist.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView productname, product_price, product_rate;
        public ItemClickListner listner;
        public ImageView imageView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.name_product);
            product_price = (TextView) itemView.findViewById(R.id.p_price);
            product_rate = (TextView) itemView.findViewById(R.id.product_ratings);
            imageView = (ImageView) itemView.findViewById(R.id.image_product);

        }
    }

}

