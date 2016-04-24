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
    public static final String FEED_ID = "FEED_ID";
    private FeedListFragment feedListFragment;
    private FeedDetailFragment feedDetailFragment;
    private View logoFrame;
    private View contentMain;
    /**
     * Флаг нужен на тот случай, если после начала анимации приложение скроется и
     * произойдет вызов функции отображения фрагмента
     */
    private long feedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: savedInstanceState = " + savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentMain = findViewById(R.id.contentMain);
        if (!isInTwoPaneMode()) {
            // если режим отображения для смартфона
            if (savedInstanceState != null) {
                feedId = savedInstanceState.getLong(FEED_ID);
            } else {
                initListFragment();
            }
        }  else {
            // иначе - взять ссылки на фрагменты для дальнейшего использования
            feedListFragment = (FeedListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.feedFragment);
            feedDetailFragment = (FeedDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);
        }
        logoFrame = findViewById(R.id.llLogoFrame);

        showLogo();
    }

    /**
     * Проверка режима отображения для планшета (если есть
     * @return
     */
    private boolean isInTwoPaneMode() {

        return contentMain == null;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putLong(FEED_ID, feedId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (feedId > 0 && !isInTwoPaneMode()) {
            onFeedSelected(feedId);
        }
    }

    /**
     * Скрыть логотип инициализации
     */
    @Override
    protected void onInit() {
        Log.d(TAG, "onInit: ");
        logoFrame.setVisibility(View.GONE);
        if (!isInTwoPaneMode()) {
            initListFragment();
        }
    }

    /**
     * Событие во время выбора элемента списка
     * @param feedId
     */
    @Override
    public void onFeedSelected(long feedId) {
        if (!isInTwoPaneMode()) {
            feedDetailFragment = (FeedDetailFragment) getSupportFragmentManager()
                        .findFragmentByTag(FeedDetailFragment.class.getSimpleName());
            if(feedDetailFragment == null){
                feedDetailFragment = FeedDetailFragment.newInstance();
            }

            if (!feedDetailFragment.isAdded()) {
                Log.d(TAG, "onFeedSelected: id = " + feedId);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMain, feedDetailFragment,
                                FeedDetailFragment.class.getSimpleName())
                        .addToBackStack(null)
                        // если использовать commit, то может возникнуть IllegalStateException во время
                        // попытки сделать коммит после onSavedInstanceState
                        .commitAllowingStateLoss();
                getSupportFragmentManager().executePendingTransactions();

            }
        }
        feedDetailFragment.updateData(feedId);
        this.feedId = 0;
    }

    /**
     * Отобразить фрагмент со списком
     */
    private void initListFragment() {
        if (!App.isInitiated() ) {
            Log.d(TAG, "initListFragment: not initiated");
            return;
        }
        feedListFragment = (FeedListFragment) getSupportFragmentManager()
                .findFragmentByTag(FeedListFragment.class.getSimpleName());
        if (feedListFragment == null) {
            feedListFragment = FeedListFragment.newInstance(true);
        }
        if (!feedListFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMain, feedListFragment, FeedListFragment.class.getSimpleName())
                    .commit();
            feedId = 0;
        }
    }

    /**
     * Отобразить логотип инициализации
     */
    private void showLogo() {
        if (App.isInitiated()) return;
        logoFrame.setVisibility(View.VISIBLE);
    }
}
