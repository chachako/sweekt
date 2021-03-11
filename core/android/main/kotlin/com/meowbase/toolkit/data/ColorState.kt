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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.toolkit.data

import android.annotation.SuppressLint

/*
 * author: 凛
 * date: 2020/10/1 下午16:50
 * github: https://github.com/RinOrz
 * description: Indicates color status
 */
inline class ColorState(val res: Int?) {
  companion object {
    /** When default state */
    val Normal = ColorState(null)

    /** When a view has input focus */
    val Focused = ColorState(android.R.attr.state_focused)

    /** When a view's window has input focus */
    val WindowFocused = ColorState(android.R.attr.state_window_focused)

    /** When a view is enabled */
    val Enabled = ColorState(android.R.attr.state_enabled)

    /**
     * State identifier indicating that the object may display a check mark.
     * @see [Checked] for the identifier that indicates
     * whether it is actually checked
     */
    val Checkable = ColorState(android.R.attr.state_checkable)

    /**
     * State identifier indicating that the object is currently checked.
     * @see [Checkable] for an additional identifier that can indicate
     * if any object may ever display a check
     * regardless of whether state_checked is currently set
     */
    val Checked = ColorState(android.R.attr.state_checked)

    /** When a view (or one of its parents) is currently selected */
    val Selected = ColorState(android.R.attr.state_selected)

    /** When the user is pressing down in a view */
    val Pressed = ColorState(android.R.attr.state_pressed)

    /**
     * When a view or its parent has been "activated" meaning the user has currently
     * marked it as being of interest.
     *
     * This is an alternative representation of
     * [Checked] for when the state should be propagated down the view hierarchy
     */
    val Activated = ColorState(android.R.attr.state_activated)

    /**
     * When a view or drawable is considered "active" by its host.
     *
     * Actual usage may vary between views
     * consult the host view documentation for details
     */
    val Active = ColorState(android.R.attr.state_active)

    /**
     * When a view or drawable is considered "single" by its host.
     *
     * Actual usage may vary between views
     * consult the host view documentation for details
     */
    val Single = ColorState(android.R.attr.state_single)

    /**
     * When a view or drawable is in the first position in an ordered set.
     *
     * Actual usage may vary between views
     * consult the host view documentation for details
     */
    val First = ColorState(android.R.attr.state_first)

    /**
     * When a view or drawable is in the middle position in an ordered set.
     *
     * Actual usage may vary between views
     * consult the host view documentation for details
     */
    val Middle = ColorState(android.R.attr.state_middle)

    /**
     * When a view or drawable is in the last position in an ordered set.
     *
     * Actual usage may vary between views
     * consult the host view documentation for details
     */
    val Last = ColorState(android.R.attr.state_last)

    /**
     * Indicating that the Drawable is in a view that is hardware accelerated.
     * This means that the device can at least render a full-screen scaled
     * bitmap with one layer of text and bitmaps
     * composited on top of it at 60fps.
     *
     * When this is set the `colorBackgroundCacheHint` will be
     * ignored even if it specifies a solid color
     * since that optimization is not needed
     */
    val Accelerated = ColorState(android.R.attr.state_accelerated)

    /** When a pointer is hovering over the view */
    val Hovered = ColorState(android.R.attr.state_hovered)

    /**
     * Indicating that the Drawable is in a view
     * that is capable of accepting a drop of the content
     * currently being manipulated in a drag-and-drop operation
     */
    val DragCanAccept = ColorState(android.R.attr.state_drag_can_accept)

    /**
     * Indicating that a drag operation (for which the Drawable's view is a valid recipient)
     * is currently positioned over the Drawable
     */
    val DragHovered = ColorState(android.R.attr.state_drag_hovered)
  }
}

/** Means this [ColorState] is reversed */
@SuppressLint("UseValueOf") fun ColorState.reversed(): ColorState = ColorState(-res!!)