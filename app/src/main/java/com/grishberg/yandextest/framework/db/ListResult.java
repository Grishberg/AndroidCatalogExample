package com.grishberg.yandextest.framework.db;

/**
 * Created by grishberg on 19.04.16.
 */
public interface ListResult<T> {
    void addDataReceiveObserver(DataReceiveObserver observer);
    void removeDataReceiveObserver(DataReceiveObserver observer);
    T getItem(int index);
    int getCount();
    boolean isLoaded();
}

