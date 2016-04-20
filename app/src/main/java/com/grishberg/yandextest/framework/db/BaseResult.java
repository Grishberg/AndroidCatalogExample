package com.grishberg.yandextest.framework.db;

/**
 * Created by g on 20.04.16.
 */
public interface BaseResult {
    void addDataReceiveObserver(DataReceiveObserver observer);
    void removeDataReceiveObserver(DataReceiveObserver observer);
    boolean isLoaded();
}
