package com.softwaregroup.imageconcept.util

/**
 * Globally access data object class for whole application
 */
object Constants{

        const val BASE_URL = "https://file.io"
        private const val CAMERA_PERMISSION = "android.permission.CAMERA"
        private const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
        private const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
        private const val READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES"
        const val REQUEST_CODE_GALLERY = 102
        const val REQUEST_CODE_CAMERA = 400
        val REQUIRED_PERMISSIONS_13 =
                arrayOf<String>(CAMERA_PERMISSION, READ_MEDIA_IMAGES)

        val REQUIRED_PERMISSIONS = arrayOf<String>(
                CAMERA_PERMISSION,
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE

        )
}