package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class MyOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView ItemName, ItemQty, ItemTotal, txtStatus;
    public ItemClickListner listner;
    public ImageView ItemImage;

    public MyOrderViewHolder(View itemView)
    {
        super(itemView);

        ItemImage = (ImageView) itemView.findViewById(R.id.s_imgitem);
        ItemName = (TextView) itemView.findViewById(R.id.sp_pname);
        ItemTotal = (TextView) itemView.findViewById(R.id.sp_ptotal);
        ItemQty = (TextView) itemView.findViewById(R.id.sp_pqty);
        txtStatus = (TextView) itemView.findViewById(R.id.sp_pstatus);
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
