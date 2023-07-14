package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productname, product_price, product_rate;
    public ItemClickListner listner;
    public ImageView imageView;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        productname = (TextView) itemView.findViewById(R.id.name_product);
        product_price = (TextView) itemView.findViewById(R.id.p_price);
        product_rate = (TextView) itemView.findViewById(R.id.product_ratings);
        imageView = (ImageView) itemView.findViewById(R.id.image_product);
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
