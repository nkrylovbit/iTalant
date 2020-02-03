package ru.vnn.nick.italent.camera.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.WindowManager;

public class AutoFitTextureView extends TextureView {
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        Log.d("setAspectRatio", "Ratio aspect: " + width + " " + height);
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        @SuppressLint("DrawAllocation") DisplayMetrics mMetrics = new DisplayMetrics();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("onMeasure", "ONMEASURE " + width + " " + height);

        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(mMetrics);
            Log.d("onMeasure", "ONMEASURE WIDTH METRIX: " + mMetrics.widthPixels);
            Log.d("onMeasure", "ONMEASURE HEIGHT METRIX: " + mMetrics.heightPixels);
            double ratio = (double) mRatioWidth / (double) mRatioHeight;
            double invertedRatio = (double) mRatioHeight / (double) mRatioWidth;
            double portraitHeight = width * invertedRatio;
            double portraitWidth = width * (mMetrics.heightPixels / portraitHeight);
            double landscapeWidth = height * ratio;
            double landscapeHeight = (mMetrics.widthPixels / landscapeWidth) * height;

            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension((int) portraitWidth, mMetrics.heightPixels);
            } else {
                setMeasuredDimension(mMetrics.widthPixels, (int) landscapeHeight);
            }
        }
    }
}
