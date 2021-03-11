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
@file:Suppress("unused", "LocalVariableName", "MemberVisibilityCanBePrivate",
  "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.meowbase.toolkit.graphics.drawable

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import kotlin.math.roundToInt

/**
 * A Drawable that manages an array of other Drawables. These are drawn in array
 * order, so the element with the largest index will be drawn on top.
 *
 *
 * It can be defined in an XML file with the `<layer-list>` element.
 * Each Drawable in the layer is defined in a nested `<item>`.
 *
 *
 * For more information, see the guide to
 * [Drawable Resources]({@docRoot}guide/topics/resources/drawable-resource.html).
 *
 * @attr ref android.R.styleable#LayerDrawable_paddingMode
 * @attr ref android.R.styleable#LayerDrawableItem_left
 * @attr ref android.R.styleable#LayerDrawableItem_top
 * @attr ref android.R.styleable#LayerDrawableItem_right
 * @attr ref android.R.styleable#LayerDrawableItem_bottom
 * @attr ref android.R.styleable#LayerDrawableItem_start
 * @attr ref android.R.styleable#LayerDrawableItem_end
 * @attr ref android.R.styleable#LayerDrawableItem_width
 * @attr ref android.R.styleable#LayerDrawableItem_height
 * @attr ref android.R.styleable#LayerDrawableItem_gravity
 * @attr ref android.R.styleable#LayerDrawableItem_drawable
 * @attr ref android.R.styleable#LayerDrawableItem_id
 */
class LayerDrawableCompat @JvmOverloads internal constructor(
  state: LayerState? = null,
  res: Resources? = null
) : Drawable(), Drawable.Callback {
  var mLayerState: LayerState
  private var mPaddingL: IntArray? = null
  private var mPaddingT: IntArray? = null
  private var mPaddingR: IntArray? = null
  private var mPaddingB: IntArray? = null
  private val mTmpRect = Rect()
  private val mTmpOutRect = Rect()
  private val mTmpContainer = Rect()
  private var mHotspotBounds: Rect? = null
  private var mMutated = false
  private var mSuspendChildInvalidation = false
  private var mChildRequestedInvalidation = false

  /**
   * Creates a new layer drawable with the list of specified layers.
   *
   * @param layers a list of drawables to use as layers in this new drawable,
   * must be non-null
   */
  constructor(layers: Array<Drawable>) : this(layers, null)

  /**
   * Creates a new layer drawable with the specified list of layers and the
   * specified constant state.
   *
   * @param layers The list of layers to add to this drawable.
   * @param state  The constant drawable state.
   */
  internal constructor(layers: Array<Drawable>, state: LayerState?) : this(state, null) {
    val length = layers.size
    val r = arrayOfNulls<ChildDrawable>(length)
    for (i in 0 until length) {
      r[i] = ChildDrawable(mLayerState.mDensity)
      r[i]!!.mDrawable = layers[i]
      layers[i].callback = this
      mLayerState.mChildrenChangingConfigurations = mLayerState.mChildrenChangingConfigurations or layers[i].changingConfigurations
    }
    mLayerState.mNum = length
    mLayerState.mChildren = r
    ensurePadding()
    refreshPadding()
  }

  fun createConstantState(state: LayerState?, res: Resources?): LayerState {
    return LayerState(state, this, res)
  }

  //    @Override
  //    public void applyTheme(@NonNull Theme t) {
  //        super.applyTheme(t);
  //
  //        final LayerState state = mLayerState;
  //        if (state == null) {
  //            return;
  //        }
  //
  //        final int density = Drawable2.resolveDensity(t.getResources(), 0);
  //        state.setDensity(density);
  //
  //        if (state.mThemeAttrs != null) {
  //            final TypedArray a = t.resolveAttributes(
  //                state.mThemeAttrs, R.styleable.LayerDrawable);
  //            updateStateFromTypedArray(a);
  //            a.recycle();
  //        }
  //
  //        final ChildDrawable[] array = state.mChildren;
  //        final int N = state.mNum;
  //        for (int i = 0; i < N; i++) {
  //            final ChildDrawable layer = array[i];
  //            layer.setDensity(density);
  //
  //            if (layer.mThemeAttrs != null) {
  //                final TypedArray a = t.resolveAttributes(
  //                    layer.mThemeAttrs, R.styleable.LayerDrawableItem);
  //                updateLayerFromTypedArray(layer, a);
  //                a.recycle();
  //            }
  //
  //            final Drawable d = layer.mDrawable;
  //            if (d != null && d.canApplyTheme()) {
  //                d.applyTheme(t);
  //
  //                // Update cached mask of child changing configurations.
  //                state.mChildrenChangingConfigurations |= d.getChangingConfigurations();
  //            }
  //        }
  //    }
  //
  //    /**
  //     * Initializes the constant state from the values in the typed array.
  //     */
  //    private void updateStateFromTypedArray(@NonNull TypedArray a) {
  //        final LayerState state = mLayerState;
  //
  //        // Account for any configuration changes.
  //        state.mChangingConfigurations |= a.getChangingConfigurations();
  //
  //        // Extract the theme attributes, if any.
  //        state.mThemeAttrs = a.extractThemeAttrs();
  //
  //        final int N = a.getIndexCount();
  //        for (int i = 0; i < N; i++) {
  //            final int attr = a.getIndex(i);
  //            switch (attr) {
  //                case R.styleable.LayerDrawable_opacity:
  //                    state.mOpacityOverride = a.getInt(attr, state.mOpacityOverride);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingTop:
  //                    state.mPaddingTop = a.getDimensionPixelOffset(attr, state.mPaddingTop);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingBottom:
  //                    state.mPaddingBottom = a.getDimensionPixelOffset(attr, state.mPaddingBottom);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingLeft:
  //                    state.mPaddingLeft = a.getDimensionPixelOffset(attr, state.mPaddingLeft);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingRight:
  //                    state.mPaddingRight = a.getDimensionPixelOffset(attr, state.mPaddingRight);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingStart:
  //                    state.mPaddingStart = a.getDimensionPixelOffset(attr, state.mPaddingStart);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingEnd:
  //                    state.mPaddingEnd = a.getDimensionPixelOffset(attr, state.mPaddingEnd);
  //                    break;
  //                case R.styleable.LayerDrawable_autoMirrored:
  //                    state.mAutoMirrored = a.getBoolean(attr, state.mAutoMirrored);
  //                    break;
  //                case R.styleable.LayerDrawable_paddingMode:
  //                    state.mPaddingMode = a.getInteger(attr, state.mPaddingMode);
  //                    break;
  //            }
  //        }
  //    }
  //
  //    private void updateLayerFromTypedArray(@NonNull ChildDrawable layer, @NonNull TypedArray a) {
  //        final LayerState state = mLayerState;
  //
  //        // Account for any configuration changes.
  //        state.mChildrenChangingConfigurations |= a.getChangingConfigurations();
  //
  //        // Extract the theme attributes, if any.
  //        layer.mThemeAttrs = a.extractThemeAttrs();
  //
  //        final int N = a.getIndexCount();
  //        for (int i = 0; i < N; i++) {
  //            final int attr = a.getIndex(i);
  //            switch (attr) {
  //                case R.styleable.LayerDrawableItem_left:
  //                    layer.mInsetL = a.getDimensionPixelOffset(attr, layer.mInsetL);
  //                    break;
  //                case R.styleable.LayerDrawableItem_top:
  //                    layer.mInsetT = a.getDimensionPixelOffset(attr, layer.mInsetT);
  //                    break;
  //                case R.styleable.LayerDrawableItem_right:
  //                    layer.mInsetR = a.getDimensionPixelOffset(attr, layer.mInsetR);
  //                    break;
  //                case R.styleable.LayerDrawableItem_bottom:
  //                    layer.mInsetB = a.getDimensionPixelOffset(attr, layer.mInsetB);
  //                    break;
  //                case R.styleable.LayerDrawableItem_start:
  //                    layer.mInsetS = a.getDimensionPixelOffset(attr, layer.mInsetS);
  //                    break;
  //                case R.styleable.LayerDrawableItem_end:
  //                    layer.mInsetE = a.getDimensionPixelOffset(attr, layer.mInsetE);
  //                    break;
  //                case R.styleable.LayerDrawableItem_width:
  //                    layer.mWidth = a.getDimensionPixelSize(attr, layer.mWidth);
  //                    break;
  //                case R.styleable.LayerDrawableItem_height:
  //                    layer.mHeight = a.getDimensionPixelSize(attr, layer.mHeight);
  //                    break;
  //                case R.styleable.LayerDrawableItem_gravity:
  //                    layer.mGravity = a.getInteger(attr, layer.mGravity);
  //                    break;
  //                case R.styleable.LayerDrawableItem_id:
  //                    layer.mId = a.getResourceId(attr, layer.mId);
  //                    break;
  //            }
  //        }
  //
  //        final Drawable dr = a.getDrawable(R.styleable.LayerDrawableItem_drawable);
  //        if (dr != null) {
  //            layer.mDrawable = dr;
  //        }
  //    }
  override fun canApplyTheme(): Boolean {
    return false
    //        return (mLayerState != null && mLayerState.canApplyTheme()) || super.canApplyTheme();
  }

  /**
   * Adds a new layer at the end of list of layers and returns its index.
   *
   * @param layer The layer to add.
   * @return The index of the layer.
   */
  fun addLayer(layer: ChildDrawable): Int {
    val st = mLayerState
    val N = if (st.mChildren != null) st.mChildren!!.size else 0
    val i = st.mNum
    if (i >= N) {
      val nu = arrayOfNulls<ChildDrawable>(N + 10)
      if (i > 0) {
        System.arraycopy(st.mChildren!!, 0, nu, 0, i)
      }
      st.mChildren = nu
    }
    st.mChildren!![i] = layer
    st.mNum++
    st.invalidateCache()
    return i
  }

  /**
   * Add a new layer to this drawable. The new layer is identified by an id.
   *
   * @param dr         The drawable to add as a layer.
   * @param themeAttrs Theme attributes extracted from the layer.
   * @param id         The id of the new layer.
   * @param left       The left padding of the new layer.
   * @param top        The top padding of the new layer.
   * @param right      The right padding of the new layer.
   * @param bottom     The bottom padding of the new layer.
   */
  fun addLayer(
    dr: Drawable, themeAttrs: IntArray?, id: Int,
    left: Int, top: Int, right: Int, bottom: Int
  ): ChildDrawable {
    val childDrawable = createLayer(dr)
    childDrawable.mId = id
    childDrawable.mThemeAttrs = themeAttrs
    DrawableCompat.setAutoMirrored(childDrawable.mDrawable!!, DrawableCompat.isAutoMirrored(this))
    childDrawable.mInsetL = left
    childDrawable.mInsetT = top
    childDrawable.mInsetR = right
    childDrawable.mInsetB = bottom
    addLayer(childDrawable)
    mLayerState.mChildrenChangingConfigurations = mLayerState.mChildrenChangingConfigurations or dr.changingConfigurations
    dr.callback = this
    return childDrawable
  }

  private fun createLayer(dr: Drawable): ChildDrawable {
    val layer = ChildDrawable(mLayerState.mDensity)
    layer.mDrawable = dr
    return layer
  }

  /**
   * Adds a new layer containing the specified `drawable` to the end of
   * the layer list and returns its index.
   *
   * @param dr The drawable to add as a new layer.
   * @return The index of the new layer.
   */
  fun addLayer(dr: Drawable): Int {
    val layer = createLayer(dr)
    val index = addLayer(layer)
    ensurePadding()
    refreshChildPadding(index, layer)
    return index
  }

  /**
   * Looks for a layer with the given ID and returns its [Drawable].
   *
   *
   * If multiple layers are found for the given ID, returns the
   * [Drawable] for the matching layer at the highest index.
   *
   * @param id The layer ID to search for.
   * @return The [Drawable] for the highest-indexed layer that has the
   * given ID, or null if not found.
   */
  fun findDrawableByLayerId(id: Int): Drawable? {
    val layers = mLayerState.mChildren
    for (i in mLayerState.mNum - 1 downTo 0) {
      if (layers!![i]!!.mId == id) {
        return layers[i]!!.mDrawable
      }
    }
    return null
  }

  /**
   * Sets the ID of a layer.
   *
   * @param index The index of the layer to modify, must be in the range
   * `0...getNumberOfLayers()-1`.
   * @param id    The id to assign to the layer.
   * @attr ref android.R.styleable#LayerDrawableItem_id
   * @see getId
   */
  fun setId(index: Int, id: Int) {
    mLayerState.mChildren!![index]!!.mId = id
  }

  /**
   * Returns the ID of the specified layer.
   *
   * @param index The index of the layer, must be in the range
   * `0...getNumberOfLayers()-1`.
   * @return The id of the layer or [View.NO_ID] if the
   * layer has no id.
   * @attr ref android.R.styleable#LayerDrawableItem_id
   * @see setId
   */
  fun getId(index: Int): Int {
    if (index >= mLayerState.mNum) {
      throw IndexOutOfBoundsException()
    }
    return mLayerState.mChildren!![index]!!.mId
  }

  /**
   * Replaces the [Drawable] for the layer with the given id.
   *
   * @param id       The layer ID to search for.
   * @param drawable The replacement [Drawable].
   * @return Whether the [Drawable] was replaced (could return false if
   * the id was not found).
   */
  fun setDrawableByLayerId(id: Int, drawable: Drawable?): Boolean {
    val index = findIndexByLayerId(id)
    if (index < 0) {
      return false
    }
    setDrawable(index, drawable)
    return true
  }

  /**
   * Returns the layer with the specified `id`.
   *
   *
   * If multiple layers have the same ID, returns the layer with the lowest
   * index.
   *
   * @param id The ID of the layer to return.
   * @return The index of the layer with the specified ID.
   */
  fun findIndexByLayerId(id: Int): Int {
    val layers = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val childDrawable = layers!![i]
      if (childDrawable!!.mId == id) {
        return i
      }
    }
    return -1
  }

  /**
   * Sets the drawable for the layer at the specified index.
   *
   * @param index    The index of the layer to modify, must be in the range
   * `0...getNumberOfLayers()-1`.
   * @param drawable The drawable to set for the layer.
   * @attr ref android.R.styleable#LayerDrawableItem_drawable
   * @see getDrawable
   */
  fun setDrawable(index: Int, drawable: Drawable?) {
    if (index >= mLayerState.mNum) {
      throw IndexOutOfBoundsException()
    }
    val layers = mLayerState.mChildren
    val childDrawable = layers!![index]
    if (childDrawable!!.mDrawable != null) {
      if (drawable != null) {
        val bounds = childDrawable.mDrawable!!.bounds
        drawable.bounds = bounds
      }
      childDrawable.mDrawable!!.callback = null
    }
    if (drawable != null) {
      drawable.callback = this
    }
    childDrawable.mDrawable = drawable
    mLayerState.invalidateCache()
    refreshChildPadding(index, childDrawable)
  }

  /**
   * Returns the drawable for the layer at the specified index.
   *
   * @param index The index of the layer, must be in the range
   * `0...getNumberOfLayers()-1`.
   * @return The [Drawable] at the specified layer index.
   * @attr ref android.R.styleable#LayerDrawableItem_drawable
   * @see setDrawable
   */
  fun getDrawable(index: Int): Drawable? {
    if (index >= mLayerState.mNum) {
      throw IndexOutOfBoundsException()
    }
    return mLayerState.mChildren!![index]!!.mDrawable
  }

  /**
   * Sets an explicit size for the specified layer.
   *
   *
   * **Note:** Setting an explicit layer size changes the
   * default layer gravity behavior. See [.setLayerGravity]
   * for more information.
   *
   * @param index the index of the layer to adjust
   * @param w     width in pixels, or -1 to use the intrinsic width
   * @param h     height in pixels, or -1 to use the intrinsic height
   * @attr ref android.R.styleable#LayerDrawableItem_width
   * @attr ref android.R.styleable#LayerDrawableItem_height
   * @see getLayerWidth
   * @see getLayerHeight
   */
  fun setLayerSize(index: Int, w: Int, h: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mWidth = w
    childDrawable.mHeight = h
  }

  /**
   * @param index the index of the layer to adjust
   * @param w     width in pixels, or -1 to use the intrinsic width
   * @attr ref android.R.styleable#LayerDrawableItem_width
   */
  fun setLayerWidth(index: Int, w: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mWidth = w
  }

  /**
   * @param index the index of the drawable to adjust
   * @return the explicit width of the layer, or -1 if not specified
   * @attr ref android.R.styleable#LayerDrawableItem_width
   * @see setLayerSize
   */
  fun getLayerWidth(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mWidth
  }

  /**
   * @param index the index of the layer to adjust
   * @param h     height in pixels, or -1 to use the intrinsic height
   * @attr ref android.R.styleable#LayerDrawableItem_height
   */
  fun setLayerHeight(index: Int, h: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mHeight = h
  }

  /**
   * @param index the index of the drawable to adjust
   * @return the explicit height of the layer, or -1 if not specified
   * @attr ref android.R.styleable#LayerDrawableItem_height
   * @see setLayerSize
   */
  fun getLayerHeight(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mHeight
  }

  /**
   * Sets the gravity used to position or stretch the specified layer within
   * its container. Gravity is applied after any layer insets (see
   * [.setLayerInset]) or padding (see
   * [.setPaddingMode]).
   *
   *
   * If gravity is specified as [Gravity.NO_GRAVITY], the default
   * behavior depends on whether an explicit width or height has been set
   * (see [.setLayerSize]), If a dimension is not set,
   * gravity in that direction defaults to [Gravity.FILL_HORIZONTAL] or
   * [Gravity.FILL_VERTICAL]; otherwise, gravity in that direction
   * defaults to [Gravity.LEFT] or [Gravity.TOP].
   *
   * @param index   the index of the drawable to adjust
   * @param gravity the gravity to set for the layer
   * @attr ref android.R.styleable#LayerDrawableItem_gravity
   * @see getLayerGravity
   */
  fun setLayerGravity(index: Int, gravity: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mGravity = gravity
  }

  /**
   * @param index the index of the layer
   * @return the gravity used to position or stretch the specified layer
   * within its container
   * @attr ref android.R.styleable#LayerDrawableItem_gravity
   * @see setLayerGravity
   */
  fun getLayerGravity(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mGravity
  }

  /**
   * Specifies the insets in pixels for the drawable at the specified index.
   *
   * @param index the index of the drawable to adjust
   * @param l     number of pixels to add to the left bound
   * @param t     number of pixels to add to the top bound
   * @param r     number of pixels to subtract from the right bound
   * @param b     number of pixels to subtract from the bottom bound
   * @attr ref android.R.styleable#LayerDrawableItem_left
   * @attr ref android.R.styleable#LayerDrawableItem_top
   * @attr ref android.R.styleable#LayerDrawableItem_right
   * @attr ref android.R.styleable#LayerDrawableItem_bottom
   */
  fun setLayerInset(index: Int, l: Int, t: Int, r: Int, b: Int) {
    setLayerInsetInternal(index, l, t, r, b, INSET_UNDEFINED, INSET_UNDEFINED)
  }

  /**
   * Specifies the relative insets in pixels for the drawable at the
   * specified index.
   *
   * @param index the index of the layer to adjust
   * @param s     number of pixels to inset from the start bound
   * @param t     number of pixels to inset from the top bound
   * @param e     number of pixels to inset from the end bound
   * @param b     number of pixels to inset from the bottom bound
   * @attr ref android.R.styleable#LayerDrawableItem_start
   * @attr ref android.R.styleable#LayerDrawableItem_top
   * @attr ref android.R.styleable#LayerDrawableItem_end
   * @attr ref android.R.styleable#LayerDrawableItem_bottom
   */
  fun setLayerInsetRelative(index: Int, s: Int, t: Int, e: Int, b: Int) {
    setLayerInsetInternal(index, 0, t, 0, b, s, e)
  }

  /**
   * @param index the index of the layer to adjust
   * @param l     number of pixels to inset from the left bound
   * @attr ref android.R.styleable#LayerDrawableItem_left
   */
  fun setLayerInsetLeft(index: Int, l: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetL = l
  }

  /**
   * @param index the index of the layer
   * @return number of pixels to inset from the left bound
   * @attr ref android.R.styleable#LayerDrawableItem_left
   */
  fun getLayerInsetLeft(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetL
  }

  /**
   * @param index the index of the layer to adjust
   * @param r     number of pixels to inset from the right bound
   * @attr ref android.R.styleable#LayerDrawableItem_right
   */
  fun setLayerInsetRight(index: Int, r: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetR = r
  }

  /**
   * @param index the index of the layer
   * @return number of pixels to inset from the right bound
   * @attr ref android.R.styleable#LayerDrawableItem_right
   */
  fun getLayerInsetRight(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetR
  }

  /**
   * @param index the index of the layer to adjust
   * @param t     number of pixels to inset from the top bound
   * @attr ref android.R.styleable#LayerDrawableItem_top
   */
  fun setLayerInsetTop(index: Int, t: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetT = t
  }

  /**
   * @param index the index of the layer
   * @return number of pixels to inset from the top bound
   * @attr ref android.R.styleable#LayerDrawableItem_top
   */
  fun getLayerInsetTop(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetT
  }

  /**
   * @param index the index of the layer to adjust
   * @param b     number of pixels to inset from the bottom bound
   * @attr ref android.R.styleable#LayerDrawableItem_bottom
   */
  fun setLayerInsetBottom(index: Int, b: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetB = b
  }

  /**
   * @param index the index of the layer
   * @return number of pixels to inset from the bottom bound
   * @attr ref android.R.styleable#LayerDrawableItem_bottom
   */
  fun getLayerInsetBottom(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetB
  }

  /**
   * @param index the index of the layer to adjust
   * @param s     number of pixels to inset from the start bound
   * @attr ref android.R.styleable#LayerDrawableItem_start
   */
  fun setLayerInsetStart(index: Int, s: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetS = s
  }

  /**
   * @param index the index of the layer
   * @return the number of pixels to inset from the start bound, or
   * [.INSET_UNDEFINED] if not specified
   * @attr ref android.R.styleable#LayerDrawableItem_start
   */
  fun getLayerInsetStart(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetS
  }

  /**
   * @param index the index of the layer to adjust
   * @param e     number of pixels to inset from the end bound, or
   * [.INSET_UNDEFINED] if not specified
   * @attr ref android.R.styleable#LayerDrawableItem_end
   */
  fun setLayerInsetEnd(index: Int, e: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetE = e
  }

  /**
   * @param index the index of the layer
   * @return number of pixels to inset from the end bound
   * @attr ref android.R.styleable#LayerDrawableItem_end
   */
  fun getLayerInsetEnd(index: Int): Int {
    val childDrawable = mLayerState.mChildren!![index]
    return childDrawable!!.mInsetE
  }

  private fun setLayerInsetInternal(index: Int, l: Int, t: Int, r: Int, b: Int, s: Int, e: Int) {
    val childDrawable = mLayerState.mChildren!![index]
    childDrawable!!.mInsetL = l
    childDrawable.mInsetT = t
    childDrawable.mInsetR = r
    childDrawable.mInsetB = b
    childDrawable.mInsetS = s
    childDrawable.mInsetE = e
  }
  /**
   * @return the current padding mode
   * @attr ref android.R.styleable#LayerDrawable_paddingMode
   * @see setPaddingMode
   */
  /**
   * Specifies how layer padding should affect the bounds of subsequent
   * layers. The default value is [.PADDING_MODE_NEST].
   *
   * padding mode, one of:
   *
   *  * [.PADDING_MODE_NEST] to nest each layer inside the
   * padding of the previous layer
   *  * [.PADDING_MODE_STACK] to stack each layer directly
   * atop the previous layer
   *
   * @attr ref android.R.styleable#LayerDrawable_paddingMode
   * @see getPaddingMode
   */
  var paddingMode: Int
    get() = mLayerState.mPaddingMode
    set(mode) {
      if (mLayerState.mPaddingMode != mode) {
        mLayerState.mPaddingMode = mode
      }
    }

  /**
   * Temporarily suspends child invalidation.
   *
   * @see resumeChildInvalidation
   */
  private fun suspendChildInvalidation() {
    mSuspendChildInvalidation = true
  }

  /**
   * Resumes child invalidation after suspension, immediately performing an
   * invalidation if one was requested by a child during suspension.
   *
   * @see suspendChildInvalidation
   */
  private fun resumeChildInvalidation() {
    mSuspendChildInvalidation = false
    if (mChildRequestedInvalidation) {
      mChildRequestedInvalidation = false
      invalidateSelf()
    }
  }

  override fun invalidateDrawable(who: Drawable) {
    if (mSuspendChildInvalidation) {
      mChildRequestedInvalidation = true
    } else {
      invalidateSelf()
    }
  }

  override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
    scheduleSelf(what, `when`)
  }

  override fun unscheduleDrawable(who: Drawable, what: Runnable) {
    unscheduleSelf(what)
  }

  override fun draw(canvas: Canvas) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.draw(canvas)
    }
  }

  override fun getChangingConfigurations(): Int {
    return super.getChangingConfigurations() or mLayerState.changingConfigurations
  }

  override fun getPadding(padding: Rect): Boolean {
    val layerState = mLayerState
    if (layerState.mPaddingMode == PADDING_MODE_NEST) {
      computeNestedPadding(padding)
    } else {
      computeStackedPadding(padding)
    }
    val paddingT = layerState.mPaddingTop
    val paddingB = layerState.mPaddingBottom

    // Resolve padding for RTL. Relative padding overrides absolute
    // padding.
    val isLayoutRtl = DrawableCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    val paddingRtlL = if (isLayoutRtl) layerState.mPaddingEnd else layerState.mPaddingStart
    val paddingRtlR = if (isLayoutRtl) layerState.mPaddingStart else layerState.mPaddingEnd
    val paddingL = if (paddingRtlL >= 0) paddingRtlL else layerState.mPaddingLeft
    val paddingR = if (paddingRtlR >= 0) paddingRtlR else layerState.mPaddingRight

    // If padding was explicitly specified (e.g. not -1) then override the
    // computed padding in that dimension.
    if (paddingL >= 0) {
      padding.left = paddingL
    }
    if (paddingT >= 0) {
      padding.top = paddingT
    }
    if (paddingR >= 0) {
      padding.right = paddingR
    }
    if (paddingB >= 0) {
      padding.bottom = paddingB
    }
    return padding.left != 0 || padding.top != 0 || padding.right != 0 || padding.bottom != 0
  }

  /**
   * Sets the absolute padding.
   *
   *
   * If padding in a dimension is specified as `-1`, the resolved
   * padding will use the value computed according to the padding mode (see
   * [.setPaddingMode]).
   *
   *
   * Calling this method clears any relative padding values previously set
   * using [.setPaddingRelative].
   *
   * @param left   the left padding in pixels, or -1 to use computed padding
   * @param top    the top padding in pixels, or -1 to use computed padding
   * @param right  the right padding in pixels, or -1 to use computed padding
   * @param bottom the bottom padding in pixels, or -1 to use computed
   * padding
   * @attr ref android.R.styleable#LayerDrawable_paddingLeft
   * @attr ref android.R.styleable#LayerDrawable_paddingTop
   * @attr ref android.R.styleable#LayerDrawable_paddingRight
   * @attr ref android.R.styleable#LayerDrawable_paddingBottom
   * @see setPaddingRelative
   */
  fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
    val layerState = mLayerState
    layerState.mPaddingLeft = left
    layerState.mPaddingTop = top
    layerState.mPaddingRight = right
    layerState.mPaddingBottom = bottom

    // Clear relative padding values.
    layerState.mPaddingStart = -1
    layerState.mPaddingEnd = -1
  }

  /**
   * Sets the relative padding.
   *
   *
   * If padding in a dimension is specified as `-1`, the resolved
   * padding will use the value computed according to the padding mode (see
   * [.setPaddingMode]).
   *
   *
   * Calling this method clears any absolute padding values previously set
   * using [.setPadding].
   *
   * @param start  the start padding in pixels, or -1 to use computed padding
   * @param top    the top padding in pixels, or -1 to use computed padding
   * @param end    the end padding in pixels, or -1 to use computed padding
   * @param bottom the bottom padding in pixels, or -1 to use computed
   * padding
   * @attr ref android.R.styleable#LayerDrawable_paddingStart
   * @attr ref android.R.styleable#LayerDrawable_paddingTop
   * @attr ref android.R.styleable#LayerDrawable_paddingEnd
   * @attr ref android.R.styleable#LayerDrawable_paddingBottom
   * @see setPadding
   */
  fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
    val layerState = mLayerState
    layerState.mPaddingStart = start
    layerState.mPaddingTop = top
    layerState.mPaddingEnd = end
    layerState.mPaddingBottom = bottom

    // Clear absolute padding values.
    layerState.mPaddingLeft = -1
    layerState.mPaddingRight = -1
  }

  private fun computeNestedPadding(padding: Rect) {
    padding.left = 0
    padding.top = 0
    padding.right = 0
    padding.bottom = 0

    // Add all the padding.
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      refreshChildPadding(i, array!![i])
      padding.left += mPaddingL!![i]
      padding.top += mPaddingT!![i]
      padding.right += mPaddingR!![i]
      padding.bottom += mPaddingB!![i]
    }
  }

  private fun computeStackedPadding(padding: Rect) {
    padding.left = 0
    padding.top = 0
    padding.right = 0
    padding.bottom = 0

    // Take the max padding.
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      refreshChildPadding(i, array!![i])
      padding.left = padding.left.coerceAtLeast(mPaddingL!![i])
      padding.top = padding.top.coerceAtLeast(mPaddingT!![i])
      padding.right = padding.right.coerceAtLeast(mPaddingR!![i])
      padding.bottom = padding.bottom.coerceAtLeast(mPaddingB!![i])
    }
  }

  /**
   * Populates `outline` with the first available (non-empty) layer outline.
   *
   * @param outline Outline in which to place the first available layer outline
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun getOutline(outline: Outline) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null) {
        dr.getOutline(outline)
        if (!outline.isEmpty) {
          return
        }
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun setHotspot(x: Float, y: Float) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setHotspot(x, y)
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun setHotspotBounds(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setHotspotBounds(left, top, right, bottom)
    }
    if (mHotspotBounds == null) {
      mHotspotBounds = Rect(left, top, right, bottom)
    } else {
      mHotspotBounds!![left, top, right] = bottom
    }
  }

  override fun getHotspotBounds(outRect: Rect) {
    if (mHotspotBounds != null) {
      outRect.set(mHotspotBounds!!)
    } else {
      super.getHotspotBounds(outRect)
    }
  }

  override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
    val changed = super.setVisible(visible, restart)
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setVisible(visible, restart)
    }
    return changed
  }

  override fun setDither(dither: Boolean) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setDither(dither)
    }
  }

  override fun setAlpha(alpha: Int) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null) {
        dr.alpha = alpha
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) override fun getAlpha(): Int {
    val dr = firstNonNullDrawable
    return dr?.alpha ?: super.getAlpha()
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null) {
        dr.colorFilter = colorFilter
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun setTintList(tint: ColorStateList?) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setTintList(tint)
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun setTintMode(tintMode: PorterDuff.Mode?) {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.setTintMode(tintMode)
    }
  }

  private val firstNonNullDrawable: Drawable?
    get() {
      val array = mLayerState.mChildren
      val N = mLayerState.mNum
      for (i in 0 until N) {
        val dr = array!![i]!!.mDrawable
        if (dr != null) {
          return dr
        }
      }
      return null
    }

  /**
   * Sets the opacity of this drawable directly instead of collecting the
   * states from the layers.
   *
   * @param opacity The opacity to use, or [                PixelFormat.UNKNOWN][PixelFormat.UNKNOWN] for the default behavior
   * @see PixelFormat.UNKNOWN
   *
   * @see PixelFormat.TRANSLUCENT
   *
   * @see PixelFormat.TRANSPARENT
   *
   * @see PixelFormat.OPAQUE
   */
  fun setOpacity(opacity: Int) {
    mLayerState.mOpacityOverride = opacity
  }

  override fun getOpacity(): Int {
    return if (mLayerState.mOpacityOverride != PixelFormat.UNKNOWN) {
      mLayerState.mOpacityOverride
    } else mLayerState.opacity
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) override fun setAutoMirrored(mirrored: Boolean) {
    mLayerState.mAutoMirrored = mirrored
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null) {
        dr.isAutoMirrored = mirrored
      }
    }
  }

  override fun isAutoMirrored(): Boolean {
    return mLayerState.mAutoMirrored
  }

  override fun jumpToCurrentState() {
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      dr?.jumpToCurrentState()
    }
  }

  override fun isStateful(): Boolean {
    return mLayerState.isStateful
  }

  override fun onStateChange(state: IntArray): Boolean {
    var changed = false
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null && dr.isStateful && dr.setState(state)) {
        refreshChildPadding(i, array[i])
        changed = true
      }
    }
    if (changed) {
      updateLayerBounds(bounds)
    }
    return changed
  }

  override fun onLevelChange(level: Int): Boolean {
    var changed = false
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null && dr.setLevel(level)) {
        refreshChildPadding(i, array[i])
        changed = true
      }
    }
    if (changed) {
      updateLayerBounds(bounds)
    }
    return changed
  }

  override fun onBoundsChange(bounds: Rect) {
    updateLayerBounds(bounds)
  }

  private fun updateLayerBounds(bounds: Rect) {
    try {
      suspendChildInvalidation()
      updateLayerBoundsInternal(bounds)
    } finally {
      resumeChildInvalidation()
    }
  }

  private fun updateLayerBoundsInternal(bounds: Rect) {
    var paddingL = 0
    var paddingT = 0
    var paddingR = 0
    var paddingB = 0
    val outRect = mTmpOutRect
    val layoutDirection = DrawableCompat.getLayoutDirection(this)
    val isLayoutRtl = layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
    val isPaddingNested = mLayerState.mPaddingMode == PADDING_MODE_NEST
    val array = mLayerState.mChildren
    var i = 0
    val count = mLayerState.mNum
    while (i < count) {
      val r = array!![i]
      val d = r!!.mDrawable
      if (d == null) {
        i++
        continue
      }
      val insetT = r.mInsetT
      val insetB = r.mInsetB

      // Resolve insets for RTL. Relative insets override absolute
      // insets.
      val insetRtlL = if (isLayoutRtl) r.mInsetE else r.mInsetS
      val insetRtlR = if (isLayoutRtl) r.mInsetS else r.mInsetE
      val insetL = if (insetRtlL == INSET_UNDEFINED) r.mInsetL else insetRtlL
      val insetR = if (insetRtlR == INSET_UNDEFINED) r.mInsetR else insetRtlR

      // Establish containing region based on aggregate padding and
      // requested insets for the current layer.
      val container = mTmpContainer
      container[bounds.left + insetL + paddingL, bounds.top + insetT + paddingT, bounds.right - insetR - paddingR] = bounds.bottom - insetB - paddingB

      // Compute a reasonable default gravity based on the intrinsic and
      // explicit dimensions, if specified.
      val intrinsicW = d.intrinsicWidth
      val intrinsicH = d.intrinsicHeight
      val layerW = r.mWidth
      val layerH = r.mHeight
      val gravity = resolveGravity(r.mGravity, layerW, layerH, intrinsicW, intrinsicH)

      // Explicit dimensions override intrinsic dimensions.
      val resolvedW = if (layerW < 0) intrinsicW else layerW
      val resolvedH = if (layerH < 0) intrinsicH else layerH
      GravityCompat.apply(gravity, resolvedW, resolvedH, container, outRect, layoutDirection)
      d.bounds = outRect
      if (isPaddingNested) {
        paddingL += mPaddingL!![i]
        paddingR += mPaddingR!![i]
        paddingT += mPaddingT!![i]
        paddingB += mPaddingB!![i]
      }
      i++
    }
  }

  override fun getIntrinsicWidth(): Int {
    var width = -1
    var padL = 0
    var padR = 0
    val nest = mLayerState.mPaddingMode == PADDING_MODE_NEST
    val isLayoutRtl = DrawableCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val r = array!![i]
      if (r!!.mDrawable == null) {
        continue
      }

      // Take the resolved layout direction into account. If start / end
      // padding are defined, they will be resolved (hence overriding) to
      // left / right or right / left depending on the resolved layout
      // direction. If start / end padding are not defined, use the
      // left / right ones.
      val insetRtlL = if (isLayoutRtl) r.mInsetE else r.mInsetS
      val insetRtlR = if (isLayoutRtl) r.mInsetS else r.mInsetE
      val insetL = if (insetRtlL == INSET_UNDEFINED) r.mInsetL else insetRtlL
      val insetR = if (insetRtlR == INSET_UNDEFINED) r.mInsetR else insetRtlR

      // Don't apply padding and insets for children that don't have
      // an intrinsic dimension.
      val minWidth = if (r.mWidth < 0) r.mDrawable!!.intrinsicWidth else r.mWidth
      val w = if (minWidth < 0) -1 else minWidth + insetL + insetR + padL + padR
      if (w > width) {
        width = w
      }
      if (nest) {
        padL += mPaddingL!![i]
        padR += mPaddingR!![i]
      }
    }
    return width
  }

  override fun getIntrinsicHeight(): Int {
    var height = -1
    var padT = 0
    var padB = 0
    val nest = mLayerState.mPaddingMode == PADDING_MODE_NEST
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val r = array!![i]
      if (r!!.mDrawable == null) {
        continue
      }

      // Don't apply padding and insets for children that don't have
      // an intrinsic dimension.
      val minHeight = if (r.mHeight < 0) r.mDrawable!!.intrinsicHeight else r.mHeight
      val h = if (minHeight < 0) -1 else minHeight + r.mInsetT + r.mInsetB + padT + padB
      if (h > height) {
        height = h
      }
      if (nest) {
        padT += mPaddingT!![i]
        padB += mPaddingB!![i]
      }
    }
    return height
  }

  /**
   * Refreshes the cached padding values for the specified child.
   *
   * @return true if the child's padding has changed
   */
  private fun refreshChildPadding(i: Int, r: ChildDrawable?): Boolean {
    if (r!!.mDrawable != null) {
      val rect = mTmpRect
      r.mDrawable!!.getPadding(rect)
      if (rect.left != mPaddingL!![i] || rect.top != mPaddingT!![i] || rect.right != mPaddingR!![i] || rect.bottom != mPaddingB!![i]) {
        mPaddingL!![i] = rect.left
        mPaddingT!![i] = rect.top
        mPaddingR!![i] = rect.right
        mPaddingB!![i] = rect.bottom
        return true
      }
    }
    return false
  }

  /**
   * Ensures the child padding caches are large enough.
   */
  fun ensurePadding() {
    val N = mLayerState.mNum
    if (mPaddingL != null && mPaddingL!!.size >= N) {
      return
    }
    mPaddingL = IntArray(N)
    mPaddingT = IntArray(N)
    mPaddingR = IntArray(N)
    mPaddingB = IntArray(N)
  }

  fun refreshPadding() {
    val N = mLayerState.mNum
    val array = mLayerState.mChildren
    for (i in 0 until N) {
      refreshChildPadding(i, array!![i])
    }
  }

  override fun getConstantState(): ConstantState? {
    if (mLayerState.canConstantState()) {
      mLayerState.mChangingConfigurations = changingConfigurations
      return mLayerState
    }
    return null
  }

  override fun mutate(): Drawable {
    if (!mMutated && super.mutate() === this) {
      mLayerState = createConstantState(mLayerState, null)
      val array = mLayerState.mChildren
      val N = mLayerState.mNum
      for (i in 0 until N) {
        val dr = array!![i]!!.mDrawable
        dr?.mutate()
      }
      mMutated = true
    }
    return this
  }

  override fun onLayoutDirectionChanged(layoutDirection: Int): Boolean {
    var changed = false
    val array = mLayerState.mChildren
    val N = mLayerState.mNum
    for (i in 0 until N) {
      val dr = array!![i]!!.mDrawable
      if (dr != null) {
        changed = changed or DrawableCompat.setLayoutDirection(dr, layoutDirection)
      }
    }
    updateLayerBounds(bounds)
    return changed
  }

  class ChildDrawable {
    var mDrawable: Drawable? = null
    var mThemeAttrs: IntArray? = null
    var mDensity = DisplayMetrics.DENSITY_DEFAULT
    var mInsetL = 0
    var mInsetT = 0
    var mInsetR = 0
    var mInsetB = 0
    var mInsetS = INSET_UNDEFINED
    var mInsetE = INSET_UNDEFINED
    var mWidth = -1
    var mHeight = -1
    var mGravity = Gravity.NO_GRAVITY
    var mId = View.NO_ID

    constructor(density: Int) {
      mDensity = density
    }

    constructor(
      orig: ChildDrawable, owner: LayerDrawableCompat,
      res: Resources?
    ) {
      val dr = orig.mDrawable
      val clone: Drawable?
      if (dr != null) {
        val cs = dr.constantState
        clone = when {
          cs == null -> dr
          res != null -> cs.newDrawable(res)
          else -> cs.newDrawable()
        }
        clone.callback = owner
        DrawableCompat.setLayoutDirection(clone, DrawableCompat.getLayoutDirection(dr))
        clone.bounds = dr.bounds
        clone.level = dr.level
      } else {
        clone = null
      }
      mDrawable = clone
      mThemeAttrs = orig.mThemeAttrs
      mInsetL = orig.mInsetL
      mInsetT = orig.mInsetT
      mInsetR = orig.mInsetR
      mInsetB = orig.mInsetB
      mInsetS = orig.mInsetS
      mInsetE = orig.mInsetE
      mWidth = orig.mWidth
      mHeight = orig.mHeight
      mGravity = orig.mGravity
      mId = orig.mId
      mDensity = DrawableInternal.resolveDensity(res, orig.mDensity)
      if (orig.mDensity != mDensity) {
        applyDensityScaling(orig.mDensity, mDensity)
      }
    }

    fun canApplyTheme(): Boolean {
      return (mThemeAttrs != null
        || mDrawable != null && DrawableCompat.canApplyTheme(mDrawable!!))
    }

    fun setDensity(targetDensity: Int) {
      if (mDensity != targetDensity) {
        val sourceDensity = mDensity
        mDensity = targetDensity
        applyDensityScaling(sourceDensity, targetDensity)
      }
    }

    private fun applyDensityScaling(sourceDensity: Int, targetDensity: Int) {
      mInsetL = DrawableInternal.scaleFromDensity(mInsetL, sourceDensity, targetDensity, false)
      mInsetT = DrawableInternal.scaleFromDensity(mInsetT, sourceDensity, targetDensity, false)
      mInsetR = DrawableInternal.scaleFromDensity(mInsetR, sourceDensity, targetDensity, false)
      mInsetB = DrawableInternal.scaleFromDensity(mInsetB, sourceDensity, targetDensity, false)
      if (mInsetS != INSET_UNDEFINED) {
        mInsetS = DrawableInternal.scaleFromDensity(mInsetS, sourceDensity, targetDensity, false)
      }
      if (mInsetE != INSET_UNDEFINED) {
        mInsetE = DrawableInternal.scaleFromDensity(mInsetE, sourceDensity, targetDensity, false)
      }
      if (mWidth > 0) {
        mWidth = DrawableInternal.scaleFromDensity(mWidth, sourceDensity, targetDensity, true)
      }
      if (mHeight > 0) {
        mHeight = DrawableInternal.scaleFromDensity(mHeight, sourceDensity, targetDensity, true)
      }
    }
  }

  class LayerState(
    orig: LayerState?, owner: LayerDrawableCompat,
    res: Resources?
  ) : ConstantState() {
    var mThemeAttrs: IntArray? = null
    var mNum = 0
    var mChildren: Array<ChildDrawable?>? = null
    var mDensity: Int

    // These values all correspond to mDensity.
    var mPaddingTop = -1
    var mPaddingBottom = -1
    var mPaddingLeft = -1
    var mPaddingRight = -1
    var mPaddingStart = -1
    var mPaddingEnd = -1
    var mOpacityOverride = PixelFormat.UNKNOWN
    var mChangingConfigurations = 0
    var mChildrenChangingConfigurations = 0
    private var mHaveOpacity = false
    private var mOpacity = 0
    private var mHaveIsStateful = false
    private var mIsStateful = false
    var mAutoMirrored = false
    var mPaddingMode = PADDING_MODE_NEST
    fun setDensity(targetDensity: Int) {
      if (mDensity != targetDensity) {
        val sourceDensity = mDensity
        mDensity = targetDensity
        onDensityChanged(sourceDensity, targetDensity)
      }
    }

    fun onDensityChanged(sourceDensity: Int, targetDensity: Int) {
      applyDensityScaling(sourceDensity, targetDensity)
    }

    private fun applyDensityScaling(sourceDensity: Int, targetDensity: Int) {
      if (mPaddingLeft > 0) {
        mPaddingLeft = DrawableInternal.scaleFromDensity(
          mPaddingLeft, sourceDensity, targetDensity, false
        )
      }
      if (mPaddingTop > 0) {
        mPaddingTop = DrawableInternal.scaleFromDensity(
          mPaddingTop, sourceDensity, targetDensity, false
        )
      }
      if (mPaddingRight > 0) {
        mPaddingRight = DrawableInternal.scaleFromDensity(
          mPaddingRight, sourceDensity, targetDensity, false
        )
      }
      if (mPaddingBottom > 0) {
        mPaddingBottom = DrawableInternal.scaleFromDensity(
          mPaddingBottom, sourceDensity, targetDensity, false
        )
      }
      if (mPaddingStart > 0) {
        mPaddingStart = DrawableInternal.scaleFromDensity(
          mPaddingStart, sourceDensity, targetDensity, false
        )
      }
      if (mPaddingEnd > 0) {
        mPaddingEnd = DrawableInternal.scaleFromDensity(
          mPaddingEnd, sourceDensity, targetDensity, false
        )
      }
    }

    override fun canApplyTheme(): Boolean {
      if (mThemeAttrs != null || super.canApplyTheme()) {
        return true
      }
      val array = mChildren
      val N = mNum
      for (i in 0 until N) {
        val layer = array!![i]
        if (layer!!.canApplyTheme()) {
          return true
        }
      }
      return false
    }

    override fun newDrawable(): Drawable {
      return LayerDrawableCompat(this, null)
    }

    override fun newDrawable(res: Resources?): Drawable {
      return LayerDrawableCompat(this, res)
    }

    override fun getChangingConfigurations(): Int {
      return (mChangingConfigurations
        or mChildrenChangingConfigurations)
    }
    // Seek to the first non-null drawable.

    // Merge all remaining non-null drawables.
    val opacity: Int
      get() {
        if (mHaveOpacity) {
          return mOpacity
        }
        val array = mChildren
        val N = mNum

        // Seek to the first non-null drawable.
        var firstIndex = -1
        for (i in 0 until N) {
          if (array!![i]!!.mDrawable != null) {
            firstIndex = i
            break
          }
        }
        var op: Int
        op = if (firstIndex >= 0) {
          array!![firstIndex]!!.mDrawable!!.opacity
        } else {
          PixelFormat.TRANSPARENT
        }

        // Merge all remaining non-null drawables.
        for (i in firstIndex + 1 until N) {
          val dr = array!![i]!!.mDrawable
          if (dr != null) {
            op = resolveOpacity(op, dr.opacity)
          }
        }
        mOpacity = op
        mHaveOpacity = true
        return op
      }
    val isStateful: Boolean
      get() {
        if (mHaveIsStateful) {
          return mIsStateful
        }
        val array = mChildren
        val N = mNum
        var isStateful = false
        for (i in 0 until N) {
          val dr = array!![i]!!.mDrawable
          if (dr != null && dr.isStateful) {
            isStateful = true
            break
          }
        }
        mIsStateful = isStateful
        mHaveIsStateful = true
        return isStateful
      }

    fun canConstantState(): Boolean {
      val array = mChildren
      val N = mNum
      for (i in 0 until N) {
        val dr = array!![i]!!.mDrawable
        if (dr != null && dr.constantState == null) {
          return false
        }
      }

      // Don't cache the result, this method is not called very often.
      return true
    }

    fun invalidateCache() {
      mHaveOpacity = false
      mHaveIsStateful = false
    }

    init {
      mDensity = DrawableInternal.resolveDensity(res, orig?.mDensity ?: 0)
      if (orig != null) {
        val origChildDrawable = orig.mChildren
        val N = orig.mNum
        mNum = N
        mChildren = arrayOfNulls(N)
        mChangingConfigurations = orig.mChangingConfigurations
        mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations
        for (i in 0 until N) {
          val or = origChildDrawable!![i]
          mChildren!![i] = ChildDrawable(or!!, owner, res)
        }
        mHaveOpacity = orig.mHaveOpacity
        mOpacity = orig.mOpacity
        mHaveIsStateful = orig.mHaveIsStateful
        mIsStateful = orig.mIsStateful
        mAutoMirrored = orig.mAutoMirrored
        mPaddingMode = orig.mPaddingMode
        mThemeAttrs = orig.mThemeAttrs
        mPaddingTop = orig.mPaddingTop
        mPaddingBottom = orig.mPaddingBottom
        mPaddingLeft = orig.mPaddingLeft
        mPaddingRight = orig.mPaddingRight
        mPaddingStart = orig.mPaddingStart
        mPaddingEnd = orig.mPaddingEnd
        mOpacityOverride = orig.mOpacityOverride
        if (orig.mDensity != mDensity) {
          applyDensityScaling(orig.mDensity, mDensity)
        }
      } else {
        mNum = 0
        mChildren = null
      }
    }
  }

  companion object {
    /**
     * Padding mode used to nest each layer inside the padding of the previous
     * layer.
     *
     * @see setPaddingMode
     */
    const val PADDING_MODE_NEST = 0

    /**
     * Padding mode used to stack each layer directly atop the previous layer.
     *
     * @see setPaddingMode
     */
    const val PADDING_MODE_STACK = 1

    /**
     * Value used for undefined start and end insets.
     *
     * @see getLayerInsetStart
     * @see getLayerInsetEnd
     */
    const val INSET_UNDEFINED = Int.MIN_VALUE

    /**
     * Resolves layer gravity given explicit gravity and dimensions.
     *
     *
     * If the client hasn't specified a gravity but has specified an explicit
     * dimension, defaults to START or TOP. Otherwise, defaults to FILL to
     * preserve legacy behavior.
     *
     * @param gravity layer gravity
     * @param width   width of the layer if set, -1 otherwise
     * @param height  height of the layer if set, -1 otherwise
     * @return the default gravity for the layer
     */
    private fun resolveGravity(
      gravity: Int, width: Int, height: Int,
      intrinsicWidth: Int, intrinsicHeight: Int
    ): Int {
      var gravity = gravity
      if (!Gravity.isHorizontal(gravity)) {
        gravity = if (width < 0) {
          gravity or Gravity.FILL_HORIZONTAL
        } else {
          gravity or Gravity.START
        }
      }
      if (!Gravity.isVertical(gravity)) {
        gravity = if (height < 0) {
          gravity or Gravity.FILL_VERTICAL
        } else {
          gravity or Gravity.TOP
        }
      }

      // If a dimension if not specified, either implicitly or explicitly,
      // force FILL for that dimension's gravity. This ensures that colors
      // are handled correctly and ensures backward compatibility.
      if (width < 0 && intrinsicWidth < 0) {
        gravity = gravity or Gravity.FILL_HORIZONTAL
      }
      if (height < 0 && intrinsicHeight < 0) {
        gravity = gravity or Gravity.FILL_VERTICAL
      }
      return gravity
    }
  }

  /**
   * The one constructor to rule them all. This is called by all public
   * constructors to set the state and initialize local properties.
   */
  init {
    mLayerState = createConstantState(state, res)
    if (mLayerState.mNum > 0) {
      ensurePadding()
      refreshPadding()
    }
  }
}

