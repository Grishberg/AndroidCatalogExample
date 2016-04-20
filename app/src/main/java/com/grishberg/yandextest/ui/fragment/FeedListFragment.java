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
import com.paveldudka.util.FastBlur;

public class FeedListFragment extends Fragment implements OnItemClickListener {
    private static final String TAG = FeedListFragment.class.getSimpleName();
    private RecyclerView rvFeeds;
    private FeedAdapter feedAdapter;
    private FeedDao feedDao;
    private CardView cvDetailViewStub;
    private ImageView ivStubAvatar;
    private ImageView ivBlurBackground;
    private OnFeedFragmentInteractionListener activityListener;
    private int screenHeight;

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
        feedDao = new FeedDao();
        feedAdapter = new FeedAdapter(getContext(), feedDao.getFeeds(), this);
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
        super.onViewCreated(view, savedInstanceState);
        cvDetailViewStub = (CardView) view.findViewById(R.id.cvRoot);
        ivStubAvatar = (ImageView) view.findViewById(R.id.ivFeedAvatar);
        ivBlurBackground = (ImageView) view.findViewById(R.id.ivBlurBackground);
        ivBlurBackground.setVisibility(View.GONE);
        // Инициализация recycler view
        rvFeeds = (RecyclerView) view.findViewById(R.id.rvFeeds);
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

    @Override
    public void onItemClicked(final long id, final int offsetTop, final int offsetBottom, Drawable drawable) {
        // 1) размыть задний план
        blurBackground();
        // 2) отобразить заглушку на том же месте, что и выбранный элемент
        cvDetailViewStub.setVisibility(View.VISIBLE);
        //ivStubAvatar.setImageDrawable(drawable);
        int screenWidth = 0;
        if (getView() != null) {
            screenHeight = getView().getMeasuredHeight();
            screenWidth = getView().getMeasuredWidth();
        }
        final int finalScreenWidth = screenWidth;
        Log.d(TAG, "onItemClicked: height = " + screenHeight);
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(1000);
        int detailAvatarHeight = (int) getContext().getResources()
                .getDimension(R.dimen.feed_detail_image_height);
        float k = detailAvatarHeight / (int) getContext().getResources().getDimension(R.dimen.feed_image_height);
        int detailAvatarWidth = (int) (getContext().getResources().getDimension(R.dimen.feed_image_width) * k);
        final int newAvatarMargin = (screenWidth - detailAvatarWidth) / 2;
        final int dTop = offsetTop;
        final int dBottom = screenHeight - offsetBottom;
/*
        final int imageLeft = ivStubAvatar.getLeft() + (int) getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);;
        final int imageRight = (int) getContext().getResources().getDimension(R.dimen.feed_image_width);
        final int imageTop = offsetTop;
        final int imageBottom = offsetBottom;
        final int dAvatarLeft = (detailAvatarWidth - screenWidth ) / 2;
        final int dAvatarTop = dTop;
        final int dAvatarRight = screenWidth - detailAvatarWidth;
        final int dAvatarBottom = detailAvatarHeight - imageBottom;

        cvDetailViewStub.setTop(offsetTop);
        cvDetailViewStub.setBottom(offsetBottom);
        */
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 3) анимация раскрытия
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = (float) animation.getAnimatedValue();
                int newTop = (int) (offsetTop - dTop * i);
                int newBottom = (int) (offsetBottom + dBottom * i);
                /*
                int newAvatarTop = (int) (imageTop - dAvatarTop * i);
                int newAvatarLeft = (int) (imageLeft - dAvatarLeft * i);
                int newAvatarBottom = (int) (imageBottom + dAvatarBottom * i);
                int newAvatarRight = (int) (imageRight + dAvatarRight * i);
                */
                cvDetailViewStub.setTop(newTop);
                cvDetailViewStub.setBottom(newBottom);
/*
                if (cvDetailViewStub.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) cvDetailViewStub.getLayoutParams();
                    lp.topMargin = newTop;
                    lp.bottomMargin = newBottom;
                    lp.height = newBottom - newTop;
                    //cvDetailViewStub.setLayoutParams(lp);

                }

                //ivStubAvatar.setLeft(newAvatarLeft);
                //ivStubAvatar.setTop(newAvatarTop);
                //ivStubAvatar.setRight(newAvatarRight);
                //ivStubAvatar.setBottom(newAvatarBottom);

                if (ivStubAvatar.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ivStubAvatar.getLayoutParams();
                    lp.leftMargin = newAvatarLeft;
                    lp.rightMargin = finalScreenWidth - newAvatarRight;
                    lp.height = (int) (newAvatarBottom - newAvatarTop);
                    lp.width = (int) (newAvatarRight - newAvatarLeft);

                }
                ViewGroup.LayoutParams lp = ivStubAvatar.getLayoutParams();
                lp.height = (int) (newAvatarBottom - newAvatarTop);
                lp.width = (int) (newAvatarRight - newAvatarLeft);
                ivStubAvatar.setLayoutParams(lp);
                */
            }
        });

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 4) вызвать каллбак и скрыть заглушку
                if (activityListener != null) {
                    activityListener.onFeedSelected(id);
                }
            }
        });
        animation.start();
    }

    private void blurBackground() {
        ivBlurBackground.setVisibility(View.VISIBLE);
        if (getView() == null) return;
        // make screenshot
        getView().setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(getView().getDrawingCache());
        getView().setDrawingCacheEnabled(false);
        FastBlur.blur(getContext(), bitmap, ivBlurBackground);
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
        if (feedDao != null) {
            feedDao.release();
            feedDao = null;
        }
        if (feedAdapter != null) {
            feedAdapter = null;
        }
    }

    public interface OnFeedFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFeedSelected(long feedId);
    }
}
