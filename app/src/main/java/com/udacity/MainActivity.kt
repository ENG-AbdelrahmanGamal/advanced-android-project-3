package com.udacity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var loadingButton: LoadingButton
    private var checkBox: Boolean = false

    private var radioGroup: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        //  move()
        createChannel(getString(R.string.notification_channel_id), CHANNEL_ID)
        loadingButton = findViewById(R.id.custom_button)
        fun showToast() {
            Toast.makeText(
                this,
                "please select the file to Download",
                Toast.LENGTH_LONG
            ).show()

        }
        custom_button.setOnClickListener {
            if (checkBox) {
                download()
                registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                //     complete = true
                loadingButton.animation
            } else {
                val handler = Handler()
                handler.postDelayed({ showToast() }, 2000)
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                if (getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(this@MainActivity, " Download Completed", Toast.LENGTH_LONG)
                        .show()
                    Log.d("TAG", "onReceive: Download Completed")
                    fileStatus = "Completed"
                    notificationManager.sendNotification(fileName, applicationContext)

                } else {
                    try {
                        Toast.makeText(this@MainActivity, " Download failure", Toast.LENGTH_LONG)
                            .show()
                        Log.d("TAG", "onReceive: Download not Completed")
                        fileStatus = "Download failure"
                    } catch (e: Exception) {
                        Log.d("TAG", "onReceive: Download not Completed${e.localizedMessage}")

                    }
                }
            }
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
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun getDownloadStatus(): Int {
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)
        val downloadManager: DownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {

            val coulmnIndex: Int = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val state = cursor.getInt(coulmnIndex)
            return state


        }
        return DownloadManager.ERROR_UNKNOWN
    }

    companion object {
        var fileName: String = ""
        var fileStatus: String = "nothing downloading "
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )
            // TODO: Step 2.6 disable badges for this channel

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for download"
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)


        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            checkBox = true
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_1 ->
                    if (checked) {
                        fileName = "Gide Image Loading "
                        Log.d("redio", "onRadioButton first /// $fileName")

                    }
                R.id.radio_button_2 ->
                    if (checked) {
                        fileName = "Load Current Repository "
                        Log.d("redio", "onRadioButton   Clicked Load app///$fileName  ")
                    }
                R.id.radio_button_3 ->
                    if (checked) {
                        fileName = "Retrofit Type save "
                        Log.d("redio", "onRadioButton  Clicked retrofit/// $fileName ")

                    }
            }
        } else {
            Log.d("redio", "onRadioButton  UN Clicked:$fileName  ")
        }
    }

    fun move(view: LoadingButton) {
        val va = ValueAnimator.ofFloat(4f, 0f, 0f, 0f)
        val startColor = Color.BLUE
        val endColor = Color.GREEN
        var cAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            startColor,
            endColor
        )
        cAnimator.setDuration(1000)
        va.duration = 1000 //in millis
//        va.addUpdateListener { animation -> view.translationX = animation.animatedValue as Float }
        va.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(anamation: ValueAnimator?) {
                val animated = anamation?.animatedValue as Float
                view.translationY = animated
                view.translationX = animated
            }
        })
        va.repeatCount = 3
        va.reverse()
        cAnimator.reverse()
        cAnimator.start()
        va.start()
    }
}

