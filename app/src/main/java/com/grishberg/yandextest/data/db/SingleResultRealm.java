package com.grishberg.yandextest.data.db;

import com.grishberg.yandextest.framework.db.SingleResult;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by g on 20.04.16.
 */
public class SingleResultRealm<T extends RealmObject> extends BaseResultImpl<T>
        implements SingleResult<T> {

    public SingleResultRealm(RealmResults<T> realmResults) {
        super(realmResults);
        realmResults.addChangeListener(callback);
    }

    /**
     * Вернуть элемент
     * @return
     */
    @Override
    public T getItem() {
        return realmResults.size() > 0 ? realmResults.first() : null;
    }

    /**
     * Вернуть состояние - получены ли данные из бд
     * @return
     */
    @Override
    public boolean isLoaded() {
        return realmResults.isLoaded();
    }
}
