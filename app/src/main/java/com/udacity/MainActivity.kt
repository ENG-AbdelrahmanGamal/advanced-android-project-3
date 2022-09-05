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
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private var downloadID: Long = 0

    enum class FileName(val value: String) {
        x1("Glide"),
        x2("Repository"),
        x3("Retrofit");

        companion object {
            fun from(findValue: String): FileName =
                FileName.values().first { it.value == findValue }
        }
    }

    enum class FileStatus {
        Completed,
        Faliear
    }

    private lateinit var fileName: String
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var loadingButton: LoadingButton
    private var complete = false

    private var radioGroup: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        move(binding.imageHeader)
        createChannel(getString(R.string.notification_channel_id), "NOTIFICATION_Name")
        loadingButton = findViewById(R.id.custom_button)
        custom_button.setOnClickListener {
            //  radioGroupSelecte()
            download()

            registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            //  receiver.onReceive(this,intent)
            complete = true
            loadingButton.animation
            notificationManager.sendNotification(FileName.x2.value, applicationContext)
        }
        if (complete) {

        }
//        binding.radio!!.setOnCheckedChangeListener { group, checkedId ->
//            // The flow will come here when
//            // any of the radio buttons in the radioGroup
//            // has been clicked
//            // Check which radio button has been clicked
//            // Get the selected Radio Button
//            val radioButton = group
//                .findViewById<View>(checkedId) as RadioButton
//        }

    }

    private fun radioGroupSelecte() {
        val selectedId = radioGroup!!.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "No answer has been selected", Toast.LENGTH_SHORT).show()
        } else {
            val radioButton = radioGroup!!
                .findViewById<View>(selectedId) as RadioButton

            // Now display the value of selected item
            // by the Toast message
            Toast.makeText(this, radioButton.text, Toast.LENGTH_SHORT).show()
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
                        intent?.putExtra("test", FileStatus.Completed.toString())

                    } else {
                        try {
                        Toast.makeText(this@MainActivity, " Download failure", Toast.LENGTH_LONG)
                            .show()
                        intent?.putExtra("test", FileStatus.Faliear.toString())
                        Log.d("TAG", "onReceive: Download not Completed")
                    }catch (e: Exception) {
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
            while (cursor.isAfterLast) {
                val coulmnIndex: Int = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val state = cursor.getInt(coulmnIndex)
                return state
            }
        }
        return DownloadManager.ERROR_UNKNOWN
    }

    companion object {
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
            val intent = Intent(this, DetailActivity::class.java)
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_1 ->
                    if (checked) {
                        fileName = "Gide Image Loading "
                        Log.d("redio", "onRadioButton first  ")
                        intent.putExtra("Radio", "Gide Image Loading")
                    }
                R.id.radio_button_2 ->
                    if (checked) {
                        fileName = "Load Current Repository "
                        Log.d("redio", "onRadioButton   Clicked Load app  ")
                        intent.putExtra("Radio", "Load Current Repository")


                    }
                R.id.radio_button_3 ->
                    if (checked) {
                        fileName = "Retrofit Type save "
                        Log.d("redio", "onRadioButton  Clicked retrofit  ")
                        intent.putExtra("Radio", "Retrofit Type save")
                    }
            }
        } else {
            Log.d("redio", "onRadioButton  UN Clicked:  ")
            intent.putExtra("Radio", "not any thing downloading ")
        }

    }


    fun move(view: ImageView) {
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

