package com.grishberg.yandextest.data.rest;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grishberg.yandextest.data.db.FeedDao;
import com.grishberg.yandextest.data.model.FeedRestContainer;
import com.grishberg.yandextest.framework.rest.HttpLoggingInterceptor;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.List;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Воркер, непосредственно занимающийся отправкой данных REST-сервису
 * Created by grishberg on 19.04.16.
 */
public class RestWorker {
    private static final String TAG = RestWorker.class.getSimpleName();
    private static final int TRIES_COUNT = 3;

    private RetrofitService restService;
    private FeedDao feedDao;

    public RestWorker() {
        feedDao = new FeedDao();
        createService();
    }

    /**
     * Инициализация retrofit
     */
    private void createService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestConst.BACKEND_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(
                        new Gson()))
                .addConverterFactory(GsonConverterFactory.create(
                        GsonConverterFactoryHelper.create()))
                .build();
        restService = retrofit.create(RetrofitService.class);
    }

    /**
     * Извлечение данных с сервера
     * @param intent
     * @return
     */
    public Serializable getFeeds(Serializable intent) {
        Log.d(TAG, "getFeeds: " + intent);
        Serializable data = null;
        int errorCode = 0;
        String errorMessage = "";
        // несколько попыток
        for (int tries = 0; tries < TRIES_COUNT; tries++) {
            try {
                retrofit2.Call<List<FeedRestContainer>> resp = restService.plannedTasks();
                Response<List<FeedRestContainer>> response = resp.execute();
                List<FeedRestContainer> result = response != null ? response.body() : null;
                Log.d(TAG, String.format("executeRequest: code = %d",
                        response != null ? response.code() : 0));
                if(response != null) {
                    switch (response.code()) {
                        case 200:
                            data = 200;
                            feedDao.addFeeds(result);
                            return data;
                    }
                }
            } catch (ConnectException e) {
                errorCode = RestConst.Errors.CONNECTION_ERROR;
                errorMessage = e.getMessage();
                Log.e(TAG, "executeRequest: ", e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static class GsonConverterFactoryHelper {
        public static final String TAG = GsonConverterFactoryHelper.class.getName();

        public static Gson create() {
            return new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();
        }
    }

    public void release(){
        if(feedDao != null){
            feedDao.release();
        }
    }
}
