package com.playbowdogs.neighbors.data.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.playbowdogs.neighbors.utils.GlideApp

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("userType")
    fun loadUserType(imageView: ImageView, drawable: Int) {
        GlideApp.with(imageView.context)
            .load(drawable)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("CamImage")
    fun loadImage(imageView: ImageView, imageUrl: String?) {
        GlideApp.with(imageView.context)
            .load(imageUrl)
//            .placeholder(com.playbowdogs.neighbors.android.R.drawable.ic_user)
            .dontAnimate()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("formatPhone")
    fun setFormattedPhone(textView: TextView, phoneNumber: String?) {
        phoneNumber?.let {
            textView.text = when {
                phoneNumber.isNullOrEmpty() -> ""
                phoneNumber.contains("-") -> phoneNumber
                else -> {
                    StringBuilder(phoneNumber).insert(5, "-").insert(9, "-").toString()
                        .replace("+1", "")
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("formatAddress", "formatCity")
    fun setFormattedAddress(textView: TextView, address: String?, city: String?) {
        address?.let {
            textView.text = when {
                address.isNullOrEmpty() -> ""
                city.isNullOrEmpty() -> ""
                else -> {
                    val length = address.length
                    StringBuilder(address).insert(length, "\n$city, Ca")
                }
            }
        }
    }
}
