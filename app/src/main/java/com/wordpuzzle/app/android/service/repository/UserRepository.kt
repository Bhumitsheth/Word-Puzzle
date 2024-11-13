package com.wordpuzzle.app.android.service.repository

import android.content.Context
import com.wordpuzzle.app.android.service.main.ServerApi
import com.wordpuzzle.app.android.service.model.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class UserRepository(val context: Context, private val serverApi: ServerApi) {
    fun registerUserAPI(params: HashMap<String, Any>): Observable<RegisterResponseModel> {
        return serverApi.registerUserAPI(params)
    }

    fun logoutUserAPI(params: HashMap<String, Any>): Observable<LogoutResponseModel> {
        return serverApi.logoutUserAPI(params)
    }

    fun loginUserAPI(params: HashMap<String, Any>): Observable<RegisterResponseModel> {
        return serverApi.loginUserAPI(params)
    }

    fun editProfileUserImageAPI(params: HashMap<String, RequestBody>, @Part profileImage: MultipartBody.Part): Observable<EditProfileResponseModel> {
        return serverApi.editProfileUserImageAPI(params, profileImage)
    }

    fun editProfileUserAPI(params: HashMap<String, Any>): Observable<EditProfileResponseModel> {
        return serverApi.editProfileUserAPI(params)
    }

    fun getProfileUserAPI(): Observable<GetProfileResponseModel> {
        return serverApi.getProfileUserAPI()
    }

    fun audioMusicOnOffAPI(params: HashMap<String, Any>): Observable<AudioMusicResponseModel> {
        return serverApi.audioMusicOnOffAPI(params)
    }

    fun forgotPasswordAPI(params: HashMap<String, Any>): Observable<ForgotPasswordResponseModel> {
        return serverApi.forgotPasswordAPI(params)
    }

    fun getListAllBookAPI(): Observable<ListAllBookResponseModel> {
        return serverApi.getListAllBookAPI()
    }

    fun getLeaderBoardAPI(): Observable<LeaderBoardResponseModel> {
        return serverApi.getLeaderBoardAPI()
    }

    fun generateWordListAPI(params: HashMap<String, Any>): Observable<GenerateWordListResponseModel> {
        return serverApi.generateWordListAPI(params)
    }
}