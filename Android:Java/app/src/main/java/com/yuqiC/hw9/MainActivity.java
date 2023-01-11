package com.yuqiC.hw9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button mBtnSubmit, mBtnClear;
    EditText etDistance, etLocation;
    AutoCompleteTextView etKeyword;
    Spinner spCategory;
    CheckBox chLocation;
    String lat, lgt;
    RecyclerView rvBusiness;
    TextView tvNoBusiness;
    LinearLayout myLinearLayout;

    boolean isAllFieldsChecked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Hw9);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        // providing title for the ActionBar
        actionBar.setTitle("Yelp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        rvBusiness = findViewById(R.id.rvBusiness);
        tvNoBusiness = findViewById(R.id.noBusiness);
        myLinearLayout = findViewById(R.id.myLinear);

        myLinearLayout.setVisibility(View.GONE);
        tvNoBusiness.setVisibility(View.GONE);
        // TextFields
        etKeyword = findViewById(R.id.keyword);
        etDistance = findViewById(R.id.distance);
        etLocation = findViewById(R.id.location);

        // Checkbox
        chLocation = (CheckBox) findViewById(R.id.chkBox);

        // spinner
        spCategory = this.<Spinner>findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
        spCategory.setSelection(0);

        // buttons
        mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnClear = findViewById(R.id.btn_clear);

        // AutoComplete
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0) return;
                String url_autocomplete =  "https://cs571hw8node.wl.r.appspot.com/autocomplete?text=" + charSequence.toString();
                StringRequest myRequest = new StringRequest(url_autocomplete,
                        response -> {
                            try {
                                JSONArray myJsonArray = new JSONArray(response);
                                List<String> myList = new ArrayList<String>();
                                for(int j = 0; j < myJsonArray.length(); j++){
                                    myList.add(myJsonArray.getString(j));
                                }
                                int size = myList.size();
                                String[] myStringArray = myList.toArray(new String[size]);
                                ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(
                                        getApplicationContext(), android.R.layout.select_dialog_item, myStringArray);

                                etKeyword.setThreshold(1);
                                etKeyword.setAdapter(autoAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> {
                            Log.d("error", volleyError.getMessage());
                        }
                );
                RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
                volleyQueue.add(myRequest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        // Checkbox
        chLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chLocation.isChecked()){
                    etLocation.getText().clear();
                    etLocation.setVisibility(View.INVISIBLE);
                }else{
                    etLocation.setVisibility(View.VISIBLE);
                }
            }
        });

        // Submit
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked = CheckAllFields();

                if(isAllFieldsChecked) {
                    if(chLocation.isChecked()){
                        Log.d("auto", "auto");
                        getLocByAuto();
                    }else{
                        Log.d("test", "input");
                        getLocByInput();
                    }
                }

            }
        });

        // Clear
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeyword.getText().clear();
                etDistance.getText().clear();
                etLocation.getText().clear();
                chLocation.setChecked(false);
                etLocation.setVisibility(View.VISIBLE);
                myLinearLayout.setVisibility(View.GONE);
                tvNoBusiness.setVisibility(View.GONE);
            }
        });
    }

    private void getLocByInput() {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyA15LHOXZAKVDceS3qIuHnbn0MyyCDaiDM", etLocation.getText().toString());
        StringRequest myRequest = new StringRequest(url,
            response -> {
                try {
                    JSONObject myJsonObject = new JSONObject(response);
                    double nlat = myJsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    double nlgt = myJsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    lat = Double.toString(nlat);
                    lgt = Double.toString(nlgt);
                    getBusinesses();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            volleyError -> {
                Log.d("error", volleyError.getMessage());
            }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
        volleyQueue.add(myRequest);
    }

    private void getLocByAuto() {
        String url = "https://ipinfo.io/json?token=c5a96995ca9e33";
        StringRequest myRequest = new StringRequest(url,
            response -> {
                try {
                    JSONObject myJsonObject = new JSONObject(response);
                    String[] loc = myJsonObject.getString("loc").split(",");
                    lat = loc[0];
                    lgt = loc[1];
                    getBusinesses();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            volleyError -> {
                Log.d("error", volleyError.getMessage());
            }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
        volleyQueue.add(myRequest);
    }

    private boolean CheckAllFields() {

        if(etKeyword.length() == 0){
            etKeyword.setError("This field is required");
            return false;
        }

        if(etDistance.length() == 0){
            etDistance.setError("This field is required");
            return false;
        }

        if(etDistance.length() == 0){
            etDistance.setError("This field is required");
            return false;
        }

        if(!chLocation.isChecked()){
            if(etLocation.length() == 0){
                etLocation.setError("This field is required");
                return false;
            }
        }

        return true;

    }

    private void getBusinesses() {
        String url = String.format("https://cs571hw8node.wl.r.appspot.com/search?keyword=%s&lat=%s&lgt=%s&category=%s&distance=%s", etKeyword.getText().toString(), lat, lgt, spCategory.getSelectedItem().toString(),etDistance.getText().toString());
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        if(response.length() == 2){
                            tvNoBusiness.setVisibility(View.VISIBLE);
                            myLinearLayout.setVisibility(View.GONE);
                            return;
                        }
                        myLinearLayout.setVisibility(View.VISIBLE);
                        tvNoBusiness.setVisibility(View.GONE);
                        JSONArray myJsonArray = new JSONArray(response);
                        BusinessAdapter adapter = new BusinessAdapter(this, myJsonArray, new BusinessAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(int pos) {
                                try {
                                    String id = myJsonArray.getJSONObject(pos).getString("id");
                                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        rvBusiness.setAdapter(adapter);
                        rvBusiness.setLayoutManager(new LinearLayoutManager(this));
                        rvBusiness.addItemDecoration(new Cutting());
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
        volleyQueue.add(myRequest);
    }

    class Cutting extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0, 4);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case R.id.reserve:
                Intent intent = new Intent(this, ReservationActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}