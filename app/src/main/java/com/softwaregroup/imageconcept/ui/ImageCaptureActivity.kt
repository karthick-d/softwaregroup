package com.softwaregroup.imageconcept.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.net.toUri



import com.softwaregroup.imageconcept.databinding.ActivityImageCaptureBinding
import com.softwaregroup.imageconcept.util.Utils

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
        val photo = Utils.rotateImageIfRequired(bitmap,files.toUri())

        binding.imageViewDisplay.setImageBitmap(photo)


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