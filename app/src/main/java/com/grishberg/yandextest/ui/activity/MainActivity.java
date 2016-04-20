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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoFrame = findViewById(R.id.llLogoFrame);

        if(App.isInitiated()) {
            initFragments();
        } else {
            showLogo();
        }
    }

    /**
     * Скрыть логотип инициализации
     */
    @Override
    protected void onInit() {
        logoFrame.setVisibility(View.GONE);
        initFragments();
    }

    private void initFragments(){
        feedListFragment = FeedListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentMain, feedListFragment)
                .commit();
    }

    @Override
    public void onFeedSelected(long feedId) {
        if(feedDetailFragment == null){
            feedDetailFragment = FeedDetailFragment.newInstance(feedId);
        }
        Log.d(TAG, "onFeedSelected: id = " + feedId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentMain, feedDetailFragment)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Отобразить логотип инициализации
     */
    private void showLogo() {
        logoFrame.setVisibility(View.VISIBLE);
    }
}
