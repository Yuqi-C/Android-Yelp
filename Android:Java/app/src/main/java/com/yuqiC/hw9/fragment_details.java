package com.yuqiC.hw9;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_details extends Fragment {

    String mName, mAddress, mPrice, mPhone, mCategory, mLink, mPhoto1, mPhoto2, mPhoto3;
    Boolean mStatus;
    ImageView ivPhoto1, ivPhoto2, ivPhoto3;
    TextView tvAddress, tvPrice, tvPhone, tvStatus, tvCategory, tvLink;
    Button btReserve;


    public static fragment_details newInstance(JSONObject details) {
        fragment_details fragment = new fragment_details();
        Bundle args = new Bundle();

        try {
            String name = details.getString("name");
            JSONArray display_address = details.getJSONObject("location").getJSONArray("display_address");
            String address = "";
            for(int i = 0; i < display_address.length(); i++){
                address += display_address.getString(i);
                if(i < display_address.length()-1) address += " ";
            }
            String price = details.getString("price");
            String phone = details.getString("display_phone");
            Boolean status = details.getJSONArray("hours").getJSONObject(0).getBoolean("is_open_now");
            String category = "";
            JSONArray categories = details.getJSONArray("categories");
            for(int i = 0; i < categories.length(); i++){
                category += categories.getJSONObject(i).getString("title");
                if(i < categories.length()-1) category += " | ";
            }
            String link = details.getString("url");
            JSONArray photos = details.getJSONArray("photos");
            String[] pictures = new String[photos.length()];
            for(int i = 0; i < photos.length(); i++){
                pictures[i] = photos.getString(i);
            }

            args.putString("name", name);
            args.putString("address", address);
            args.putString("price", price);
            args.putString("phone", phone);
            args.putBoolean("status", status);
            args.putString("category", category);
            args.putString("link", link);
            args.putStringArray("photos", pictures);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString("name");
            mAddress = getArguments().getString("address");
            mPrice = getArguments().getString("price");
            mPhone = getArguments().getString("phone");
            mStatus = getArguments().getBoolean("status");
            mCategory = getArguments().getString("category");
            mLink = getArguments().getString("link");
            String[] tmp = getArguments().getStringArray("photos");
            mPhoto1 = tmp[0];
            mPhoto2 = tmp[1];
            mPhoto3 = tmp[2];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAddress = view.findViewById(R.id.address);
        tvPrice = view.findViewById(R.id.price);
        tvPhone = view.findViewById(R.id.phone);
        tvStatus = view.findViewById(R.id.status);
        tvCategory = view.findViewById(R.id.category);
        tvLink = view.findViewById(R.id.link);
        ivPhoto1 = view.findViewById(R.id.photo1);
        ivPhoto2 = view.findViewById(R.id.photo2);
        ivPhoto3 = view.findViewById(R.id.photo3);

        tvAddress.setText(mAddress);
        tvPrice.setText(mPrice);
        tvPhone.setText(mPhone);
        tvCategory.setText(mCategory);
        String linkedText = String.format("<a href=\"%s\">Business Link</a>", mLink);
        tvLink.setText(Html.fromHtml(linkedText, HtmlCompat.FROM_HTML_MODE_LEGACY));
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());

        if(mStatus == true){
            tvStatus.setText("Open Now");
            tvStatus.setTextColor(getResources().getColor(R.color.green));
        }else{
            tvStatus.setText("Closed");
            tvStatus.setTextColor(getResources().getColor(R.color.red));
        }

        Glide.with(getActivity()).load(mPhoto1).into(ivPhoto1);
        Glide.with(getActivity()).load(mPhoto2).into(ivPhoto2);
        Glide.with(getActivity()).load(mPhoto3).into(ivPhoto3);

        btReserve = view.findViewById(R.id.reserve);
        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReservationDialog reservationDialog = new ReservationDialog(getActivity());
                reservationDialog.setTitle(mName);
                reservationDialog.setmSubmit(new ReservationDialog.IOnSubmitListener() {
                    @Override
                    public void onSubmit(ReservationDialog dialog) {

                    }
                });
                reservationDialog.setmCancel(new ReservationDialog.IOnCancelListener() {
                    @Override
                    public void onCancel(ReservationDialog dialog) {

                    }
                });
                reservationDialog.show();
            }
        });
    }
}