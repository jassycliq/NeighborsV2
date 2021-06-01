package com.playbowdogs.neighbors.data.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.playbowdogs.neighbors.data.viewHolder.ViewHolderFactory


class UserTypeDetailLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as ViewHolderFactory.UserTypeViewHolder)
                .getItemDetails()
        }
        return null
    }
}
