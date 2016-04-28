package com.grishberg.yandextest.common.db;

/**
 * Created by grishberg on 19.04.16.
 */
public interface ListResult<T> extends BaseResult {
    T getItem(int index);
    int getCount();
}

