package com.yuqiC.hw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private JSONArray mBusinesses;
    private Context mContext;
    private OnItemClickListener mListener;

    public BusinessAdapter(Context context, JSONArray businesses, OnItemClickListener listener){
        mBusinesses = businesses;
        mContext = context;
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNum, tvName, tvRate, tvDistance;
        public ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.business_num);
            tvName = itemView.findViewById(R.id.business_name);
            tvRate = itemView.findViewById(R.id.business_rate);
            tvDistance = itemView.findViewById(R.id.business_distance);
            ivImg = itemView.findViewById(R.id.business_img);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View businessView = inflater.inflate(R.layout.item_business, parent, false);
        ViewHolder viewHolder = new ViewHolder(businessView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject business = mBusinesses.getJSONObject(position);
            holder.tvNum.setText(Integer.toString(position+1));
            holder.tvName.setText(business.getString("name"));
            holder.tvRate.setText(business.getString("rating"));
            Double dist = Double.valueOf(business.getString("distance")) / 1609.34;
            holder.tvDistance.setText(Integer.toString(dist.intValue()));
            Glide.with(mContext).load(business.getString("image_url")).into(holder.ivImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mListener.onClick(holder.getAdapterPosition());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }
}
