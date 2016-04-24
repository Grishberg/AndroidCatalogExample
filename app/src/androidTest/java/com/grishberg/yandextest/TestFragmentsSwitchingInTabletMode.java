package com.grishberg.yandextest;

/**
 * Created by grishberg on 24.04.16.
 */
import com.grishberg.yandextest.ui.activity.MainActivity;
import com.grishberg.yandextest.ui.fragment.FeedDetailFragment;
import com.grishberg.yandextest.ui.fragment.FeedListFragment;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class TestFragmentsSwitchingInTabletMode extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private static final int TIMEOUT = 20000;
    private static final int SLEEP = 1000;


    public TestFragmentsSwitchingInTabletMode() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testRun() {

        int delay = 2000;

        // ожидаем главную активность MainActivity
        assertTrue("MainActivity not found", solo.waitForActivity(
                MainActivity.class, delay));

        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found",
                solo.waitForFragmentById(R.id.feedFragment, TIMEOUT));

        //Клик на первом элементе
        solo.clickOnView(solo.getView(R.id.cvRoot));

        // Ожидаем фрагмент FeedDetailFragment
        assertTrue("FeedDetailFragment not found",
                solo.waitForFragmentById(R.id.detailFragment, TIMEOUT));
        // Ожидаем жанр rock
        assertTrue("rock genre not found",
                solo.searchText("rock"));

        // Клик на втором элементе
        solo.clickOnView(solo.getView(R.id.cvRoot, 1));

        // Ожидаем фрагмент FeedDetailFragment
        assertTrue("FeedDetailFragment not found after rv item click",
                solo.waitForFragmentById(R.id.detailFragment, TIMEOUT));

        // Ожидаем жанр - pop
        assertTrue("pop genre not found",
                solo.searchText("pop"));

    }

}