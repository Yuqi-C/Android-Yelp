package com.yuqiC.hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Set;

public class ReservationActivity extends AppCompatActivity {

    public Set<String> titles;
    RecyclerView rvReservation;
    TextView tvNotFound;
    ReservationAdapter adapter;
    LinearLayout mLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Hw9);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Yelp");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        rvReservation = findViewById(R.id.rvReservation);
        tvNotFound = findViewById(R.id.notFound);
        mLinearlayout = findViewById(R.id.reservationLayout);
        tvNotFound.setVisibility(View.VISIBLE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if(pref.contains("all_titles")) {
            titles = pref.getStringSet("all_titles", null);
            if(titles.size() != 0){
                tvNotFound.setVisibility(View.GONE);
                adapter = new ReservationAdapter(this, titles);
                rvReservation.setAdapter(adapter);
                rvReservation.setLayoutManager(new LinearLayoutManager(this));
                enableSwipeToDelete();
            }
        }
    }

    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.third, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(mLinearlayout, "Removing Existing reservation", Snackbar.LENGTH_LONG);

                snackbar.show();
                if(adapter.isEmpty()){
                    tvNotFound.setVisibility(View.VISIBLE);
                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvReservation);
    }
}