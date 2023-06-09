package com.sidratul.storylens_submission1bpaai.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sidratul.storylens_submission1bpaai.api.ApiConfig
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import com.sidratul.storylens_submission1bpaai.register.Resource
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryAddViewModel(private val pref: UserPreferences) : ViewModel() {

    private val _uploadInfo = MutableLiveData<Resource<String>>()
    val uploadInfo: LiveData<Resource<String>> = _uploadInfo

    suspend fun uploadStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
    ) {
        _uploadInfo.postValue(Resource.Loading())
        val client = ApiConfig.getApiClient().addStory(
            token = "Bearer ${pref.getToken().first()}",
            imageMultipart,
            description
        )

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.isSuccessful) {
                    _uploadInfo.postValue(Resource.Success(response.body()?.message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _uploadInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(
                    StoryAddViewModel::class.java.simpleName,
                    "onFailure upload"
                )
                _uploadInfo.postValue(Resource.Error(t.message))
            }
        })
    }
}