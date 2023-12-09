package com.softwaregroup.imageconcept.network


import com.google.gson.JsonElement
import com.softwaregroup.imageconcept.model.lmageResponse.ImageOutputItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

/**
 * Interface class for retrofit to call post api
 *
 */
interface API {
    @Multipart
    @POST
    fun uploadFile(
        @Url url: String?,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") authorization: String?
    ): Call<ImageOutputItem?>?

    @Multipart
    @POST
    fun uploadFile(@Url url: String?, @Part file: MultipartBody.Part): Call<ImageOutputItem?>?
}