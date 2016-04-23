package com.grishberg.yandextest.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by grishberg on 23.04.16.
 */
public class CustomImageView extends ImageView {
    private static final String TAG = CustomImageView.class.getSimpleName();
    private Bitmap background;
    private Paint paint = new Paint();
    private Rect destRect;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }

    public void drawBitmap(Bitmap src) {
        background = src;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        destRect = new Rect(0, 0, getWidth(), getHeight());
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (background != null) {
            canvas.drawBitmap(background, null, destRect, paint);
        }
    }
}
