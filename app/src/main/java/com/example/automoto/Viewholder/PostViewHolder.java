package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView sub, bod, petsa;

    public ItemClickListner listner;


    public PostViewHolder(View itemView)
    {
        super(itemView);

        sub = (TextView) itemView.findViewById(R.id.subject);
        bod = (TextView) itemView.findViewById(R.id.body);
        petsa = (TextView) itemView.findViewById(R.id.datepost);

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
