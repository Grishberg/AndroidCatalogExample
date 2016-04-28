package com.grishberg.yandextest.common.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.grishberg.yandextest.App;

/**
 * Created by grishberg on 20.04.16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean isBroadcastRegistered;
    private IntentFilter localBroadcast = new IntentFilter(App.INIT_ACTION);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    /**
     * рессивер для получения сообщения о том, что App класс проинициализирован
     */
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case App.INIT_ACTION:
                    onInit();
                    break;
            }
        }
    };

    /**
     * Вызывается после инициализации App
     */
    protected abstract void onInit();


    private void registerBroadcast() {
        if (!isBroadcastRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    messageReceiver, localBroadcast);
            isBroadcastRegistered = true;
        }
    }

    private void unregisterBroadcast() {
        if (isBroadcastRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    messageReceiver);
            isBroadcastRegistered = false;
        }
    }
}
