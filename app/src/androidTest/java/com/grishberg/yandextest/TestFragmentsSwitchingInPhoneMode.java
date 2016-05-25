package com.grishberg.yandextest;

import com.grishberg.yandextest.ui.activity.MainActivity;
import com.grishberg.yandextest.ui.fragment.FeedDetailFragment;
import com.grishberg.yandextest.ui.fragment.FeedListFragment;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class TestFragmentsSwitchingInPhoneMode extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private static final int TIMEOUT = 20000;
    private static final int SLEEP = 1000;


    public TestFragmentsSwitchingInPhoneMode() {
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
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));

        //Клик на первом элементе
        solo.clickOnView(solo.getView(R.id.cvRoot));

        // Ожидаем фрагмент FeedDetailFragment
        assertTrue("FeedDetailFragment not found",
                solo.waitForFragmentByTag(FeedDetailFragment.class.getSimpleName(), TIMEOUT));
        // Ожидаем жанр rock
        assertTrue("rock genre not found",
                solo.searchText("rock"));

        // Нажимаем назад-
        solo.goBack();

        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found after back pressed",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));

        // Клик на втором элементе
        solo.clickOnView(solo.getView(R.id.cvRoot, 1));

        // Ожидаем фрагмент FeedDetailFragment
        assertTrue("FeedDetailFragment not found after rv item click",
                solo.waitForFragmentByTag(FeedDetailFragment.class.getSimpleName(), TIMEOUT));

        // Ожидаем жанр - pop
        assertTrue("pop genre not found",
                solo.searchText("pop"));

        // Нажимаем назад
        solo.goBack();

        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found after back pressed",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));


    }

    /**
     * Тест корретного отображения фрагментов при поворотах экрана
     */
    public void testRotateScreen() {
        //Ожидаем отображение главной активности
        assertTrue("MainActivity did not correctly load",
                solo.waitForActivity(MainActivity.class, TIMEOUT));

        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found after rotate screen",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));
        solo.sleep(SLEEP);

        // Поворот экрана в ландшафтный режим
        solo.setActivityOrientation(Solo.LANDSCAPE);

        //Ожидаем отображение главной активности
        assertTrue("MainActivity did not correctly load after first LANDSCAPE rotation.",
                solo.waitForActivity(MainActivity.class, TIMEOUT));
        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found after rotate screen",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));
        solo.sleep(SLEEP);

        // Поворот экрана в исходное состояние
        solo.setActivityOrientation(Solo.PORTRAIT);

        //Ожидаем отображение главной активности
        assertTrue("MainActivity did not correctly load after second PORTRAIT rotation.",
                solo.waitForActivity(MainActivity.class, TIMEOUT));
        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedListFragment not found after rotate screen",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName(), TIMEOUT));
        solo.sleep(SLEEP);

        //   2я часть
        // Клик на первом элементе
        solo.clickOnView(solo.getView(R.id.cvRoot));

        // Ожидаем фрагмент FeedDetailFragment
        assertTrue("FeedDetailFragment not found",
                solo.waitForFragmentByTag(FeedDetailFragment.class.getSimpleName(), TIMEOUT));

        // Поворот экрана в ландшафтный режим
        solo.setActivityOrientation(Solo.LANDSCAPE);

        // Ожидаем фрагмент FeedListFragment
        assertTrue("FeedDetailFragment not found",
                solo.waitForFragmentByTag(FeedDetailFragment.class.getSimpleName(), TIMEOUT));

        // Ожидаем фрагмент FeedDetailFragment и отсутствие FeedListFragment
        assertFalse("FeedListFragment found in detail mode",
                solo.searchText("pop"));

        // Нажимаем назад
        solo.goBack();

        assertTrue("FeedListFragment not found after back pressed",
                solo.waitForFragmentByTag(FeedListFragment.class.getSimpleName()));
    }
}