package com.example.automoto.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automoto.Model.Services;
import com.example.automoto.R;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {


    Context context;
    ArrayList<Services> servicelist;

    public ServicesAdapter(Context context, ArrayList<Services> list) {
        this.context = context;
        this.servicelist = list;

    }
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Services services = servicelist.get(position);
        holder.servname.setText(services.getService());
        holder.servdet.setText(services.getDetails());
        Picasso.get().load(services.getServiceimage()).into(holder.serviceimg);
        holder.status1.setText(services.getStatus());

        if (services.getStatus().equals("Not Available")){

            holder.not.setVisibility(View.VISIBLE);
            holder.avail.setVisibility(View.GONE);
            holder.status1.setTextColor(Color.RED);


        }else if (services.getStatus().equals("Available")){

            holder.not.setVisibility(View.GONE);
            holder.avail.setVisibility(View.VISIBLE);
            holder.status1.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return servicelist.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        public TextView servdet, servname;
        public ImageView serviceimg;
        public TextView status1;
        public View not, avail;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            servname = (TextView) itemView.findViewById(R.id.serv_name);
            servdet = (TextView) itemView.findViewById(R.id.serv_details);
            serviceimg = (ImageView) itemView.findViewById(R.id.serv_image);
            status1 = (TextView) itemView.findViewById(R.id.statusa);
            not = (View) itemView.findViewById(R.id.minus);
            avail = (View) itemView.findViewById(R.id.check);
        }
    }




}


