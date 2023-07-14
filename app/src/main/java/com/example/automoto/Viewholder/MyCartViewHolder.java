package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class MyCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtItemName, txtItemPrice, txtItemQty, txtItemTotal, cancelBtn, proceedBtn;
    public ItemClickListner listner;
    public ImageView ItemImage;

    public MyCartViewHolder(View itemView)
    {
        super(itemView);

        ItemImage = (ImageView) itemView.findViewById(R.id.added_imgitem);
        txtItemName = (TextView) itemView.findViewById(R.id.added_pname);
        txtItemPrice = (TextView) itemView.findViewById(R.id.added_price);
        txtItemQty = (TextView) itemView.findViewById(R.id.added_qty);
        txtItemTotal = (TextView) itemView.findViewById(R.id.added_totalp);
        cancelBtn = (TextView) itemView.findViewById(R.id.added_cancel);
        proceedBtn = (TextView) itemView.findViewById(R.id.added_proceed);
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
