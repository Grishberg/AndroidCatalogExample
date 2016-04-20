package com.grishberg.yandextest.data.rest;

import com.grishberg.yandextest.data.model.FeedRestContainer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * интерфейс, описывающий методы REST-сервера
 * Created by grishberg on 20.04.16.
 */
public interface RetrofitService {
    @GET(RestConst.Methods.GET_FEEDS)
    Call<List<FeedRestContainer>> plannedTasks();
}
