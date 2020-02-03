package ru.vnn.nick.italent.utils;

import android.content.Context;
import android.util.Size;
import android.util.SparseArray;
import android.view.Display;

/**
 * Helper for make CSS based on done design.
 */
public class DrawableManager {
    private final Context ctx;
    private final DimensionManager dimensionManager;
    private final Display display;

    public DrawableManager(final Context ctx, final Display display) {
        this.ctx = ctx;
        this.dimensionManager = new DimensionManager(ctx);
        this.display = display;
    }

    /** Return the need size of container for resource drawable
     *
     * @param resId drawable resource id
     * @return Size of container for drawable
     */
    @SuppressWarnings("deprecation")
    public Size getDrawableIdWithSize (final int resId) {
        return dimensionManager.getSizeWithAspect(resId, display.getWidth());
    }

    /** Return the need sizes of containers for resources drawables.
     *
     * @param resIds drawable resource ids
     * @return Sizes of containers for drawables
     */
    public SparseArray<Size> getDrawableIdsWithSizes (final int... resIds) {
        SparseArray<Size> result = new SparseArray<>();

        for (int resId: resIds) {
            result.append(resId, getDrawableIdWithSize(resId));
        }

        return result;
    }
}
