package com.grishberg.yandextest.data.rest;

/**
 * Created by grishberg on 20.04.16.
 */
public class RestConst {
    private static final String TAG = RestConst.class.getSimpleName();
    public static final String BACKEND_ENDPOINT = "http://cache-novosibirsk05.cdn.yandex.net";
    public static final String API = "/download.cdn.yandex.net/mobilization-2016";
    public static final class Methods{
        public static final String GET_FEEDS = API + "/artists.json";
    }
    public static final class Errors{
        public static final int CONNECTION_ERROR = -1;
    }
}
