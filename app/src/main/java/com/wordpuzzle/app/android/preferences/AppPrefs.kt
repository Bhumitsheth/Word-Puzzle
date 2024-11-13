package com.wordpuzzle.app.android.preferences

import android.content.Context
import com.google.gson.*
import com.wordpuzzle.app.android.service.model.response.RegisterDataModel

class AppPrefs(private val context: Context) : BasePreferences() {

    companion object {
        const val PROFILE_GSON = "profile_gson"
//        const val TOKEN = "token"
        const val FULL_NAME = "full_name"
        const val EMAIL = "email"
        const val KEY_PREV_SAVE_GAME_DATA_COUNT = "prevSaveGameDataCount"
        var isSoundOnOff = "isSoundOnOff"
        var isMusicOnOff = "isMusicOnOff"
        var IS_SELECT_BOOK_PDF = "isSelectBookPdf"
    }

    fun setRegisterResponseData(response: RegisterDataModel?) {
        val profileJson = Gson().toJson(response)
        putString(context, PROFILE_GSON, profileJson)
    }

    fun getRegisterResponseData(): RegisterDataModel? {
        return GsonBuilder().create().fromJson(getString(PROFILE_GSON), RegisterDataModel::class.java)
    }

    fun setString(key: String?, value: String?) {
        putString(context, key, value)
    }

    fun getString(key: String?): String = getString(context, key)

    fun setLong(key: String?, value: Long) {
        putLong(context, key, value)
    }

    fun getLong(key: String?): Long = getLong(context, key)

    fun setBoolean(key: String?, value: Boolean) {
        putBoolean(context, key, value)
    }

    fun getBoolean(key: String?): Boolean = getBoolean(context, key)

    fun setFloat(key: String?, value: Float) {
        putFloat(context, key, value)
    }

    fun getFloat(key: String?): Float = getFloat(context, key)

    fun setInt(key: String?, value: Int) {
        putInt(context, key, value)
    }

    fun getInt(key: String?): Int = getInt(context, key)

    fun removeRecord(key: String?) {
        removeRecord(context, key)
    }

    fun clearAll() {
        clearAll(context)
    }

    fun incrementSavedGameDataCount() {
        putInt(context, KEY_PREV_SAVE_GAME_DATA_COUNT, previouslySavedGameDataCount + 1)
    }

    val previouslySavedGameDataCount: Int get() = getInt(context, KEY_PREV_SAVE_GAME_DATA_COUNT)
}
