package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class ShopFeedbackHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView feedbackUser, feedbackCom, feedbackDate, feedbackTime, feedbackText;
    public RatingBar feedbackRating;
    public ImageView userImg;
    public ItemClickListner listner;


    public ShopFeedbackHolder(View itemView)
    {
        super(itemView);

        userImg = (ImageView) itemView.findViewById(R.id.shop_frimg);
        feedbackRating = (RatingBar) itemView.findViewById(R.id.shop_frrate);
        feedbackUser = (TextView) itemView.findViewById(R.id.shop_fruser);
        feedbackText = (TextView) itemView.findViewById(R.id.shop_frtext);
        feedbackCom = (TextView) itemView.findViewById(R.id.shop_frcomment);
        feedbackDate = (TextView) itemView.findViewById(R.id.shop_fdate);
        feedbackTime = (TextView) itemView.findViewById(R.id.shop_ftime);

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
