package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView Servicetype,bookDate, bookStatus;
    public ItemClickListner listner;


    public BookingViewHolder(View itemView)
    {
        super(itemView);

        Servicetype = (TextView) itemView.findViewById(R.id.my_btype);
        bookDate = (TextView) itemView.findViewById(R.id.my_date);
        bookStatus = (TextView) itemView.findViewById(R.id.my_status);
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