package com.grishberg.yandextest.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grishberg.yandextest.R;
import com.grishberg.yandextest.data.db.FeedDao;
import com.grishberg.yandextest.data.model.FeedContainer;
import com.grishberg.yandextest.common.db.DataReceiveObserver;
import com.grishberg.yandextest.common.db.SingleResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FeedDetailFragment extends Fragment implements DataReceiveObserver {
    private static final String TAG = FeedDetailFragment.class.getSimpleName();
    public static final String FEED_ID_KEY = "FEED_ID_KEY";
    public static final int ANIMATION_DURATION = 1000;
    private FeedDao feedDao;
    private ImageLoader imageLoader;
    private ImageView ivAvatar;
    private ProgressBar pbLoading;
    private TextView tvGenres;
    private TextView tvInfo;
    private TextView tvDescription;
    private TextView tvBiography;
    private SingleResult<FeedContainer> feedResult;
    private DisplayImageOptions options;
    private long feedId;
    private Animation fadeIn;

    public FeedDetailFragment() {
        // Required empty public constructor
    }

    public static FeedDetailFragment newInstance() {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        feedDao = new FeedDao();
        setRetainInstance(true);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_broken)
                .showImageOnFail(R.drawable.ic_broken)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
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
        tvBiography = (TextView) view.findViewById(R.id.tvBiography);
        if (savedInstanceState != null) {
            feedId = savedInstanceState.getLong(FEED_ID_KEY);
            if (feedId > 0) {
                updateData(feedId);
            }
        }
    }

    /**
     * Во время поворота сохраняем ИД выбранного элемента
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(FEED_ID_KEY, feedId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (feedResult != null) {
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
    private void populateWidgets() {
        FeedContainer item = feedResult.getItem();
        if (item == null) {
            Log.e(TAG, "populateWidgets: item == null");
            return;
        }
        if (getActivity() != null) {
            getActivity().setTitle(item.getName());
        }
        // загрузка картинки
        imageLoader.displayImage(item.getCoverBig(), ivAvatar, options,
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
                        ivAvatar.startAnimation(fadeIn);
                    }
                });
        // заполнение элементов
        tvDescription.setText(item.getDescription());
        tvGenres.setText(item.getGenres());
        tvInfo.setText(String.format(
                getContext().getString(R.string.feed_cell_info), item.getAlbums(), item.getTracks()));
        //анимация
        Animation moveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        tvBiography.setVisibility(View.VISIBLE);
        tvInfo.startAnimation(moveAnimation);
        tvBiography.startAnimation(moveAnimation);
        tvGenres.startAnimation(moveAnimation);
        tvDescription.startAnimation(moveAnimation);
    }

    /**
     * Обновить данные во врогменте для выбранного feedId
     *
     * @param feedId
     */
    public void updateData(long feedId) {
        this.feedId = feedId;
        feedResult = feedDao.getFeed(feedId);
        feedResult.addDataReceiveObserver(this);
        if (feedResult.isLoaded()) {
            Log.d(TAG, "onViewCreated: loaded immediately");
            populateWidgets();
        }
    }
}
