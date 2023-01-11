package com.yuqiC.hw9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.json.JSONObject;

public class ViewPagerAdapter extends FragmentStateAdapter {

    String mId;
    JSONObject mDetails;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String id, JSONObject details){
        super(fragmentActivity);
        mId = id;
        mDetails = details;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return fragment_location.newInstance(mDetails);
            case 2:
                return fragment_reviews.newInstance(mId);
            default:
                return fragment_details.newInstance(mDetails);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
