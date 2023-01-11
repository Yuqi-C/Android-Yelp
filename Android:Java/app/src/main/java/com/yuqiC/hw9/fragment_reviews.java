package com.yuqiC.hw9;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_reviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_reviews extends Fragment {

    String id;
    private RecyclerView mRvReview;

    // TODO: Rename and change types and number of parameters
    public static fragment_reviews newInstance(String param) {
        fragment_reviews fragment = new fragment_reviews();
        Bundle args = new Bundle();
        args.putString("id", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url = String.format("https://cs571hw8node.wl.r.appspot.com/reviews/%s", id);
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONObject details = new JSONObject(response);
                        JSONArray mReview = details.getJSONArray("reviews");
                        mRvReview = view.findViewById(R.id.rv_review);
                        mRvReview.setLayoutManager(new LinearLayoutManager(getActivity()));

                        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
                        mRvReview.addItemDecoration(dividerItemDecoration);
                        mRvReview.setAdapter(new ReviewAdapter(getActivity(), mReview));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity());
        volleyQueue.add(myRequest);
    }

    public class DividerItemDecorator extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i <= childCount - 2; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }


}