package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class ShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtShopname,txtShopAddress, txtratingnum;
    public ItemClickListner listner;
    public RatingBar bookrating;
    public ImageView imageView;

    public ShopViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.image_shop);
        bookrating = (RatingBar) itemView.findViewById(R.id.booking_ratestar);
        txtratingnum = (TextView) itemView.findViewById(R.id.booking_Rating);
        txtShopname = (TextView) itemView.findViewById(R.id.shop_name);
        txtShopAddress = (TextView) itemView.findViewById(R.id.address_shop);
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
