package com.softwaregroup.imageconcept.ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

import com.softwaregroup.imageconcept.util.Utils.rotate
import com.softwaregroup.imageconcept.databinding.ActivityShowImageBinding
import com.softwaregroup.imageconcept.util.Utils
import java.io.File

/**
 * Preview image what we capture from gallery or camera
 *
 */

class ShowPreviewImageActivity : AppCompatActivity() {

    private lateinit var bindingView: ActivityShowImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bindingView = ActivityShowImageBinding.inflate(layoutInflater)
        setContentView(bindingView.root)


        val checks = intent.getStringExtra("value")
        if (checks == "1") {
            val files = intent.getStringExtra("bituri")
            val fileUri = Uri.parse(files)
            bindingView.imgShow.setImageURI(fileUri)
        } else {
            val files = intent.getSerializableExtra("bitmap") as File
            val bitmap = BitmapFactory.decodeFile(files!!.absolutePath)
            val photos = Utils.rotateImageIfRequired(bitmap!!,files!!.toUri())
            bindingView.imgShow.setImageBitmap(photos)
        }


    }


}
