package com.grishberg.yandextest.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishberg on 22.05.16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    /**
     * Вернуть список папок с фотографиями
     * @param context
     * @return
     */
    public static File[] getMediaDirs(Context context){
        File mediaFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File[] res = mediaFolder.listFiles();
        List<File> files = new ArrayList<>();

        //TODO: реализовать сканирование папок с фотографиями
        return files.toArray(new File[files.size()]);

    }

    public static String getExt(String fileName){
        int dotPos = fileName.lastIndexOf('.');
        if(dotPos > 0){
            return fileName.substring(dotPos + 1);
        }
        return "";
    }
}
