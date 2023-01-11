package com.yuqiC.hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class DetailActivity extends AppCompatActivity {

    String[] title = {"BUSINESS DETAILS", "MAP LOCATION", "REVIEWS"};
    String url_facebook, url_twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Hw9);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);

        String url = String.format("https://cs571hw8node.wl.r.appspot.com/searchId/%s", id);
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONObject details = new JSONObject(response);
                        String name = details.getString("name");
                        String link = details.getString("url");
                        actionBar.setTitle(name);
                        url_twitter = "https://twitter.com/compose/tweet?text=Check%20" + name + "%20on%20Yelp%21%0A" + link;
                        url_facebook =  "https://www.facebook.com/sharer/sharer.php?u=" + link;

                        ViewPagerAdapter adapter = new ViewPagerAdapter(this, id, details);
                        viewPager2.setAdapter(adapter);

                        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override
                            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                tab.setText(title[position]);
                            }
                        }).attach();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(DetailActivity.this);
        volleyQueue.add(myRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.icon_facebook:
                Uri uri_fb = Uri.parse(url_facebook);
                Intent intent_fb = new Intent(Intent.ACTION_VIEW, uri_fb);
                startActivity(intent_fb);
                break;
            case R.id.icon_twitter:
                Uri uri_tw = Uri.parse(url_twitter);
                Intent intent_tw = new Intent(Intent.ACTION_VIEW, uri_tw);
                startActivity(intent_tw);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}