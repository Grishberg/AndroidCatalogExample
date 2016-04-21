package com.grishberg.yandextest;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by grishberg on 19.04.16.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    public static final String INIT_ACTION = "initAction";
    private static boolean sInitiated;
    private Intent initOkIntent = new Intent(INIT_ACTION);

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        Log.d(TAG, String.format("onCreate: v.%s:%d", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        init();
    }

    // Инициализация библиотек
    private void init(){

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void[] params) {
                initRealm();
                initUil();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                sInitiated = true;
                LocalBroadcastManager.getInstance(App.this).sendBroadcast(initOkIntent);
            }
        }.execute();
    }

    /**
     * Инициализация БД
     */
    private void initRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * Инициализация Universal Image Loader
     */
    private void initUil(){
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration
                .Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static boolean isInitiated(){
        return sInitiated;
    }
}
