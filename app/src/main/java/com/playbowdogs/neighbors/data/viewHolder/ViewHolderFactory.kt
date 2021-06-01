package com.playbowdogs.neighbors.data.viewHolder

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.data.adapter.GenericAdapter
import com.playbowdogs.neighbors.data.model.UserType
import com.playbowdogs.neighbors.databinding.RecyclerViewUserTypeBinding

object ViewHolderFactory {

    fun create(view: View, parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.recycler_view_user_type -> {
                val binder = RecyclerViewUserTypeBinding.inflate(
                    LayoutInflater.from(view.context),
                    parent,
                    false
                )
                UserTypeViewHolder(binder)
            }

            else -> {
                val binder = RecyclerViewUserTypeBinding.inflate(
                    LayoutInflater.from(view.context),
                    parent,
                    false
                )
                UserTypeViewHolder(binder)
            }
        }
    }

    class UserTypeViewHolder(
        private val binding: RecyclerViewUserTypeBinding,
    ) : RecyclerView.ViewHolder(binding.root), GenericAdapter.Binder<UserType> {

        override fun bind(data: UserType, isActivated: Boolean) {
            binding.userType = data
            itemView.isActivated = isActivated

            if (isActivated) {
                binding.userTypeCardView.setCardBackgroundColor(itemView.context.resources.getColor(
                    R.color.item_selected,
                    null))
            } else {
                binding.userTypeCardView.setCardBackgroundColor(itemView.context.resources.getColor(
                    data.backgroundColor,
                    null))
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = itemId
                override fun inSelectionHotspot(e: MotionEvent): Boolean {
                    return true
                }
            }
    }

//    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<Car> {
//
//        var textView: TextView
//        init {
//            textView = itemView.findViewById(R.id.carName)
//        }
//        override fun bind(car: Car) {
//            textView.text = car.name
//        }
//    }
}
