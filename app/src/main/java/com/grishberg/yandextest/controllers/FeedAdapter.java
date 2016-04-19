package com.grishberg.yandextest.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grishberg.yandextest.R;
import com.grishberg.yandextest.data.db.FeedContainer;
import com.grishberg.yandextest.framework.controllers.BaseRecyclerAdapter;
import com.grishberg.yandextest.framework.db.ListResult;

import java.util.Date;

/**
 * Адаптер recyclerView со списком фидов
 * Created by grishberg on 19.04.16.
 */
public class FeedAdapter extends BaseRecyclerAdapter<FeedContainer, FeedAdapter.FeedViewHolder> {
    private static final String TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter(ListResult<FeedContainer> listResult) {
        super(listResult);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_element
                , parent, false);
        FeedViewHolder pvh = new FeedViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedContainer item = getItem(position);

        holder.tvTitle.setText(item.getTitle());
        holder.id = item.getId();
        holder.position = position;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public int position;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvGenre;
        TextView tvInfo;

        FeedViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivFeedAvatar);
            tvTitle = (TextView) itemView.findViewById(R.id.tvFeedTitle);
            tvGenre = (TextView) itemView.findViewById(R.id.tvFeedGenre);
            tvInfo = (TextView) itemView.findViewById(R.id.tvFeedInfo);
        }
    }
}
