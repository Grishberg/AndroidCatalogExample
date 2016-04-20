package com.grishberg.yandextest.framework.db;

/**
 * Created by g on 20.04.16.
 */
public interface SingleResult<T> extends BaseResult {
    T getItem();
}
