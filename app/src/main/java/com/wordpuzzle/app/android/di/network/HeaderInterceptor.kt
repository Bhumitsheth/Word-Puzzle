package com.wordpuzzle.app.android.di.network

import com.wordpuzzle.app.android.preferences.AppPrefs
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor(private val preferences: AppPrefs) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        var request = chain.request()
        val builder = request.headers.newBuilder()
        if (preferences.getRegisterResponseData() != null) {
            builder.add("Authorization", "token ${preferences.getRegisterResponseData()!!.token}")
        }
        val headers = builder.build()
        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }

}