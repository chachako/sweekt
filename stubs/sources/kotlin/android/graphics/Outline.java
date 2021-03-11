/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package android.graphics;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Outline {
    private static final float RADIUS_UNDEFINED = Float.NEGATIVE_INFINITY;

    public static final int MODE_EMPTY = 0;
    public static final int MODE_ROUND_RECT = 1;
    public static final int MODE_PATH = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = false,
            value = {
                    MODE_EMPTY,
                    MODE_ROUND_RECT,
                    MODE_PATH,
            })
    public @interface Mode {
    }

    @Mode
    public int mMode = MODE_EMPTY;

    public Path mPath;

    public final Rect mRect = new Rect();
    public float mRadius = RADIUS_UNDEFINED;
    public float mAlpha;

    /**
     * Constructs an empty Outline. Call one of the setter methods to make
     * the outline valid for use with a View.
     */
    public Outline() {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Constructs an Outline with a copy of the data in src.
     */
    public Outline(@NonNull Outline src) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the outline to be empty.
     *
     * @see #isEmpty()
     */
    public void setEmpty() {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Returns whether the Outline is empty.
     * <p>
     * Outlines are empty when constructed, or if {@link #setEmpty()} is called,
     * until a setter method is called
     *
     * @see #setEmpty()
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Stub!");
    }


    /**
     * Returns whether the outline can be used to clip a View.
     * <p>
     * Currently, only Outlines that can be represented as a rectangle, circle,
     * or round rect support clipping.
     *
     * @see android.view.View#setClipToOutline(boolean)
     */
    public boolean canClip(){
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the alpha represented by the Outline - the degree to which the
     * producer is guaranteed to be opaque over the Outline's shape.
     * <p>
     * An alpha value of <code>0.0f</code> either represents completely
     * transparent content, or content that isn't guaranteed to fill the shape
     * it publishes.
     * <p>
     * Content producing a fully opaque (alpha = <code>1.0f</code>) outline is
     * assumed by the drawing system to fully cover content beneath it,
     * meaning content beneath may be optimized away.
     */
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha){
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Returns the alpha represented by the Outline.
     */
    public float getAlpha(){
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Replace the contents of this Outline with the contents of src.
     *
     * @param src Source outline to copy from.
     */
    public void set(@NonNull Outline src) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the Outline to the rounded rect defined by the input rect, and
     * corner radius.
     */
    public void setRect(int left, int top, int right, int bottom) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Convenience for {@link #setRect(int, int, int, int)}
     */
    public void setRect(@NonNull Rect rect) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the Outline to the rounded rect defined by the input rect, and corner radius.
     * <p>
     * Passing a zero radius is equivalent to calling {@link #setRect(int, int, int, int)}
     */
    public void setRoundRect(int left, int top, int right, int bottom, float radius) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Convenience for {@link #setRoundRect(int, int, int, int, float)}
     */
    public void setRoundRect(@NonNull Rect rect, float radius) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Populates {@code outBounds} with the outline bounds, if set, and returns
     * {@code true}. If no outline bounds are set, or if a path has been set
     * via {@link #setPath(Path)}, returns {@code false}.
     *
     * @param outRect the rect to populate with the outline bounds, if set
     * @return {@code true} if {@code outBounds} was populated with outline
     * bounds, or {@code false} if no outline bounds are set
     */
    public boolean getRect(@NonNull Rect outRect) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Returns the rounded rect radius, if set, or a value less than 0 if a path has
     * been set via {@link #setPath(Path)}. A return value of {@code 0}
     * indicates a non-rounded rect.
     *
     * @return the rounded rect radius, or value < 0
     */
    public float getRadius() {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the outline to the oval defined by input rect.
     */
    public void setOval(int left, int top, int right, int bottom) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Convenience for {@link #setOval(int, int, int, int)}
     */
    public void setOval(@NonNull Rect rect) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the Outline to a
     * {@link android.graphics.Path#isConvex() convex path}.
     *
     * @param convexPath used to construct the Outline. As of
     *                   {@link android.os.Build.VERSION_CODES#Q}, it is no longer required to be
     *                   convex.
     * @deprecated As of {@link android.os.Build.VERSION_CODES#Q}, the restriction
     * that the path must be convex is removed. However, the API is misnamed until
     * {@link android.os.Build.VERSION_CODES#R}, when {@link #setPath} is
     * introduced. Use {@link #setPath} instead.
     */
    @Deprecated
    public void setConvexPath(@NonNull Path convexPath){
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Sets the Outline to a {@link android.graphics.Path path}.
     *
     * @param path used to construct the Outline.
     */
    @RequiresApi(android.os.Build.VERSION_CODES.R)
    public void setPath(@NonNull Path path) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Offsets the Outline by (dx,dy)
     */
    public void offset(int dx, int dy) {
        throw new UnsupportedOperationException("Stub!");
    }
}
