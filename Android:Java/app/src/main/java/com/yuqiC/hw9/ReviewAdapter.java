package com.yuqiC.hw9;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context mContext;
    JSONArray mReview;

    public ReviewAdapter(Context context, JSONArray reviews){
        mContext = context;
        mReview = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        try {
            holder.tvUsername.setText(mReview.getJSONObject(position).getJSONObject("user").getString("name"));
            holder.tvRating.setText(mReview.getJSONObject(position).getString("rating") + "/5");
            holder.tvContent.setText(mReview.getJSONObject(position).getString("text"));
            holder.tvDate.setText(mReview.getJSONObject(position).getString("time_created"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername, tvRating, tvContent, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.review_username);
            tvRating = itemView.findViewById(R.id.review_rating);
            tvContent = itemView.findViewById(R.id.review_content);
            tvDate = itemView.findViewById(R.id.review_date);
        }
    }
}
