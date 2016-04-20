package com.grishberg.yandextest.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grishberg.yandextest.R;

public class FeedDetailFragment extends Fragment {
    private static final String ARG_FEED_ID = "param1";

    // TODO: Rename and change types of parameters
    private long feedId;


    public FeedDetailFragment() {
        // Required empty public constructor
    }

    public static FeedDetailFragment newInstance(long feedId) {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FEED_ID, feedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            feedId = getArguments().getLong(ARG_FEED_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_detail, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFeedFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
