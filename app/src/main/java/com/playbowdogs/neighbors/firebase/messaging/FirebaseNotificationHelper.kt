package com.playbowdogs.neighbors.firebase.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class FirebaseNotificationHelper(val context: Context) {

    fun onNotificationReceived(it: RemoteMessage.Notification) {
        val builder: NotificationCompat.Builder? =
            if (it.imageUrl.toString().isBlank() or it.imageUrl.toString().isEmpty()) {
                messageNotificationBuilder(it.title, it.body, it.channelId)
            } else {
                messageNotificationBuilder(
                    it.title,
                    it.body,
                    it.channelId,
                    it.imageUrl.toString(),
                )
            }

        createDefaultChannels()

        when (it.channelId) {
            MESSAGE_CHANNEL_ID -> {
                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    if (builder != null) {
                        notify(MESSAGE_CHANNEL_ID_INT, builder.build())
                    }
                }
            }

            LIVE_CHANNEL_ID -> {
                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    if (builder != null) {
                        notify(LIVE_CHANNEL_ID_INT, builder.build())
                    }
                }
            }

            OTHER_CHANNEL_ID -> {
                /* TODO: Action for whatever we want to do for future notifications that may or
                    may not require a notification. */
            }
        }
    }

    private fun createDefaultChannels() {
        // Create Firebase Message channel
        createChannel(
            R.string.firebase_message_channel_name,
            R.string.firebase_message_channel_description,
            NotificationManager.IMPORTANCE_HIGH,
            MESSAGE_CHANNEL_ID
        )

        createChannel(
            R.string.live_video_channel_name,
            R.string.live_channel_description,
            NotificationManager.IMPORTANCE_MAX,
            LIVE_CHANNEL_ID
        )

        createChannel(
            R.string.other_notifications_channel_name,
            R.string.other_notifications_channel_description,
            NotificationManager.IMPORTANCE_DEFAULT,
            OTHER_CHANNEL_ID
        )
    }

    private fun messageNotificationBuilder(title: String?, body: String?, channelID: String?) =
        channelID?.let {
            NotificationCompat.Builder(context, it)
                .setSmallIcon(R.drawable.ic_playbow_logo_full_color)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setCategory("Private Messages")
                .setContentText(body)
//                .setContentIntent(createDeepLink())
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        }

    private fun messageNotificationBuilder(
        title: String?,
        body: String?,
        channelID: String?,
        image: String?,
    ) =
        channelID?.let {
            val futureTarget = GlideApp.with(context)
                .asBitmap()
                .load(image)
                .submit()
            val bitmap = futureTarget.get()

            NotificationCompat.Builder(context, it)
                .setSmallIcon(R.drawable.ic_playbow_logo_full_color)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setCategory("Private Messages")
                .setContentText(body)
                .setLargeIcon(bitmap)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
//                .setContentIntent(createDeepLink())
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        }

    private fun createChannel(
        channelName: Int,
        channelDescription: Int,
        channelImportance: Int,
        channelID: String,
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(channelName)
            val descriptionText = context.getString(channelDescription)
            val channel = NotificationChannel(channelID, name, channelImportance).apply {
                description = descriptionText
                lightColor = context.resources.getColor(R.color.white, null)
                importance = channelImportance
                vibrationPattern = longArrayOf(0L, 100L, 0L)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /* TODO: Fix DeepLink handling in [BottomNavActivity] in order to allow notifications to
        actually navigate to correct fragments */
//    private fun createDeepLink(): PendingIntent {
//        return NavDeepLinkBuilder(context)
//            .setComponentName(ComponentName.createRelative("com.playbowdogs.neighbors",
//                ".client.ui.bottomNav.BottomNavActivity"))
//            .setGraph(R.navigation.mobile_navigation)
//            .setDestination(R.id.navigation_camera_details)
//            .createPendingIntent()
//    }
}
