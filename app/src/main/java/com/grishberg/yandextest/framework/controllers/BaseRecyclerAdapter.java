package com.grishberg.yandextest.framework.controllers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.grishberg.yandextest.framework.db.DataReceiveObserver;
import com.grishberg.yandextest.framework.db.ListResult;

/**
 * базовый адаптер RecyclerView с функцией обновления данных после получения
 * результата из асинхронного запроса к бд
 * Created by grishberg on 19.04.16.
 */
public abstract class BaseRecyclerAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H>
        implements DataReceiveObserver {
    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();
    private final ListResult<T> listResult;
    private boolean isObserved;

    public BaseRecyclerAdapter(@NonNull ListResult<T> listResult) {
        this.listResult = listResult;
        listResult.addDataReceiveObserver(this);
        isObserved = true;

    }

    protected T getItem(int position){
        return listResult.getItem(position);
    }

    @Override
    public int getItemCount() {
        return listResult.getCount();
    }

    @Override
    public void onDataReceived() {
        Log.d(TAG, "onDataReceived: ");
        notifyDataSetChanged();
    }

    /**
     * Подписка на обновления из бд
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // подписываться, только если ранее не были подписаны
        if(!isObserved){
            listResult.addDataReceiveObserver(this);
            isObserved = false;
        }

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        // если были подписаны - отписаться
        if(isObserved){
            listResult.removeDataReceiveObserver(this);
        }
    }
}
