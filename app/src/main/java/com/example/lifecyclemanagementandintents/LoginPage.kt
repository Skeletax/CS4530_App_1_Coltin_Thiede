package com.example.lifecyclemanagementandintents

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DateFormat
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*

class LoginPage : AppCompatActivity(), View.OnClickListener {

    private var mLoginButton: Button? = null

    private var fName: String? = null
    private var mName: String? = null
    private var lName: String? = null
    private var fNameET: EditText? = null
    private var mNameET: EditText? = null
    private var lNameET: EditText? = null
    private var photoButton: Button? = null
    private var camPic: ImageView? = null
    private var photoFilePath: String? = null
    private var orientation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_port)

        mLoginButton = findViewById(R.id.loginButton)
        mLoginButton!!.setOnClickListener(this)

        photoButton = findViewById(R.id.photoButton)
        photoButton!!.setOnClickListener(this)

        fNameET = findViewById(R.id.firstNameInput)
        mNameET = findViewById(R.id.midNameInput)
        lNameET = findViewById(R.id.lastNameInput)

        fNameET?.setText(fName)
        mNameET?.setText(mName)
        lNameET?.setText(lName)

        orientation = "portrait"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        pack()
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.login_port)
            initialize()
            orientation = "portrait"
        }else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.login_land)
            initialize()
            orientation = "landscape"
        }
    }

    private fun initialize(){
        mLoginButton = findViewById(R.id.loginButton)
        mLoginButton!!.setOnClickListener(this)

        photoButton = findViewById(R.id.photoButton)
        photoButton!!.setOnClickListener(this)

        fNameET = findViewById(R.id.firstNameInput)
        mNameET = findViewById(R.id.midNameInput)
        lNameET = findViewById(R.id.lastNameInput)

        fNameET?.setText(fName)
        mNameET?.setText(mName)
        lNameET?.setText(lName)

        camPic = findViewById(R.id.photoImage)

        if (photoFilePath != "UNKNOWN PATH" && photoFilePath != null){
            camPic!!.setImageBitmap(BitmapFactory.decodeFile(photoFilePath))
        }
    }

    private fun pack(){
        fName = fNameET?.text.toString()
        mName = mNameET?.text.toString()
        lName = lNameET?.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View) {
        when (view.id) {
            R.id.loginButton -> {
                fNameET = findViewById(R.id.firstNameInput)
                mNameET = findViewById(R.id.midNameInput)
                lNameET = findViewById(R.id.lastNameInput)

                fName = fNameET?.text.toString()
                mName = mNameET?.text.toString()
                lName = lNameET?.text.toString()

                val confirmIntent = Intent(this, ConfirmPage::class.java)
                confirmIntent.putExtra("F_NAME", fName)
                confirmIntent.putExtra("L_NAME", lName)
                if (photoFilePath != null)
                    confirmIntent.putExtra("FILE_PATH", photoFilePath)
                else
                    confirmIntent.putExtra("FILE_PATH", "UNKNOWN PATH")
                confirmIntent.putExtra("ORIENTATION", orientation)
                this.startActivity(confirmIntent)
            }

            R.id.photoButton -> {
                val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    camActivity.launch(camIntent)
                }catch (ex: ActivityNotFoundException){

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val camActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                camPic = findViewById<View>(R.id.photoImage) as ImageView

                val state = Environment.getExternalStorageState()
                if (Environment.MEDIA_MOUNTED == state){
                    val thumbnailImage: Bitmap?

                    if (Build.VERSION.SDK_INT >= 33) {
                        thumbnailImage =
                            result.data!!.getParcelableExtra("data", Bitmap::class.java)
                        camPic!!.setImageBitmap(thumbnailImage)
                    } else {
                        thumbnailImage = result.data!!.getParcelableExtra("data")
                        camPic!!.setImageBitmap(thumbnailImage)
                    }

                    photoFilePath = saveImage(thumbnailImage)
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val timeStamp = DateFormat.getDateTimeInstance().format(Date())
        val file = File(myDir, "Profile_$timeStamp.jpg")
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "file saved!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file.absolutePath
    }
}