package com.grishberg.yandextest.data.db;

/**
 * Created by grishberg on 19.04.16.
 */
public class FeedContainer {
    private static final String TAG = FeedContainer.class.getSimpleName();
    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
