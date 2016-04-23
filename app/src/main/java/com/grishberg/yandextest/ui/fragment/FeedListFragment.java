package com.grishberg.yandextest.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.grishberg.yandextest.R;
import com.grishberg.yandextest.controllers.FeedAdapter;
import com.grishberg.yandextest.data.db.FeedDao;
import com.grishberg.yandextest.data.rest.RestService;
import com.grishberg.yandextest.data.rest.RestServiceHelper;
import com.grishberg.yandextest.framework.interfaces.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.paveldudka.util.FastBlur;

public class FeedListFragment extends Fragment implements OnItemClickListener {
    private static final String TAG = FeedListFragment.class.getSimpleName();
    private RecyclerView rvFeeds;
    private FeedAdapter feedAdapter;
    private FeedDao feedDao;
    private CardView cvDetailViewStub;
    private ImageView ivBlurBackground;
    private ImageView imageViewContainerForPreLoad;
    private OnFeedFragmentInteractionListener activityListener;
    private int screenHeight;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private FastBlur fastBlur;
    private boolean isAnimate;

    public FeedListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeedListFragment.
     */
    public static FeedListFragment newInstance() {
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fastBlur = new FastBlur();
        imageLoader = ImageLoader.getInstance();
        feedDao = new FeedDao();
        feedAdapter = new FeedAdapter(getContext(), feedDao.getFeeds(), this);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageViewContainerForPreLoad = new ImageView(getContext());
        RestServiceHelper.getFeeds(getContext(), new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Log.d(TAG, "onReceiveResult: " + resultCode);
                if (resultData.containsKey(RestService.ERROR_KEY)) {
                    Log.e(TAG, "onReceiveResult: Error");
                } else {
                    //обновить данные в адаптере
                    if (feedAdapter != null) {
                        feedAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        getActivity().setTitle(R.string.app_name);
        super.onViewCreated(view, savedInstanceState);
        cvDetailViewStub = (CardView) view.findViewById(R.id.cvRoot);
        cvDetailViewStub.setBottom(0);
        cvDetailViewStub.setTop(0);
        ivBlurBackground = (ImageView) view.findViewById(R.id.ivBlurBackground);
        //ivBlurBackground.setVisibility(View.GONE);
        // Инициализация recycler view
        rvFeeds = (RecyclerView) view.findViewById(R.id.rvFeeds);
        rvFeeds.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvFeeds.setLayoutManager(llm);
        rvFeeds.setAdapter(feedAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFeedFragmentInteractionListener) {
            activityListener = (OnFeedFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFeedFragmentInteractionListener");
        }
    }

    /**
     * Событие при клике на элемент списка
     * @param id идентификатор элемента в бд
     * @param offsetTop верхнее смещение в пикселях
     * @param offsetBottom нижнее смещение в пикселях
     * @param drawable ссылка на изображение
     * @param bigCoverUrl URL большого постера
     */
    @Override
    public void onItemClicked(final long id, final int offsetTop, final int offsetBottom,
                              Drawable drawable,
                              String bigCoverUrl) {
        if(isAnimate) return;
        isAnimate = true;
        int detailAvatarHeight = (int) getContext().getResources()
                .getDimension(R.dimen.feed_detail_image_height);
        int screenWidth = 0;
        if (getView() != null) {
            screenHeight = getView().getMeasuredHeight();
            screenWidth = getView().getMeasuredWidth();
        }
        startImagePreLoading(screenWidth, detailAvatarHeight, bigCoverUrl);
        // 1) размыть задний план
        blurBackground();
        // 2) отобразить заглушку на том же месте, что и выбранный элемент
        cvDetailViewStub.setVisibility(View.VISIBLE);
        //ivStubAvatar.setImageDrawable(drawable);

        Log.d(TAG, "onItemClicked: height = " + screenHeight);
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(1000);
        final int dTop = offsetTop;
        final int dBottom = screenHeight - offsetBottom;
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 3) анимация раскрытия
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = (float) animation.getAnimatedValue();
                int newTop = (int) (offsetTop - dTop * i);
                int newBottom = (int) (offsetBottom + dBottom * i);
                cvDetailViewStub.setTop(newTop);
                cvDetailViewStub.setBottom(newBottom);
            }
        });

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 4) вызвать каллбак и скрыть заглушку
                if (activityListener != null) {
                    activityListener.onFeedSelected(id);
                    isAnimate = false;
                }
            }
        });
        animation.start();
    }

    /**
     * Предзагрузка изображения во время анимации
     * если изображение не успеет загрузиться, хитрый UIL поймет это и дождется выполнения операции
     * вместо того что бы заново загружать.
     *
     * @param w   ширина будущего изображения
     * @param h   высота будущего изображения
     * @param url ссылка на изображение
     */
    private void startImagePreLoading(int w, int h, String url) {
        Log.d(TAG, String.format("startImagePreLoading: preloading url=%s", url));
        imageViewContainerForPreLoad.setLayoutParams(new ViewGroup.LayoutParams(w, h)); // define approximate image size needed for your ImageViews
        imageLoader.displayImage(url, imageViewContainerForPreLoad, options);
    }

    /**
     * Размытие заднего плана
     */
    private void blurBackground() {
        if (getView() == null) return;
        // делаем скриншот
        getView().setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(getView().getDrawingCache());
        getView().setDrawingCacheEnabled(false);
        // запускаем задачу размытия
        fastBlur.blur(getContext(), bitmap, ivBlurBackground);
        // отображаем размытый фон
        ivBlurBackground.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        activityListener = null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (fastBlur != null) {
            fastBlur.release();
            fastBlur = null;
        }
        if (feedDao != null) {
            feedDao.release();
            feedDao = null;
        }
        if (feedAdapter != null) {
            feedAdapter = null;
        }
    }

    public interface OnFeedFragmentInteractionListener {
        // Событие для активити при выборе элемента во фрагменте
        void onFeedSelected(long feedId);
    }
}
