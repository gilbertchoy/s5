package com.me.gc.scratcher1;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author doriancussen
 */
public class ImageViewTopCrop extends android.support.v7.widget.AppCompatImageView
{
    public int imageWidth;

    public ImageViewTopCrop(Context context)
    {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public ImageViewTopCrop(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        Matrix matrix = getImageMatrix();
        //int dwidth = getDrawable().getIntrinsicWidth();
        //int dheight = getDrawable().getIntrinsicHeight();
//
        imageWidth = getWidth();

        float scaleFactor = imageWidth/(float)getDrawable().getIntrinsicWidth();
        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }

}