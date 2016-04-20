package com.grishberg.yandextest.data.db;

import com.grishberg.yandextest.framework.db.DataReceiveObserver;
import com.grishberg.yandextest.framework.db.ListResult;

import java.util.LinkedList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by grishberg on 19.04.16.
 */
public class ListResultRealm<T extends RealmObject> implements ListResult<T> {
    private final List<DataReceiveObserver> observers;
    private final RealmResults<T> realmResults;

    public ListResultRealm(RealmResults<T> realmResults) {
        observers = new LinkedList<>();
        this.realmResults = realmResults;
        realmResults.addChangeListener(callback);
    }

    @Override
    public T getItem(int index) {
        return realmResults.get(index);
    }

    @Override
    public int getCount() {
        return realmResults.size();
    }

    @Override
    public void addDataReceiveObserver(DataReceiveObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeDataReceiveObserver(DataReceiveObserver observer) {
        observers.remove(observer);
    }

    @Override
    public boolean isLoaded() {
        return realmResults.isLoaded();
    }

    public void release() {
        realmResults.removeChangeListener(callback);
    }
    /**
     * Сообщить подписчикам о том что данные обновлены
     */
    private RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange() { // called once the query complete and on every update
            for (DataReceiveObserver observer : observers) {
                observer.onDataReceived();
            }
        }
    };
}
