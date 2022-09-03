package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.material.appbar.AppBarLayout
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.PI


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private val CHANAL_ID = "chanal_id"
    private val NOTIFICATION_ID = 1002

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
      val  button:Button = findViewById(R.id.button)

        createChannalNotification()
        custom_button.setOnClickListener {
            download()

        }
        button.setOnClickListener {

            setNotification()


        }
    }

    private fun createChannalNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification description"
            val notificationChannel = NotificationChannel(
                CHANAL_ID, name, NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description=descriptionText }
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = " time to build second project"

        }
    }
    private fun setNotification(){
            val intent=Intent(this,DetailActivity::class.java).apply {
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent:PendingIntent= PendingIntent.getActivity(this,0,intent,0)
        val bitmap=BitmapFactory.decodeResource(applicationContext.resources,R.drawable.ic_baseline_cloud_download_24)
        val bitmapLargeIcon=BitmapFactory.decodeResource(applicationContext.resources,R.drawable.ic_outline_cloud_download_24)

        val builder=NotificationCompat.Builder(this,CHANAL_ID)
            .setSmallIcon(R.drawable.ic_outline_cloud_download_24)
            .setContentTitle("test for test")
            .setLargeIcon(bitmapLargeIcon)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentText("thanks for notification me ")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID,builder.build())
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        Toast.makeText(this, "Radio button selected", Toast.LENGTH_LONG).show()
                        // Pirates are the best
                    }
                R.id.radio_loadapp ->
                    if (checked) {
                        Toast.makeText(this, "Radio button selected", Toast.LENGTH_LONG).show()
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        Toast.makeText(this, "Radio button selected", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    fun download(view: View) {
        //  download()
        onRadioButtonClicked(view)
        //    instanseFromNotif(app = application)

        //TODO step 7 call create channal function
    }

//    private fun instanseFromNotif(app: Application) {
//        val notificationManager = ContextCompat.getSystemService(
//            app,
//            NotificationManager::class.java
//        ) as NotificationManager
//        notificationManager.sendNotification(app.getString(R.string.notification_title), app)
//    }

}
