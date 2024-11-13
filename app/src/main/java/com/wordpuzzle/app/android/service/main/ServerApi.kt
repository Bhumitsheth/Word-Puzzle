package com.wordpuzzle.app.android.service.main

import com.wordpuzzle.app.android.service.model.response.*

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ServerApi {
    @POST("user/register")
    @Headers("Content-Type: application/json")
    fun registerUserAPI(@Body body: HashMap<String, Any>): Observable<RegisterResponseModel>

    @POST("user/logout")
    @Headers("Content-Type: application/json")
    fun logoutUserAPI(@Body body: HashMap<String, Any>): Observable<LogoutResponseModel>

    @POST("user/login")
    @Headers("Content-Type: application/json")
    fun loginUserAPI(@Body body: HashMap<String, Any>): Observable<RegisterResponseModel>

    @Multipart
    @POST("user/profile")
    fun editProfileUserImageAPI(@PartMap body: HashMap<String, RequestBody>,
                                @Part filePart: MultipartBody.Part): Observable<EditProfileResponseModel>

    @POST("user/profile")
    @Headers("Content-Type: application/json")
    fun editProfileUserAPI(@Body body: HashMap<String, Any>): Observable<EditProfileResponseModel>

    @GET("user/profile")
    @Headers("Content-Type: application/json")
    fun getProfileUserAPI(): Observable<GetProfileResponseModel>

    @POST("audio/preferences")
    @Headers("Content-Type: application/json")
    fun audioMusicOnOffAPI(@Body body: HashMap<String, Any>): Observable<AudioMusicResponseModel>

    @POST("user/account/forgotpasswd")
    @Headers("Content-Type: application/json")
    fun forgotPasswordAPI(@Body body: HashMap<String, Any>): Observable<ForgotPasswordResponseModel>

    @GET("books/all")
    @Headers("Content-Type: application/json")
    fun getListAllBookAPI(): Observable<ListAllBookResponseModel>

    @POST("generate/wordlist/")
    @Headers("Content-Type: application/json")
    fun generateWordListAPI(@Body body: HashMap<String, Any>): Observable<GenerateWordListResponseModel>

    @GET("leaderboard/")
    @Headers("Content-Type: application/json")
    fun getLeaderBoardAPI(): Observable<LeaderBoardResponseModel>

    @POST("game_complition/")
    @Headers("Content-Type: application/json")
    fun gameCompletionAPI(@Body body: HashMap<String, Any>): Observable<GameCompletionResponseModel>
}