package com.grishberg.yandextest.data.db;

import com.grishberg.yandextest.framework.db.BaseResult;
import com.grishberg.yandextest.framework.db.DataReceiveObserver;

import java.util.LinkedList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by g on 20.04.16.
 */
public class BaseResultImpl<T extends RealmObject> implements BaseResult {
    private final List<DataReceiveObserver> observers;
    protected final RealmResults<T> realmResults;

    public BaseResultImpl(RealmResults<T> realmResults) {
        this.realmResults = realmResults;
        realmResults.addChangeListener(callback);
        observers = new LinkedList<>();
    }

    /**
     * Добавить слушателя
     * @param observer
     */
    @Override
    public void addDataReceiveObserver(DataReceiveObserver observer) {
        observers.add(observer);
    }

    /**
     * удалить слушателя
     * @param observer
     */
    @Override
    public void removeDataReceiveObserver(DataReceiveObserver observer) {
        observers.remove(observer);
    }

    /**
     * Сообщить подписчикам о том что данные обновлены
     */
    protected RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange() { // called once the query complete and on every update
            for (DataReceiveObserver observer : observers) {
                observer.onDataReceived();
            }
        }
    };

    @Override
    public boolean isLoaded() {
        return false;
    }
}
