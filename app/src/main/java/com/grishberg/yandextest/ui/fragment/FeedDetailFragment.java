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
import android.widget.TextView;

import com.grishberg.yandextest.R;
import com.grishberg.yandextest.data.db.FeedDao;
import com.grishberg.yandextest.data.model.FeedContainer;
import com.grishberg.yandextest.framework.db.DataReceiveObserver;
import com.grishberg.yandextest.framework.db.SingleResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FeedDetailFragment extends Fragment implements DataReceiveObserver{
    private static final String ARG_FEED_ID = "param1";
    private static final String TAG = FeedDetailFragment.class.getSimpleName();

    private FeedDao feedDao;
    private ImageLoader imageLoader;
    private ImageView ivAvatar;
    private ProgressBar pbLoading;
    private TextView tvGenres;
    private TextView tvInfo;
    private TextView tvDescription;
    private SingleResult<FeedContainer> feedResult;
    /**
     * идентификатор выбранного фида
     */
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
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            feedId = getArguments().getLong(ARG_FEED_ID);
        }
        feedDao = new FeedDao();
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
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        ivAvatar = (ImageView) view.findViewById(R.id.ivDetailFeedAvatar);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbDetailLoading);
        tvGenres = (TextView) view.findViewById(R.id.tvGenre);
        tvInfo = (TextView) view.findViewById(R.id.tvFeedInfo);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(feedResult != null){
            feedResult.removeDataReceiveObserver(this);
        }
    }

    /**
     * Событие когда данные стали доступны
     */
    @Override
    public void onDataReceived() {
        Log.d(TAG, "onDataReceived: ");
        populateWidgets();
    }

    /**
     * Заполнить поля экрана
     */
    private void populateWidgets(){
        // загрузка картинки
        FeedContainer item = feedResult.getItem();
        imageLoader.displayImage(item.getCoverBig(), ivAvatar,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        Log.d(TAG, "onLoadingStarted: ");
                        ivAvatar.setVisibility(View.INVISIBLE);
                        pbLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.d(TAG, "onLoadingFailed: ");
                        ivAvatar.setVisibility(View.INVISIBLE);
                        pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.d(TAG, "onLoadingComplete: ");
                        ivAvatar.setVisibility(View.VISIBLE);
                        pbLoading.setVisibility(View.GONE);
                    }
                });
        // заполнение элементов
        tvDescription.setText(item.getDescription());
        tvGenres.setText(item.getGenres());
        tvInfo.setText(String.format(
                getContext().getString(R.string.feed_cell_info), item.getAlbums() , item.getTracks()));

    }

    public void updateData(long feedId){
        feedResult = feedDao.getFeed(feedId);
        feedResult.addDataReceiveObserver(this);
        if(feedResult.isLoaded()){
            Log.d(TAG, "onViewCreated: loaded immediately");
            populateWidgets();
        }
    }
}
