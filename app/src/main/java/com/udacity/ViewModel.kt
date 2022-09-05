package com.udacity

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.udacity.util.sendNotification

class ViewModel(private val app:Application):AndroidViewModel(app) {
    private val  REQUEST_CODE=0

fun sendNotification(){
    val notificationManager = ContextCompat.getSystemService(
        app,
        NotificationManager::class.java
    ) as NotificationManager
    notificationManager.sendNotification(app.getString(R.string.notification_title), app)

}

}