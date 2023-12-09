package com.softwaregroup.imageconcept.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwaregroup.imageconcept.repository.AppRepository

/**
 * class represent view-model persist data
 *
 * @property app
 * @property appRepository
 */
class ViewModelProviderFactory(
    val app: Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app, appRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}