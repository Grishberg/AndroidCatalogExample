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
    public static final String WAS_INITIATED = "WAS_INITIATED";
    private FeedListFragment feedListFragment = FeedListFragment.newInstance();
    private FeedDetailFragment feedDetailFragment = FeedDetailFragment.newInstance();
    private View logoFrame;
    // новая студия запускает приложения в закрытом состоянии с вызовом onSaveInstanceState
    // данный флаг учитывает, был ли запуск в таком режиме и не позволяет проинитить фрагмент
    private boolean wasSaveInstanceState;
    private boolean wasFirstFragmentInitiated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wasFirstFragmentInitiated = App.isInitiated();
        logoFrame = findViewById(R.id.llLogoFrame);
        if (savedInstanceState != null) {
            wasFirstFragmentInitiated = savedInstanceState.getBoolean(WAS_INITIATED);
        }
        showLogo();
        initFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_INITIATED, wasFirstFragmentInitiated);
        wasSaveInstanceState = true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        wasSaveInstanceState = false;
        initFragments();
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
        if (!wasSaveInstanceState && !wasFirstFragmentInitiated && App.isInitiated()) {
            wasFirstFragmentInitiated = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMain, feedListFragment)
                    .commit();
        }
    }

    @Override
    public void onFeedSelected(long feedId) {
        if (feedDetailFragment == null) {

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
        if (App.isInitiated()) return;
        logoFrame.setVisibility(View.VISIBLE);
    }
}
