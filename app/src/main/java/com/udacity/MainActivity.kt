package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Timber.plant(Timber.DebugTree())

        Timber.i("in the onCreate()")
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {


            if (radioGroup.checkedRadioButtonId == -1) {

                Toast.makeText(this, resources.getString(R.string.radio_message_title), Toast.LENGTH_SHORT)
                        .show()
            }
            else {

                Timber.i("The Url is ${getUrl()}")
                download(getUrl())
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)



            Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun download(url: String) {
        val request =
                DownloadManager.Request(Uri.parse(url))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request) // enqueue puts the download request in the queue.
    }

    companion object {

        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL1 = "https://github.com/bumptech/glide"
        private const val URL2 =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val URL3 = "https://github.com/square/retrofit"


        private const val CHANNEL_ID = "channelId"
    }


    private fun getUrl(): String {

        return when (radioGroup.checkedRadioButtonId) {

            R.id.radioButton1 -> URL1
            R.id.radioButton2 -> URL2
            R.id.radioButton3 -> URL3
            else              -> URL
        }


    }


    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)

        builder.apply {

            setContentTitle(resources.getString(R.string.notification_title))
            setSmallIcon(R.drawable.ic_assistant_black_24dp)
            setContentText(resources.getString(R.string.notification_description))
            priority = NotificationCompat.PRIORITY_HIGH
        }
    }

}
