package com.playbowdogs.neighbors.data.adapter

import android.media.AudioManager
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.data.model.OnGoingAppointment
import com.playbowdogs.neighbors.utils.GlideApp
import timber.log.Timber

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
    @BindingAdapter("userProfile")
    fun loadUserProfile(imageView: ImageView, onGoingAppointment: OnGoingAppointment) {
        if (onGoingAppointment.user == null) {
            imageView.visibility = View.GONE
        } else {
            imageView.visibility = View.VISIBLE
            GlideApp.with(imageView.context)
                .load(onGoingAppointment.user_profile_photo)
                .placeholder(R.drawable.ic_user)
                .dontAnimate()
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("videoUrl")
    fun loadVideo(videoView: VideoView, videoUrl: String?) {
        videoUrl?.let { url ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                videoView.setAudioFocusRequest(
                    AudioManager.AUDIOFOCUS_NONE)
            }
            videoView.setOnPreparedListener {
                it.setVolume(0f, 0f)
                it.start()
            }
            videoView.setVideoURI(url.toUri())
        }
    }
    
    @JvmStatic
    @BindingAdapter("appointmentHeader")
    fun loadText(materialTextView: MaterialTextView, user: String?) {
        if (user == null) {
            materialTextView.text = materialTextView.context.getString(R.string.appointment_header_none)
        } else {
            materialTextView.text = materialTextView.context.getString(R.string.appointment_header)
        }
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
