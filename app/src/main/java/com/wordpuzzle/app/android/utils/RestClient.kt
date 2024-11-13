package com.wordpuzzle.app.android.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


object RestClient {
    /**
     * multiple image upload with multipart
     */
    private const val MULTIPART_FORM_DATA = "multipart/form-data"

    fun createRequestBody(s: String): RequestBody {
        return s.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }

    fun createRequestBody(file: File): RequestBody {
        return file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }

    fun createRequestBodyWithNull(s: String?): RequestBody {
        return s!!.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }

    fun parameterManipulation(map: Map<String, Any>) {
        var map = map
        if (map == null) {
            map = HashMap()
        }
    }
}