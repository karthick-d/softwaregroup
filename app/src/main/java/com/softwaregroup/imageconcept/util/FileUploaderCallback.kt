package com.softwaregroup.imageconcept.util

/**
 * file upload progress check
 *
 */
interface FileUploaderCallback {
    fun onError()
    fun onFinish(responses: Array<String?>?)
    fun onProgressUpdate(currentpercent: Int, totalpercent: Int, filenumber: Int)
}