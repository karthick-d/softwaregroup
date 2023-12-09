package com.softwaregroup.imageconcept.network

object RequestBodies {
    /**
     * input for create use post api call
     *
     * @property name
     * @property gender
     * @property email
     * @property status
     */
    data class ImageBody(
        val name:String,
        val gender:String,
        val email:String,
        val status:String
    )

}