package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    //private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    companion object {

        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL1 = "https://github.com/bumptech/glide"
        private const val URL2 =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val URL3 = "https://github.com/square/retrofit"


        private const val CHANNEL_ID = "channelId"
        private const val NOTIFICATION_ID = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /*execute createNotification() as soon as app starts so as to create
         the notification channel that will enable Notification posting*/
        createNotificationChannel()

        Timber.plant(Timber.DebugTree())


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


    //notificationManager already initialized in createNotificationChannel()
    private lateinit var notificationManager: NotificationManager

    //broadcastReceiver
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            //call notify off NotificationManager passing in the id & notification
            notificationManager.apply {

                notify(NOTIFICATION_ID, createNotification())
            }
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


    private fun getUrl(): String {

        return when (radioGroup.checkedRadioButtonId) {

            R.id.radioButton1 -> URL1
            R.id.radioButton2 -> URL2
            R.id.radioButton3 -> URL3
            else              -> URL
        }


    }


    private fun createNotification(): Notification {

        /*channel_ID required for compatibility with API 8, CHANNEL_ID
         ignored by older versions*/

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)

        builder.apply {

            setContentTitle(resources.getString(R.string.notification_title))
            setSmallIcon(R.drawable.ic_assistant_black_24dp)
            setContentText(resources.getString(R.string.notification_description))

            /* This is used by Android 7.1 and lower, Android 8.0 and higher,
            use channel importance*/
            priority = NotificationCompat.PRIORITY_HIGH
        }


        //base intent - pass in the context and the activity to be launched
        val intent = Intent(this, DetailActivity::class.java).apply {


        }

        //PendingIntent
        pendingIntent = PendingIntent.getActivity(this,// -> context in which this PI should start the activity
                                                      NOTIFICATION_ID,
                                                      intent,
                                                      0)

        action = NotificationCompat.Action(R.drawable.download_status, getString(R.string.download_status), pendingIntent)

        //set pendingIntent
        builder.setContentIntent(pendingIntent)

        //add action
        builder.addAction(action)
        //dismiss notification from drawer
        builder.setAutoCancel(true)

        //return notification
        return builder.build()
    }

    //create channel
    private fun createNotificationChannel() {

        //check API Level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = resources.getString(R.string.channel_name)
            val channelDesc = resources.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            //NotificationChannel constructor
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDesc
            }


            //further channel customizations before creating it
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)

            //register channel with system
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
