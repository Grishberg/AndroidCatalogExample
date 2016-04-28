package com.grishberg.yandextest.data.db;

import com.grishberg.yandextest.common.db.ListResult;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Реализация объекта - списка результатов из бд
 * позволяет добавить подписчиков, которые будут уведомлены, когда данные из
 * асинхронного запроса станут доступны.
 * Created by grishberg on 19.04.16.
 */
public class ListResultRealm<T extends RealmObject> extends BaseResultImpl<T>
        implements ListResult<T> {

    public ListResultRealm(RealmResults<T> realmResults) {
        super(realmResults);
    }

    /**
     * Вернуть элемент по индексу
     * @param index
     * @return
     */
    @Override
    public T getItem(int index) {
        return realmResults.get(index);
    }

    /**
     * Вернуть общее количество элементов
     * @return
     */
    @Override
    public int getCount() {
        return realmResults.size();
    }

    /**
     * Возвращает состояние - загружены данные или нет
     * @return
     */
    @Override
    public boolean isLoaded() {
        return realmResults.isLoaded();
    }

    public void release() {
        realmResults.removeChangeListener(callback);
    }
}
