package com.wordpuzzle.app.android.preferences

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.util.SparseIntArray
import com.wordpuzzle.app.android.R
import javax.inject.Inject

/**
 * Created by abdularis on 22/07/17.
 */
class SoundManager @Inject constructor(context: Context, private val mPreferences: AppPrefs) {
    private var mSoundPool: SoundPool? = null
    private var mSoundPoolMap: SparseIntArray? = null

    init {
        init(context)
    }

    fun play(sound: Int) {
        if (mPreferences.getBoolean(AppPrefs.isSoundOnOff)) {
            mSoundPool!!.play(mSoundPoolMap!![sound], 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    private fun init(context: Context) {
        mSoundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        mSoundPoolMap = SparseIntArray()
        mSoundPoolMap!!.put(SOUND_CORRECT, mSoundPool!!.load(context, R.raw.correct, 1))
        mSoundPoolMap!!.put(SOUND_WRONG, mSoundPool!!.load(context, R.raw.wrong, 1))
        mSoundPoolMap!!.put(SOUND_WIN, mSoundPool!!.load(context, R.raw.win, 1))
    }

    companion object {
        const val SOUND_CORRECT = 0
        const val SOUND_WRONG = 1
        const val SOUND_WIN = 2
    }
}
