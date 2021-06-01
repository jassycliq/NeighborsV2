package com.playbowdogs.neighbors.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

// Recycler View to remove padding offset, otherwise fading edge abides by said offset.
class CustomRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int,
    ) : super(context, attributeSet, defStyleAttr)

    override fun isPaddingOffsetRequired(): Boolean {
        return true
    }

    override fun getTopPaddingOffset(): Int {
        return -paddingTop
    }

    override fun getBottomPaddingOffset(): Int {
        return paddingBottom
    }
}
