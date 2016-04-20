package com.grishberg.yandextest.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grishberg.yandextest.R;
import com.grishberg.yandextest.data.model.FeedContainer;
import com.grishberg.yandextest.framework.controllers.BaseRecyclerAdapter;
import com.grishberg.yandextest.framework.db.ListResult;
import com.grishberg.yandextest.framework.interfaces.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Адаптер recyclerView со списком фидов
 * Created by grishberg on 19.04.16.
 */
public class FeedAdapter extends BaseRecyclerAdapter<FeedContainer, FeedAdapter.FeedViewHolder> {
    private static final String TAG = FeedAdapter.class.getSimpleName();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private final OnItemClickListener listener;

    public FeedAdapter(Context context,
                       ListResult<FeedContainer> listResult,
                       OnItemClickListener listener) {
        super(context, listResult);
        this.listener = listener;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_broken)
                .showImageOnFail(R.drawable.ic_broken)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_element
                , parent, false);
        FeedViewHolder pvh = new FeedViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        FeedContainer item = getItem(position);

        holder.tvTitle.setText(item.getName());
        holder.id = item.getId();
        holder.position = position;
        holder.tvGenre.setText(item.getGenres());
        holder.tvInfo.setText(String.format(
                getContext().getString(R.string.feed_cell_info), item.getAlbums() , item.getTracks()));
        imageLoader.displayImage(item.getCoverSmall(), holder.ivAvatar, options,
                new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted: ");
                holder.ivAvatar.setVisibility(View.INVISIBLE);
                holder.pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "onLoadingFailed: ");
                holder.ivAvatar.setVisibility(View.INVISIBLE);
                holder.pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete: ");
                holder.ivAvatar.setVisibility(View.VISIBLE);
                holder.pbLoading.setVisibility(View.GONE);
            }
        });

        holder.clickListener = listener;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public int position;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvGenre;
        TextView tvInfo;
        ProgressBar pbLoading;
        OnItemClickListener clickListener;

        FeedViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivFeedAvatar);
            tvTitle = (TextView) itemView.findViewById(R.id.tvFeedTitle);
            tvGenre = (TextView) itemView.findViewById(R.id.tvFeedGenre);
            tvInfo = (TextView) itemView.findViewById(R.id.tvFeedInfo);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pbLoading);
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClicked(id, position);
                    }
                }
            });
        }

    }
}
