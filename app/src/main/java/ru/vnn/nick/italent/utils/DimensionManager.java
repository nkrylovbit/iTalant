package ru.vnn.nick.italent.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Size;

/**
 * Helper for works with CSS & Drawables.
 */
public class DimensionManager {
    private Context context;
    private static final String TAG = "DimensionManager";

    public DimensionManager(Context context) {
        this.context = context;
    }

    /**
     * Get the size with aspect for container.
     * @param resId resource id
     * @param layoutWidth container width
     * @param layoutHeight container height
     * @return size
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public Size getSizeFromDrawable(int resId, int layoutWidth, int layoutHeight) {
        Size imageSize = getDrawableSize(resId);

        if (layoutWidth != 0) {
            return new Size(layoutWidth, layoutWidth * imageSize.getHeight() / imageSize.getWidth());
        } else {
            return new Size(layoutHeight * imageSize.getWidth() / imageSize.getHeight(), layoutHeight);
        }
    }

    /**
     * Get a real size of the drawable resource.
     * @param resId resource id
     * @return Size
     */
    public Size getDrawableSize(int resId) {
        BitmapDrawable image = (BitmapDrawable) context.getDrawable(resId);
        Bitmap bitmap;

        if (image != null) {
            bitmap = image.getBitmap();
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        }

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        return new Size(width, height);
    }

    /** Find how much image smaller that display.
     *
     * @param resId Resource image
     * @param width Width of display
     * @return Aspect as imageWidth/displayWidth
     */
    public double getAspectForWidth (final int resId, final int width) {
        final Size imageSize = getDrawableSize(resId);

        return imageSize.getWidth() / width;
    }

    /** Return the need size of container for resource drawable based on width of relative container.
     *
     * @param resId drawable resource id
     * @param width width of relative container
     * @return Size of container for drawable
     */
    public Size getSizeWithAspect (final int resId, final int width) {
        final double aspect = getAspectForWidth(resId, width);
        final Size drawableSize = getDrawableSize(resId);

        return new Size(
                ((Double) (drawableSize.getWidth() * aspect)).intValue(),
                ((Double) (drawableSize.getHeight() * aspect)).intValue());
    }
}
