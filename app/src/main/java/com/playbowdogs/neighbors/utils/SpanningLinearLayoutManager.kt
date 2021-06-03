package com.playbowdogs.neighbors.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


class SpanningLinearLayoutManager(context: Context?) : GridLayoutManager(context, 1) {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(
        c: Context?,
        attrs: AttributeSet?,
    ): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        if (orientation == HORIZONTAL) {
            layoutParams.width =
                (horizontalSpace / itemCount.toDouble()).roundToInt()
        } else if (orientation == VERTICAL) {
            layoutParams.height =
                (verticalSpace / itemCount.toDouble()).roundToInt()
        }
        return layoutParams
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    private val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft
    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop
}
