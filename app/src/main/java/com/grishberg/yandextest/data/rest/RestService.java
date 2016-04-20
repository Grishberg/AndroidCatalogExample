package com.grishberg.yandextest.data.rest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by grishberg on 19.04.16.
 */
public class RestService extends IntentService {
    private static final String TAG = RestService.class.getSimpleName();

    public static final String CALLBACK_KEY = "CALLBACK_KEY";
    public static final String ACTION_KEY = "ACTION_KEY";
    public static final String ERROR_KEY = "ERROR_KEY";
    public static final String REQUEST_OBJECT_KEY = "REQUEST_OBJECT_KEY";
    public static final String RESPONSE_OBJECT_KEY = "RESPONSE_OBJECT_KEY";

    public static final int ACTION_GET_FEED_LIST = 1;
    RestWorker restWorker;
    // ресивер для отправки ответа
    private ResultReceiver receiver;
    private boolean isDestroyed;

    public RestService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        isDestroyed = false;
        restWorker = new RestWorker();
        super.onCreate();
    }

    /**
     * Получение очередного интента с задачей и возврат результата
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(CALLBACK_KEY);
        int action = intent.getIntExtra(ACTION_KEY, -1);
        Bundle data = processIntent(intent, action);
        sentMessage(action, data);
    }

    /**
     * Обработка входящего интента для отправки REST-запроса
     * вызов соответствующего метода в зависимости от action
     * @param intent интент с данными
     * @param action тип запроса
     * @return
     */
    private Bundle processIntent(Intent intent, int  action){
        Serializable response = null;
        Bundle bundle = new Bundle();
        switch (action){
            case ACTION_GET_FEED_LIST:
                response = restWorker.getFeeds(getRequestObject(intent));
        }

        if(response == null){
            bundle.putBoolean(ERROR_KEY, true);
        } else {
            bundle.putSerializable(RESPONSE_OBJECT_KEY, response);
        }
        return bundle;
    }

    /**
     * Извлечение из интента данных для запроса
     * @param intent
     * @return
     */
    private Serializable getRequestObject(Intent intent){
        return intent.getSerializableExtra(REQUEST_OBJECT_KEY);
    }

    /**
     * Отправка результата ресиверу
     * @param code
     * @param data
     */
    private void sentMessage(int code, Bundle data){
        if(!isDestroyed && receiver != null){
            receiver.send(code, data);
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if(restWorker != null){
            restWorker.release();
            restWorker = null;
        }
        isDestroyed = true;
        receiver = null;
        super.onDestroy();
    }
}
