package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class NearbyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView nShop,nAddress,distance;
    public ImageButton directions;
    public ItemClickListner listner;
    public ImageView imageView;

    public NearbyViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.nearby_img);
        directions = (ImageButton) itemView.findViewById(R.id.nearby_navigate);
        nShop = (TextView) itemView.findViewById(R.id.nearby_shopName);
        nAddress = (TextView) itemView.findViewById(R.id.nearby_address);
        distance = (TextView) itemView.findViewById(R.id.nearby_distance);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view , getAdapterPosition(), false);
    }
}
