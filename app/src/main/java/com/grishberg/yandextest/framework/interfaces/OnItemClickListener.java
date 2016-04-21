package com.grishberg.yandextest.framework.interfaces;

import android.graphics.drawable.Drawable;

/**
 * Created by grishberg on 20.04.16.
 */
public interface OnItemClickListener {
    /**
     * Реакция на нажатие по элементу списка
     * @param id идентификатор элемента в бд
     * @param offsetTop верхнее смещение в пикселях
     * @param offsetBottom нижнее смещение в пикселях
     * @param drawable ссылка на изображение
     * @param bigCoverUrl URL большого постера
     */
    void onItemClicked(long id,
                       int offsetTop,
                       int offsetBottom,
                       Drawable drawable,
                       String bigCoverUrl);
}
