package com.example.automoto.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Interface.ItemClickListner;
import com.example.automoto.R;

public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView feedbackUser, feedbackCom, feedbackDate, feedbackTime, feedbackText;
    public RatingBar feedbackRating;
    public ImageView userImg;
    public ItemClickListner listner;


    public FeedbackViewHolder(View itemView)
    {
        super(itemView);

        userImg = (ImageView) itemView.findViewById(R.id.riders_fimg);
        feedbackRating = (RatingBar) itemView.findViewById(R.id.riders_frate);
        feedbackUser = (TextView) itemView.findViewById(R.id.fr_fuser);
        feedbackText = (TextView) itemView.findViewById(R.id.fr_ftext);
        feedbackCom = (TextView) itemView.findViewById(R.id.rider_fcomment);
        feedbackDate = (TextView) itemView.findViewById(R.id.riders_fdate);
        feedbackTime = (TextView) itemView.findViewById(R.id.riders_ftime);

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
