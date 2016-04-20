package com.grishberg.yandextest.data.rest;

import android.content.Context;
import android.support.v4.os.ResultReceiver;

import com.grishberg.yandextest.framework.rest.BaseRestServiceHelper;

/**
 * Created by grishberg on 19.04.16.
 */
public class RestServiceHelper extends BaseRestServiceHelper {
    private static final String TAG = RestServiceHelper.class.getSimpleName();

    /**
     * Извлечение фидов
     * @param context
     * @param onServiceResult
     */
    public static void getFeeds(Context context, ResultReceiver onServiceResult){
        executeRequest(context, RestService.ACTION_GET_FEED_LIST, null, onServiceResult);
    }
}
