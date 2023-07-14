package com.example.automoto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.Model.FeebackRatings;
import com.example.automoto.Model.ProductSearch;
import com.example.automoto.Model.ShopSearch;
import com.example.automoto.ProductDetails;
import com.example.automoto.R;
import com.example.automoto.ShopDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
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


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder>{



    Context context;
    ArrayList<ShopSearch> Shoplist;

    private int totalrv = 0;
    private int totalcount = 0;
    private float totalrvs = 0;

    public ShopAdapter(Context context, ArrayList<ShopSearch> list) {
        this.context = context;
        this.Shoplist = list;

    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_list, parent, false );


        return new ShopViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopSearch shopSearch = Shoplist.get(position);

        holder.txtShopname.setText(shopSearch.getShopname());
        holder.txtShopAddress.setText(shopSearch.getAddress());
        Picasso.get().load(shopSearch.getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetails.class);
                intent.putExtra("Uid", shopSearch.getPid());
                context.startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference("Shop Ratings").child("RidersView").child(shopSearch.getPid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            totalrvs = 0;

                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                FeebackRatings feebackRatings = snapshot1.getValue(FeebackRatings.class);
                                float rating = Float.parseFloat(""+ feebackRatings.getRatingValue());
                                totalrvs = totalrvs + rating;

                            }

                            long totalreview = snapshot.getChildrenCount();
                            float avgRating = totalrvs / totalreview;

                            holder.txtratingnum.setText(String.format("%.1f", avgRating));
                            holder.bookrating.setRating(avgRating);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return Shoplist.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public TextView txtShopname,txtShopAddress, txtratingnum;
        public ItemClickListner listner;
        public RatingBar bookrating;
        public ImageView imageView;


        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_shop);
            bookrating = (RatingBar) itemView.findViewById(R.id.booking_ratestar);
            txtratingnum = (TextView) itemView.findViewById(R.id.booking_Rating);
            txtShopname = (TextView) itemView.findViewById(R.id.shop_name);
            txtShopAddress = (TextView) itemView.findViewById(R.id.address_shop);

        }
    }

}
