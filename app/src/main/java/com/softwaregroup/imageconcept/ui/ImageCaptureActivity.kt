package com.softwaregroup.imageconcept.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import com.softwaregroup.imageconcept.util.Utils.rotate
import com.softwaregroup.imageconcept.databinding.ActivityImageCaptureBinding
import java.io.File

/**
 * After capture view once before set to imageview
 *
 */
class ImageCaptureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageCaptureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val files = intent.getSerializableExtra("captureFile") as File

        val bitmap = BitmapFactory.decodeFile(files!!.absolutePath)
        binding.imageViewDisplay.setImageBitmap(bitmap?.rotate(90F))


        binding.okButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("captureFile",files)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

        binding.cancelButton.setOnClickListener {
            files.delete()
            setResult(Activity.RESULT_CANCELED)
            finish()

        }
    }

}