package com.wordpuzzle.app.android.main.ui.play_game

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.view.Window
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityPlayGameBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.model.common.*
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.service.repository.WordRepository

class PlayGameViewModel(val appPrefs: AppPrefs, val repository: WordRepository) : BaseViewModel() {
    //binding object
    private var binding: ActivityPlayGameBinding? = null
    private var activity: PlayGameActivity? = null

    //Live data Object Object
    val bookId: MutableLiveData<Int> = MutableLiveData(0)
    val bookName: MutableLiveData<String> = MutableLiveData("")
    val selectPageNo: MutableLiveData<String> = MutableLiveData("")
    val selectWord: MutableLiveData<String> = MutableLiveData("")
    val durationMillisecond: MutableLiveData<String> = MutableLiveData("")
    private var isSoundCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var isMusicCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    var mediaPlayer: MediaPlayer? = null

    /**
     * @param binding use view file
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setBinding(binding: ActivityPlayGameBinding, activity: PlayGameActivity, intent: Intent) {
        this.binding = binding
        this.activity = activity

        if (intent.hasExtra(AppConstants.bookId)) {
            bookId.value = intent.getIntExtra(AppConstants.bookId, 0)
        }

        if (intent.hasExtra(AppConstants.bookName)) {
            bookName.value = intent.getStringExtra(AppConstants.bookName)
        }

        if (intent.hasExtra(AppConstants.selectPageNo)) {
            selectPageNo.value = intent.getStringExtra(AppConstants.selectPageNo)
        }
        soundStart()
    }

    fun imageGoToHome() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_quit_play_game)

        val ivClose: ImageView = dialog.findViewById(R.id.ivClose)
        val clYes: ConstraintLayout = dialog.findViewById(R.id.clYes)

        ivClose.setOnClickListener { dialog.dismiss() }
        clYes.setOnClickListener {
            dialog.dismiss()
            stopMediaPlayer()
            val intent = Intent(activity, HomeActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finishAffinity()
        }
        dialog.show()
    }

    private fun soundStart() {
        // Initialize MediaPlayer with the raw audio file
        mediaPlayer = MediaPlayer.create(activity, R.raw.music)

        // Set looping if you want continuous playback
        mediaPlayer?.isLooping = true

        // Start playback
        if (appPrefs.getBoolean(AppPrefs.isMusicOnOff)) {
            mediaPlayer?.start()
        }
    }

    /**
     * SETTINGS CLICK POPUP DIALOG
     */
    fun ivSettingClick() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_settings)

        val closeBtn = dialog.findViewById<ImageView>(R.id.image_close)
        val ivSoundOnOff = dialog.findViewById<ImageView>(R.id.ivSoundOnOff)
        val ivMusicOnOff = dialog.findViewById<ImageView>(R.id.ivMusicOnOff)

        isSoundCheck.value = appPrefs.getBoolean(AppPrefs.isSoundOnOff)
        isMusicCheck.value = appPrefs.getBoolean(AppPrefs.isMusicOnOff)

        ivSoundOnOff.setImageResource(if (appPrefs.getBoolean(AppPrefs.isSoundOnOff)) R.drawable.ic_audio_on else R.drawable.ic_audio_off)
        ivMusicOnOff.setImageResource(if (appPrefs.getBoolean(AppPrefs.isMusicOnOff)) R.drawable.ic_music_on else R.drawable.ic_music_off)

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        ivSoundOnOff.setOnClickListener {
            val newState = !(isSoundCheck.value ?: false)
            isSoundCheck.value = newState
            appPrefs.setBoolean(AppPrefs.isSoundOnOff, newState)
            ivSoundOnOff.setImageResource(if (newState) R.drawable.ic_audio_on else R.drawable.ic_audio_off)
        }

        ivMusicOnOff.setOnClickListener {
            val newState = !(isMusicCheck.value ?: false)
            isMusicCheck.value = newState
            appPrefs.setBoolean(AppPrefs.isMusicOnOff, newState)
            // Start playback
            if (appPrefs.getBoolean(AppPrefs.isMusicOnOff)) {
                soundStart()
            } else {
                stopMediaPlayer()
            }
            ivMusicOnOff.setImageResource(if (newState) R.drawable.ic_music_on else R.drawable.ic_music_off)
        }
        dialog.show()
    }

    // Function to stop and release MediaPlayer
    fun stopMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop() // Stops playback
            }
            release() // Releases resources
        }
        mediaPlayer = null
    }
}