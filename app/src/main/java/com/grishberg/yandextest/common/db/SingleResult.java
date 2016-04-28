package com.grishberg.yandextest.common.db;

/**
 * Created by g on 20.04.16.
 */
public interface SingleResult<T> extends BaseResult {
    T getItem();
}
