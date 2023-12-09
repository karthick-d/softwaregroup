package com.softwaregroup.imageconcept.util

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri

import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.softwaregroup.imageconcept.app.MyApplication
import com.softwaregroup.imageconcept.ui.CamerasActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min


object Utils {
    /**
     * Network connection check throughout the application
     */
    fun hasInternetConnection(application: MyApplication): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    /**
     * TODO
     *
     * @param degrees
     * @return
     */
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun getByteArrayImage(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return byteArray
    }

    /**
     * This compress the original file.
     */
    @Throws(Exception::class)
    fun compressCurrentBitmapFile(originalImageFile: File) {
        val bitmap = updateDecodeBounds(originalImageFile)
        val fOut = FileOutputStream(originalImageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut)
        fOut.flush() // Not really required
        fOut.close() // do not forget to close the stream
        bitmap.recycle() //recycle the bitmap
    }


    /**
     * Measure decodeBounds of the bitmap from given File.
     */
    private fun updateDecodeBounds(imageFile: File): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
            val sampleHeight = if (outWidth > outHeight) 900 else 1100
            val sampleWidth = if (outWidth > outHeight) 1100 else 900
            /**
             * You can tweak the sizes 900, 1100.
             * The bigger the number is, the more details you can keep.
             * The lesser, the lesser quality of details.
             */
            inSampleSize = min(outWidth / sampleWidth, outHeight / sampleHeight)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(imageFile.absolutePath, this)
        }
    }

    /**
     * TODO
     *
     * @param uri
     * @param contentResolver
     * @return
     */
    fun getFileExtension(uri: Uri?, contentResolver: ContentResolver): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    /**
     * TODO
     *
     * @param applicationContext
     * @return
     */
    fun allPermissionsGranted(applicationContext: Context): Boolean {
        for (permission in permissionList()!!) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun permissionList(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Constants.REQUIRED_PERMISSIONS_13
        } else {
            Constants.REQUIRED_PERMISSIONS
        }
    }

    /**
     * TODO
     *
     * @param pDialog
     * @param str
     */
    fun showProgress(pDialog: ProgressDialog?, str: String?) {
        try {
            pDialog!!.setCancelable(false)
            pDialog!!.setTitle("Please wait")
            pDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            pDialog!!.setMax(100) // Progress Dialog Max Value
            pDialog!!.setMessage(str)
            if (pDialog!!.isShowing()) pDialog!!.dismiss()
            pDialog!!.show()
        } catch (e: java.lang.Exception) {
            Log.e("",""+e.message)
        }
    }

    /**
     * TODO
     *
     * @param pDialog
     */
    fun hideProgress(pDialog: ProgressDialog?) {
        try {
            if (pDialog!!.isShowing()) pDialog!!.dismiss()
        } catch (e: java.lang.Exception) {
            Log.e("",""+e.message)
        }
    }

    /**
     * TODO
     *
     * @param pDialog
     * @param val
     * @param title
     * @param msg
     */
    fun updateProgress(pDialog: ProgressDialog?, `val`: Int, title: String?, msg: String?) {
        pDialog!!.setTitle(title)
        pDialog!!.setMessage(msg)
        pDialog!!.progress = `val`
    }

}