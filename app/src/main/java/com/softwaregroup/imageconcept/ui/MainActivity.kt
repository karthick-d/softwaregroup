package com.softwaregroup.imageconcept.ui


import android.app.Activity
import android.app.ProgressDialog

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.softwaregroup.imageconcept.R
import com.softwaregroup.imageconcept.app.MyApplication
import com.softwaregroup.imageconcept.databinding.ActivityMainBinding
import com.softwaregroup.imageconcept.repository.AppRepository
import com.softwaregroup.imageconcept.util.Constants
import com.softwaregroup.imageconcept.util.Constants.REQUEST_CODE_CAMERA
import com.softwaregroup.imageconcept.util.Constants.REQUEST_CODE_GALLERY
import com.softwaregroup.imageconcept.util.FileUploader
import com.softwaregroup.imageconcept.util.FileUploaderCallback
import com.softwaregroup.imageconcept.util.OptionDialogFragment
import com.softwaregroup.imageconcept.util.Utils
import com.softwaregroup.imageconcept.util.Utils.allPermissionsGranted
import com.softwaregroup.imageconcept.util.Utils.getFileExtension
import com.softwaregroup.imageconcept.util.Utils.hideProgress
import com.softwaregroup.imageconcept.util.Utils.permissionList
import com.softwaregroup.imageconcept.util.Utils.showProgress
import com.softwaregroup.imageconcept.util.Utils.updateProgress
import com.softwaregroup.imageconcept.util.errorSnack
import com.softwaregroup.imageconcept.util.showSnack
import com.softwaregroup.imageconcept.viewmodel.MainViewModel
import com.softwaregroup.imageconcept.viewmodel.ViewModelProviderFactory
import java.io.File


/**
 * class represent main screen data operations
 *
 */
class MainActivity : AppCompatActivity(), OptionDialogFragment.OnItemSelect {

