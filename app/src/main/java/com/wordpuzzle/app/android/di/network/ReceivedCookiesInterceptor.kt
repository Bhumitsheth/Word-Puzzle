package com.wordpuzzle.app.android.di.network

import android.content.Context
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.preferences.AppPrefs
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor(val context: Context) : Interceptor {
    val preferences: AppPrefs = AppPrefs(context)
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
//            preferenceProvider.putStringSet("PREF_COOKIES", cookies)
            AppConstants.cookies = cookies
        }
        return originalResponse
    }
}