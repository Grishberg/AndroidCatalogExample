package com.grishberg.yandextest.data.db;

import android.util.Log;

import com.grishberg.yandextest.data.model.FeedContainer;
import com.grishberg.yandextest.data.model.FeedRestContainer;
import com.grishberg.yandextest.framework.db.ListResult;
import com.grishberg.yandextest.framework.db.SingleResult;

import java.util.List;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by grishberg on 19.04.16.
 */
public class FeedDao {
    private static final String TAG = FeedDao.class.getSimpleName();
    private final Realm realm;

    public FeedDao() {
        realm = Realm.getDefaultInstance();
    }

    /**
     * Добавить фиды в БД
     *
     * @param feeds
     */
    public void addFeeds(List<FeedRestContainer> feeds) {
        Log.d(TAG, "addFeeds: ");
        Realm localRealm = Realm.getDefaultInstance();
        localRealm.beginTransaction();
        for (FeedRestContainer feed : feeds) {
            FeedContainer newFeed = new FeedContainer();
            newFeed.setId(feed.getId());
            newFeed.setName(feed.getName());
            newFeed.setAlbums(feed.getAlbums());
            newFeed.setTracks(feed.getTracks());
            if (feed.getCover() != null) {
                newFeed.setCoverBig(feed.getCover().getBig());
                newFeed.setCoverSmall(feed.getCover().getSmall());
            }
            newFeed.setDescription(feed.getDescription());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < feed.getGenres().size(); i++) {
                sb.append(feed.getGenres().get(i));
                if (i < feed.getGenres().size() - 1) {
                    sb.append(", ");
                }
            }
            newFeed.setGenres(sb.toString());
            newFeed.setLink(feed.getLink());
            localRealm.copyToRealmOrUpdate(newFeed);
        }
        localRealm.commitTransaction();
        localRealm.close();
    }

    /**
     * Вернуть список фидов
     * Возвращается объект интерфейся ListResult который асинхронно возвращает результат
     * и уведомляет подписчика
     * @return ListResult
     */
    public ListResult<FeedContainer> getFeeds() {
        Log.d(TAG, "getFeeds: ");
        RealmQuery<FeedContainer> query = realm.where(FeedContainer.class);
        return new ListResultRealm<>(query.findAllSortedAsync("id", Sort.ASCENDING));
    }

    public SingleResult<FeedContainer> getFeed(long id) {
        Log.d(TAG, String.format("getFeed: id=%d", id));
        RealmQuery<FeedContainer> query = realm.where(FeedContainer.class);
        return new SingleResultRealm<>(query.findAllSortedAsync("id", Sort.ASCENDING));
    }

    public void release() {
        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
