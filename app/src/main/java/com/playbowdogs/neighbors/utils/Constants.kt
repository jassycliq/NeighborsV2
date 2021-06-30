package com.playbowdogs.neighbors.utils

import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips

const val SHARED_PREFERENCES: String = "default"
const val MESSAGE_CHANNEL_ID_INT = 0
const val MESSAGE_CHANNEL_ID = "FirebaseMessage"
const val OTHER_CHANNEL_ID = "Other"
const val LIVE_CHANNEL_ID_INT = 1
const val LIVE_CHANNEL_ID = "LiveView"
const val PLAYBOW_API_URL: String = "https://playbowtech.com/api/"
const val ANGELCAM_API_URL: String = "https://api.angelcam.com/v1/"
const val ACUITY_API_URL: String = "https://acuityscheduling.com/api/v1/"
const val ANGELCAM_CAMERA_ID: String = "98280"
const val ACUITY_API_USERNAME: String = "20178508"
const val ACUITY_API_KEY: String = "dd15a26fda7612e8cf0c27de557780b8"
const val DEEP_LINK_URL: String = "https://playbowdogs.page.link"
const val PACKAGE_NAME: String = "com.playbowdogs.neighbors.android"
const val CLIENT_PACKAGE_NAME: String = "com.playbowdogs.neighbors.client"
const val BOTTOM_NAV_INTENT: String = "action.neighbor.dashboard.open"
const val FIREBASE_UI_NAV_INTENT: String = "action.neighbor.firebaseui.open"
const val USER_TYPE_PREF: String = "USER_TYPE"
const val USER_TYPE_CUSTOMER: String = "Customer"
const val USER_TYPE_DOG_SITTER: String = "Dog Sitter"
const val USER_UNIQUE_IDENTIFICATION: String = "USER_UNIQUE_IDENTIFICATION"

val EMPTY_RECORDED_CLIPS: ArrayList<AngelCamRecordedClips> =
    ArrayList<AngelCamRecordedClips>(1).apply {
        val item =
            AngelCamRecordedClips("", "No recorded clips found.", "", "", "", "", "", null, "", "", "", "")
        this.add(item)
    }
