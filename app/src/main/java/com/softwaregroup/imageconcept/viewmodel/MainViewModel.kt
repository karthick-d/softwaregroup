package com.softwaregroup.imageconcept.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.softwaregroup.imageconcept.R
import com.softwaregroup.imageconcept.app.MyApplication
import com.softwaregroup.imageconcept.model.lmageResponse.ImageOutputItem
import com.softwaregroup.imageconcept.network.RequestBodies
import com.softwaregroup.imageconcept.repository.AppRepository
import com.softwaregroup.imageconcept.util.Event
import com.softwaregroup.imageconcept.util.Resource
import com.softwaregroup.imageconcept.util.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * class represent for access data and business logic for image screen
 *
 * @property appRepository
 * @constructor
 * TODO
 *
 * @param app
 */
class MainViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _loginResponse = MutableLiveData<Event<Resource<ImageOutputItem>>>()
    val imageResponse:LiveData<Event<Resource<ImageOutputItem>>> = _loginResponse

    /**
     * input parse data access for user screen
     *
     * @param body
     */
    fun loginUser(body: RequestBodies.ImageBody) = viewModelScope.launch {
        login(body)
    }

    /**
     * method represent intermediate data pass to repository and handle output response
     *
     * @param body
     */
    private suspend fun login(body: RequestBodies.ImageBody) {
        _loginResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
//                val response = appRepository.UserImage(body)
//                _loginResponse.postValue(handleUserResponse(response))
            } else {
                _loginResponse.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection))))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _loginResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error
                            )
                        ))
                    )
                }
            }
        }
    }

    /**
     * api call output response success or failure methods
     *
     * @param response
     * @return
     */
    private fun handleImageResponse(response: Response<ImageOutputItem>): Event<Resource<ImageOutputItem>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}