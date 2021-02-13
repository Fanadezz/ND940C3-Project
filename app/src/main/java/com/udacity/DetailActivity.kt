package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val bundleExtra = intent?.extras

        bundleExtra?.let {

            //get status and the file name
            val fileName = bundleExtra.getString(MainActivity.FILE_NAME_KEY)
            val status = bundleExtra.getString(MainActivity.DOWNLOAD_STATUS_KEY)

            //change text color in case of failure_status

            if (status.equals("Failure")){

                statusTextView.setTextColor(Color.RED)
            }

            statusTextView.text = status
            fileNameTextView.text = fileName

        }





        ok_button.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
        }
    }




}
