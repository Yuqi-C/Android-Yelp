package com.yuqiC.hw9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_location extends Fragment implements OnMapReadyCallback{

    Double mLat, mLgt;

    private GoogleMap mMap;

    public static fragment_location newInstance(JSONObject detail) {
        fragment_location fragment = new fragment_location();
        Bundle args = new Bundle();

        try {
            JSONObject location = detail.getJSONObject("coordinates");
            args.putDouble("lat", Double.valueOf(location.getString("latitude")));
            args.putDouble("lgt", Double.valueOf(location.getString("longitude")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLat = getArguments().getDouble("lat");
            mLgt = getArguments().getDouble("lgt");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(mLat, mLgt);
        mMap.addMarker(new MarkerOptions()
                .position(loc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
    }

}