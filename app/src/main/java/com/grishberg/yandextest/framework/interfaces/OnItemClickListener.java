package com.grishberg.yandextest.framework.interfaces;

import android.graphics.drawable.Drawable;

/**
 * Created by grishberg on 20.04.16.
 */
public interface OnItemClickListener {
    void onItemClicked(long id, int offsetTop, int offsetBottom, Drawable drawable);
}
