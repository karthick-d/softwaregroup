package com.softwaregroup.imageconcept.network

object RequestBodies {
    /**
     * TODO
     *
     * @property filename
     * @property url
     * @property path
     * @property type
     */
    data class ImageBody(
        val filename:String,
        val url:String,
        val path:String,
        val type:String
    )

}