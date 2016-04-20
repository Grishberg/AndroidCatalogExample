package com.grishberg.yandextest.framework.rest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.os.ResultReceiver;

import com.grishberg.yandextest.data.rest.RestService;

import java.io.Serializable;

/**
 * Created by grishberg on 19.04.16.
 */
public class BaseRestServiceHelper {
    private static final String TAG = BaseRestServiceHelper.class.getSimpleName();

    // запуск команды REST-сервиса
    protected static void executeRequest(Context context, int action,
                                       Serializable data,
                                       ResultReceiver onServiceResult) {
        Intent intent = getIntent(context, action, onServiceResult);
        if (data != null) {
            intent.putExtra(RestService.REQUEST_OBJECT_KEY, data);
        }
        context.startService(intent);
    }

    /**
     * Формирование интента для запуска REST-сервиса
     * @param context
     * @param action
     * @param onServiceResult
     * @return
     */
    private static Intent getIntent(Context context, int action, ResultReceiver onServiceResult) {
        final Intent i = new Intent(context, RestService.class);
        i.putExtra(RestService.ACTION_KEY, action);
        i.putExtra(RestService.CALLBACK_KEY, onServiceResult);
        return i;
    }
}
