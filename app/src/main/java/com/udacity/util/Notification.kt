    package com.udacity.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID=0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val contentIntent=Intent(applicationContext,DetailActivity::class.java)

    contentIntent.putExtra("filename", MainActivity.fileName)
    contentIntent.putExtra("status",MainActivity.fileStatus)


    val contentPendingIntent=PendingIntent.getActivity(applicationContext,
        NOTIFICATION_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT)
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())

}
