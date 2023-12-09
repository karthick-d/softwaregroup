package com.softwaregroup.imageconcept.util

/**
 * after api call event checks functions
 *
 * @param T
 * @property content
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() = if(hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent() = content
}