    private lateinit var byteArrayImage: ByteArray
    private var photo: Bitmap? = null
    private var imagefile: File? = null
    private var tempuri: Uri? = null
    private lateinit var userViewModel: MainViewModel
    lateinit var activityMainBinding: ActivityMainBinding
    private var stringBase64ImageProfile: String? = null
    private var pDialog: ProgressDialog? = null
    var files = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        pDialog = ProgressDialog(this)
        val view: View = activityMainBinding.root
        setContentView(view)
        init()

    }

    /**
     * create an instance for repository and view-model class
     *
     */
    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        userViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        activityMainBinding.img1Expand.setOnClickListener {
            if (photo != null || tempuri != null) {
                val intent = Intent(this, ShowPreviewImageActivity::class.java)
                intent.putExtra("bitmap", imagefile)
                intent.putExtra("bituri", tempuri.toString())
                if (photo == null) {
                    intent.putExtra("value", "1")
                } else {
                    intent.putExtra("value", "2")
                }
                startActivity(intent)
            } else {
                activityMainBinding.progress.errorSnack(
                    getString(R.string.userimage_mandatory),
                    Snackbar.LENGTH_LONG
                )
            }
        }
    }

    /**
     * submit button click functionality and api calls
     *
     * @param view
     */
    fun onImageClick(view: View) {
        photo = null
        if (allPermissionsGranted(applicationContext)) {
            OptionDialogFragment(this, false, 2).show(
                supportFragmentManager,
                "OptionDialogFragment"
            )
        } else {
            ActivityCompat.requestPermissions(this, permissionList(), 114)
        }

    }

    /**
     * Gallery button click functionality
     *
     */
    override fun onGalleryButtonClicked() {
        takePictureGallery()
    }

    /**
     * Camera Button click functionality
     *
     */
    override fun onCameraButtonClicked() {
        takePictureCamera()
    }

    override fun onCancelOutSide() {
        Log.e("out", "out")
    }

    /**
     *
     *  Camera Intent to capture images
     */
    private fun takePictureCamera() {
        if (allPermissionsGranted(applicationContext)) {
            val intent = Intent(this, CamerasActivity::class.java)
            startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA)
        } else {
            ActivityCompat.requestPermissions(this, permissionList(), Constants.REQUEST_CODE_CAMERA)
        }
    }

    private fun takePictureGallery() {
        if (allPermissionsGranted(applicationContext)) {
            getSelectPhoto()
        } else {
            ActivityCompat.requestPermissions(this, permissionList(), REQUEST_CODE_GALLERY)
        }
    }

    /**
     * Gallery intent to choose files
     *
     */
    fun getSelectPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY)
    }

    /**
     * result capture set on imageview from camaera/gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        files.clear()
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            try {
                imagefile = data?.getSerializableExtra("captureFile") as File
                val strFileName: String = imagefile!!.name
                activityMainBinding.fileName.text = strFileName
                photo = BitmapFactory.decodeFile(imagefile!!.absolutePath)
                val photos = Utils.rotateImageIfRequired(photo!!,imagefile!!.toUri())

                activityMainBinding.imgProfile.setImageBitmap(photos)
                activityMainBinding.imgProfile.scaleType = ImageView.ScaleType.CENTER_CROP

                byteArrayImage = Utils.getByteArrayImage(photo!!)
                stringBase64ImageProfile = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
                files.add(imagefile!!.absolutePath)
                Log.e("filesize", "" + imagefile!!.absolutePath)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            try {
                var bm: Bitmap? = null
                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(
                            applicationContext.contentResolver,
                            data.data
                        )
                        tempuri = data.data

                        activityMainBinding.fileName.text =
                            tempuri!!.lastPathSegment + "." + getFileExtension(
                                tempuri,
                                applicationContext.contentResolver
                            )

                        activityMainBinding.imgProfile.setImageBitmap(bm)
                        activityMainBinding.imgProfile.scaleType = ImageView.ScaleType.CENTER_CROP

                        files.add(getRealPathFromURI(data.data).toString())
                        Log.e("filesize", "" + getRealPathFromURI(data.data))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * submit button click functionality
     *
     * @param view
     */
    fun onSubmitClick(view: View) {
        if (photo != null || tempuri != null
        ) {
            Log.e("filesize", "" + files!!.toString())
            if (Utils.hasInternetConnection(applicationContext as MyApplication)) {
                uploadFiles()
            }else{
                activityMainBinding.progress.errorSnack(
                    getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_LONG
                )
            }
        }
        else {
            activityMainBinding.progress.errorSnack(
                getString(R.string.userimage_mandatory),
                Snackbar.LENGTH_LONG
            )
        }


    }

    /**
     * upload image functionality
     *
     */
    fun uploadFiles() {
        val filesToUpload = arrayOfNulls<File>(files.size)
        for (i in files.indices) {
            filesToUpload[i] = File(files.get(i))
        }
        showProgress(pDialog, "Uploading media ...")
        val fileUploader = FileUploader()
        fileUploader.uploadFiles(
            "/",
            "file",
            filesToUpload,
            object : FileUploaderCallback {
                override fun onError() {
                    hideProgress(pDialog)
                }

                override fun onFinish(responses: Array<String?>?) {
                    hideProgress(pDialog)
                    for (i in responses!!.indices) {
                        val str = responses[i]
                        Log.e("RESPONSE $i", responses[i]!!)
                    }
                    activityMainBinding.progress.showSnack(
                        getString(R.string.file_upload_successfully),
                        Snackbar.LENGTH_LONG
                    )
                }


                override fun onProgressUpdate(
                    currentpercent: Int,
                    totalpercent: Int,
                    filenumber: Int
                ) {
                    updateProgress(pDialog, totalpercent, "Uploading file $filenumber", "")
                    Log.e("Progress Status", "$currentpercent $totalpercent $filenumber")
                }
            })
    }

    /**
     * Get real path of picked image from gallery
     *
     * @param uri
     * @return
     */
    fun getRealPathFromURI(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


}