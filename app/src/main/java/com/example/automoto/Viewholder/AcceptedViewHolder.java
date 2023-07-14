package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class AcceptedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView Servicetype,bookDate, bookStatus;
    public LottieAnimationView chat_wshopped;
    public ItemClickListner listner;


    public AcceptedViewHolder(View itemView)
    {
        super(itemView);

        Servicetype = (TextView) itemView.findViewById(R.id.my_atype);
        bookDate = (TextView) itemView.findViewById(R.id.my_adate);
        bookStatus = (TextView) itemView.findViewById(R.id.my_astatus);
        chat_wshopped = (LottieAnimationView) itemView.findViewById(R.id.chat_wshop);
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
