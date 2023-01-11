package com.yuqiC.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    public Context mContext;
    List<String> titles;

    public ReservationAdapter(Context context, Set<String> info){
        mContext = context;
        titles = new ArrayList<>(info);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View reservationView = inflater.inflate(R.layout.item_reservation, parent, false);
        ViewHolder viewHolder = new ViewHolder(reservationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = titles.get(position);
        Log.d("title", title);
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        String information = pref.getString(title, null);
        Log.d("information", information);
        String[] row = information.split(",");

        holder.tvNumber.setText(Integer.toString(position+1));
        holder.tvName.setText(title);
        holder.tvDate.setText(row[0]);
        holder.tvTime.setText(row[1]);
        holder.tvEmail.setText(row[2]);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNumber, tvName, tvDate, tvTime, tvEmail;

        public ViewHolder(View itemView){
            super(itemView);
            tvNumber = itemView.findViewById(R.id.reserved_num);
            tvName = itemView.findViewById(R.id.reserved_name);
            tvDate = itemView.findViewById(R.id.reserved_date);
            tvTime = itemView.findViewById(R.id.reserved_time);
            tvEmail = itemView.findViewById(R.id.reserved_email);
        }

    }

    public void removeItem(int position) {
        // remove data
        String title = titles.get(position);
        titles.remove(position);
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet("all_titles", new HashSet<>(titles));
        editor.remove(title);
        editor.commit();
        notifyItemRemoved(position);
    }

    public boolean isEmpty(){
        if(titles.size()==0){
            return true;
        }
        return false;
    }


}
