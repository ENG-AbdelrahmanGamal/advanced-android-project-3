package com.udacity.receviver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.util.sendNotification

class MyReceviver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val message =intent?.getStringExtra("MESSAGE")

        val notificationManager = ContextCompat.getSystemService(context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.notification_title).toString(),
            context
        )

    }
}