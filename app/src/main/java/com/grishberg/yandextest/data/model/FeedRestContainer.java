package com.grishberg.yandextest.data.model;

import java.util.List;

/**
 * Created by grishberg on 20.04.16.
 */
public class FeedRestContainer {
    private static final String TAG = FeedRestContainer.class.getSimpleName();
    private long id;
    private String name;
    private List<String> genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private CoverRestContainer cover;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CoverRestContainer getCover() {
        return cover;
    }

    public void setCover(CoverRestContainer cover) {
        this.cover = cover;
    }
}
