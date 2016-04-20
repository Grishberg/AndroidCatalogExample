package com.grishberg.yandextest.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.grishberg.yandextest.App;
import com.grishberg.yandextest.R;
import com.grishberg.yandextest.framework.ui.activity.BaseActivity;
import com.grishberg.yandextest.ui.fragment.FeedDetailFragment;
import com.grishberg.yandextest.ui.fragment.FeedListFragment;

public class MainActivity extends BaseActivity
        implements FeedListFragment.OnFeedFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FeedListFragment feedListFragment;
    private FeedDetailFragment feedDetailFragment;
    private View logoFrame;
    private boolean wasSaveInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoFrame = findViewById(R.id.llLogoFrame);
        wasSaveInstanceState = savedInstanceState != null;
        if (savedInstanceState == null) {
            feedListFragment = FeedListFragment.newInstance();
            if (App.isInitiated()) {
                initFragments();
            } else {
                showLogo();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        wasSaveInstanceState = true;
    }


    /**
     * Скрыть логотип инициализации
     */
    @Override
    protected void onInit() {
        Log.d(TAG, "onInit: ");
        logoFrame.setVisibility(View.GONE);
        initFragments();
    }

    private void initFragments() {
        Log.d(TAG, "initFragments: wasSaveInstanceState = " + wasSaveInstanceState);
        if (!feedListFragment.isAdded() && !wasSaveInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMain, feedListFragment)
                    .commit();
        }
    }

    @Override
    public void onFeedSelected(long feedId) {
        if (feedDetailFragment == null) {
            feedDetailFragment = FeedDetailFragment.newInstance();
        }
        Log.d(TAG, "onFeedSelected: id = " + feedId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentMain, feedDetailFragment)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        feedDetailFragment.updateData(feedId);
    }

    /**
     * Отобразить логотип инициализации
     */
    private void showLogo() {
        logoFrame.setVisibility(View.VISIBLE);
    }
}
