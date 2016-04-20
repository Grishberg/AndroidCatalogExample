package com.grishberg.yandextest.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.grishberg.yandextest.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FeedDetailFragment extends Fragment {
    private static final String ARG_FEED_ID = "param1";
    private static final String TAG = FeedDetailFragment.class.getSimpleName();

    private ImageLoader imageLoader;
    private ImageView ivAvatar;
    private ProgressBar pbLoading;

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
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAvatar = (ImageView) view.findViewById(R.id.ivDetailFeedAvatar);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbDetailLoading);
        //TODO: make async loading item from DB
        /*
        imageLoader.displayImage(item.getCoverSmall(), holder.ivAvatar, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        Log.d(TAG, "onLoadingStarted: ");
                        holder.ivAvatar.setVisibility(View.INVISIBLE);
                        holder.pbLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.d(TAG, "onLoadingFailed: ");
                        holder.ivAvatar.setVisibility(View.INVISIBLE);
                        holder.pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.d(TAG, "onLoadingComplete: ");
                        holder.ivAvatar.setVisibility(View.VISIBLE);
                        holder.pbLoading.setVisibility(View.GONE);
                    }
                });
                */
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