internal object DrawableInternal {
  /**
   * Scales a floating-point pixel value from the source density to the
   * target density.
   *
   * @param pixels        the pixel value for use in source density
   * @param sourceDensity the source density
   * @param targetDensity the target density
   * @return the scaled pixel value for use in target density
   */
  fun scaleFromDensity(pixels: Float, sourceDensity: Int, targetDensity: Int): Float {
    return pixels * targetDensity / sourceDensity
  }

  /**
   * Scales a pixel value from the source density to the target density,
   * optionally handling the resulting pixel value as a size rather than an
   * offset.
   *
   *
   * A size conversion involves rounding the base value and ensuring that
   * a non-zero base value is at least one pixel in size.
   *
   *
   * An offset conversion involves simply truncating the base value to an
   * integer.
   *
   * @param pixels        the pixel value for use in source density
   * @param sourceDensity the source density
   * @param targetDensity the target density
   * @param isSize        `true` to handle the resulting scaled value as a
   * size, or `false` to handle it as an offset
   * @return the scaled pixel value for use in target density
   */
  fun scaleFromDensity(
    pixels: Int, sourceDensity: Int, targetDensity: Int, isSize: Boolean
  ): Int {
    if (pixels == 0 || sourceDensity == targetDensity) {
      return pixels
    }
    val result = pixels * targetDensity / sourceDensity.toFloat()
    if (!isSize) {
      return result.toInt()
    }
    val rounded = result.roundToInt()
    return when {
      rounded != 0 -> rounded
      pixels > 0 -> 1
      else ->-1
    }
  }

  fun resolveDensity(r: Resources?, parentDensity: Int): Int {
    val densityDpi = r?.displayMetrics?.densityDpi ?: parentDensity
    return if (densityDpi == 0) DisplayMetrics.DENSITY_DEFAULT else densityDpi
  }
}