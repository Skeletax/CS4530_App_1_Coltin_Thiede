package com.example.lifecyclemanagementandintents

import android.content.res.Configuration
import android.graphics.Bitmap.Config
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.BitmapFactory
import android.widget.*

class ConfirmPage : AppCompatActivity(){

    private var fName: String? = null
    private var lName: String? = null
    private var photoFilePath: String? = null
    private var photoImage: ImageView? = null
    private var orientation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orientation = intent.getStringExtra("ORIENTATION")
        if (orientation == "landscape")
            setContentView(R.layout.confirm_login_land)
        else
            setContentView(R.layout.confirm_login_port)

        fName = intent.getStringExtra("F_NAME")
        lName = intent.getStringExtra("L_NAME")
        photoFilePath = intent.getStringExtra("FILE_PATH")

        photoImage = findViewById(R.id.photoThumbnail)

        if (photoFilePath != "UNKNOWN PATH"){
            photoImage!!.setImageBitmap(BitmapFactory.decodeFile(photoFilePath))
        }

        val textView: TextView = findViewById(R.id.textView)

        textView.text = fName + " " + lName + " is logged in!"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.confirm_login_port)
            initialize()
        }else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.confirm_login_land)
            initialize()
        }
    }

    private fun initialize(){
        photoImage = findViewById(R.id.photoThumbnail)

        if (photoFilePath != "UNKNOWN PATH"){
            photoImage!!.setImageBitmap(BitmapFactory.decodeFile(photoFilePath))
        }

        val textView: TextView = findViewById(R.id.textView)

        textView.text = fName + " " + lName + " is logged in!"
    }
}
