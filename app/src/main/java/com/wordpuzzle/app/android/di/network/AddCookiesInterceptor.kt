package com.wordpuzzle.app.android.di.network

import android.content.Context
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.preferences.AppPrefs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor(val context: Context) : Interceptor {
    val preferences: AppPrefs = AppPrefs(context)
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
//        val prefCookies: HashSet<String> = preferenceProvider.getStringSet("PREF_COOKIES", HashSet())
        val prefCookies: HashSet<String> = AppConstants.cookies
        for (cookie in prefCookies) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }
}