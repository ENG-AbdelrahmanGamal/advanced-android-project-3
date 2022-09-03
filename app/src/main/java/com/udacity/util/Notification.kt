    package com.udacity.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID=0
    private val REQUEST_CODE = 0
    private val FLAGS = 0



    //step one create extension fun
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent=Intent(applicationContext,MainActivity::class.java)
    val contentPendingIntent=PendingIntent.getActivity(applicationContext,
        NOTIFICATION_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT)


// TODO:step two  geet instance of notification compact of builder
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)

    )
        // TODO:step three set icon ,title, and text and so on
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)




    //TODO add style
        val downloadImage=BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_launcher_background
        )
    val bigPictureStyle=NotificationCompat
        .BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)

    // TODO: Step 1.4 call notify to send notification
    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}