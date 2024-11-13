package com.wordpuzzle.app.android.main.ui.play_game

import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.commons.DurationFormatter.fromInteger
import com.wordpuzzle.app.android.commons.Util.getRandomColorWithAlpha
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityPlayGameBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.level_complete.LevelCompleteActivity
import com.wordpuzzle.app.android.main.ui.play_game.adapter.ArrayLetterGridDataAdapter
import com.wordpuzzle.app.android.main.ui.play_game.di.PlayGameComponent
import com.wordpuzzle.app.android.preferences.SoundManager
import com.wordpuzzle.app.android.presentation.custom.LetterBoard.OnLetterSelectionListener
import com.wordpuzzle.app.android.presentation.custom.StreakView
import com.wordpuzzle.app.android.presentation.custom.StreakView.StreakLine
import com.wordpuzzle.app.android.presentation.model.UsedWordViewModel
import com.wordpuzzle.app.android.presentation.presenters.GamePlayPresenter
import com.wordpuzzle.app.android.presentation.views.GamePlayView
import com.wordpuzzle.app.android.utils.calculateScore
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PlayGameActivity : BaseMainActivity<ActivityPlayGameBinding>(), GamePlayView {
    var mGrayColor = 0
    private var mGameId = 0
    private var mLetterAdapter: ArrayLetterGridDataAdapter? = null
    @JvmField
    @Inject
    var mPresenter: GamePlayPresenter? = null
    @JvmField
    @Inject
    var mSoundManager: SoundManager? = null

    override fun getLayoutId() = R.layout.activity_play_game

    override fun create(savedInstanceState: Bundle?) {
        val handler = PlayGameHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this, intent)
        playGame()
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: PlayGameViewModel by lazy {
        ViewModelProvider(this, factory).get(PlayGameViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<PlayGameComponent>()?.inject(this)
    }

    fun playGame() {
        mGrayColor = resources.getColor(R.color.gray)
        binding.letterBoard.streakView!!.setEnableOverrideStreakLineColor(false)
        binding.letterBoard.streakView!!.setOverrideStreakLineColor(mGrayColor)
        binding.letterBoard.setOnLetterSelectionListener(object : OnLetterSelectionListener {
            override fun onSelectionBegin(streakLine: StreakLine?, str: String?) {
                streakLine!!.color = getRandomColorWithAlpha(170)
                binding.constraintSelectWordLayout.visibility = View.VISIBLE
//                mTextSelection.setText(str)
                viewModel.selectWord.value = str
            }

            override fun onSelectionDrag(streakLine: StreakLine?, str: String?) {
                if (str!!.isNotBlank()) {
                    viewModel.selectWord.value = str
                } else {
                    viewModel.selectWord.value = ""
                }
            }

            override fun onSelectionEnd(streakLine: StreakLine?, str: String?) {
                mPresenter!!.answerWord(str!!, streakLine!!, false)
                binding.constraintSelectWordLayout.visibility = View.GONE
//                mTextSelection.setText(str)
                viewModel.selectWord.value = str
            }
        })
        mPresenter!!.setView(this)
        if (intent.extras != null) {
            mGameId = intent.extras!!.getInt(AppConstants.gameRoundId)
            mPresenter!!.loadGameRound(mGameId)
        }

        binding.letterBoard.gridLineBackground!!.visibility = View.INVISIBLE
        binding.letterBoard.streakView!!.isSnapToGrid = StreakView.SnapType.valueOf("START_END")
//        mFinishedText.setVisibility(View.GONE)
//        mSoundManager!!.play(SoundManager.SOUND_CONTINUE)
    }

    override fun onStart() {
        super.onStart()
        mPresenter!!.resumeGame()
    }

    override fun onStop() {
        super.onStop()
        mPresenter!!.stopGame()
    }

    private fun tryScale() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val boardWidth = binding.letterBoard.width
        val screenWidth = metrics.widthPixels
        if (boardWidth > screenWidth) {
            val scale = screenWidth.toFloat() / boardWidth.toFloat()
            binding.letterBoard.scale(scale, scale)
        }
    }

    override fun doneLoadingContent() {
        // call tryScale() on the next render frame
        Handler().post { tryScale() }
    }

    override fun showLoading(enable: Boolean) {
        if (enable) {
//            mLoading!!.visibility = View.VISIBLE
            showProgressBlocking()
//            mContentLayout!!.visibility = View.GONE
        } else {
//            mLoading!!.visibility = View.GONE
            hideProgressBlocking()
//            mContentLayout!!.visibility = View.VISIBLE
        }
    }

    override fun showLetterGrid(grid: Array<CharArray?>?) {
        if (mLetterAdapter == null) {
            mLetterAdapter = ArrayLetterGridDataAdapter(grid)
            binding.letterBoard.dataAdapter = mLetterAdapter
        } else {
            mLetterAdapter!!.grid = grid
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun showDuration(duration: Int) {
        binding.tvTime.text = fromInteger(duration)
    }

    override fun showDurationMillisecond(duration: String) {
        viewModel.durationMillisecond.value = duration
    }

    override fun showUsedWords(usedWords: List<UsedWordViewModel?>?) {
        for (uw in usedWords!!) {
            binding.flowLayout.addView(createUsedWordTextView(uw))
        }
    }

    override fun showAnsweredWordsCount(count: Int) {
//        mAnsweredTextCount!!.text = count.toString()
    }

    override fun showWordsCount(count: Int) {
//        mWordsCount!!.text = count.toString()
    }

    override fun showFinishGame() {
        mSoundManager!!.play(SoundManager.SOUND_WIN)
        Handler(Looper.getMainLooper()).postDelayed({
            var score = calculateScore(storeSingletonData.getStoreWordListKey(), binding.tvTime.text.toString())
            val intent = Intent(this, LevelCompleteActivity::class.java)
            intent.putExtra(AppConstants.pageNo, viewModel.selectPageNo.value)
            intent.putExtra(AppConstants.score, score)
            intent.putExtra(AppConstants.playTime, binding.tvTime.text.toString())
            intent.putExtra(AppConstants.bookId, viewModel.bookId.value)
            intent.putExtra(AppConstants.bookName, viewModel.bookName.value)
            intent.putExtra(AppConstants.selectPageNo, viewModel.selectPageNo.value)
            intent.putExtra(AppConstants.durationMillisecond, viewModel.durationMillisecond.value)
            startActivity(intent)
            finish()
        }, 1000)
    }

    override fun setGameAsAlreadyFinished() {
        binding.letterBoard.streakView!!.isInteractive = false
//        mFinishedText!!.visibility = View.VISIBLE
    }

    override fun wordAnswered(correct: Boolean, usedWordId: Int) {
        if (correct) {
            val textView = findUsedWordTextViewByUsedWordId(usedWordId)
            if (textView != null) {
                val uw = textView.tag as UsedWordViewModel
//                if (mPref!!.grayscale()) {
//                    uw.usedWord.answerLine!!.color = mGrayColor
//                }
                //                textView.setBackgroundColor(uw.getStreakLine().getColor());
                textView.text = uw.string
                //                textView.setTextColor(Color.WHITE);
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                TextViewCompat.setTextAppearance(textView, R.style.Textview20TextSelectDisableColorActorRegular)
                val anim = AnimatorInflater.loadAnimator(this, R.animator.zoom_in_out)
                anim.setTarget(textView)
                anim.start()
            }
            mSoundManager!!.play(SoundManager.SOUND_CORRECT)
        } else {
            binding.letterBoard.popStreakLine()
            mSoundManager!!.play(SoundManager.SOUND_WRONG)
        }
    }

    private fun createUsedWordTextView(uw: UsedWordViewModel?): TextView {
        val tv = TextView(this)
        tv.setPadding(10, 5, 10, 5)
        if (uw!!.isAnswered) {
//            if (mPref!!.grayscale()) {
//                uw.usedWord.answerLine!!.color = mGrayColor
//            }
            tv.setBackgroundColor(uw.streakLine.color)
            tv.text = uw.string
//            tv.setTextColor(Color.WHITE)
            tv.setTextColor(ContextCompat.getColor(this, R.color.textSelectColor))
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.letterBoard.addStreakLine(uw.streakLine)
            TextViewCompat.setTextAppearance(tv, R.style.Textview20TextSelectDisableColorActorRegular)
        } else {
            val str = uw.string
            //            if (uw.isMystery()) {
//                int revealCount = uw.getUsedWord().getRevealCount();
//                String uwString = uw.getString();
//                str = "";
//                for (int i = 0; i < uwString.length(); i++) {
//                    if (revealCount > 0) {
//                        str += uwString.charAt(i);
//                        revealCount--;
//                    }
//                    else {
//                        str += " ?";
//                    }
//                }
//            }
            tv.text = str
            TextViewCompat.setTextAppearance(tv, R.style.Textview20TextSelectEnableColorActorRegular)
        }
        tv.tag = uw
        return tv
    }

    private fun findUsedWordTextViewByUsedWordId(usedWordId: Int): TextView? {
        for (i in 0 until binding.flowLayout.childCount) {
            val tv = binding.flowLayout.getChildAt(i) as TextView
            val uw = tv.tag as UsedWordViewModel
            if (uw != null && uw.id == usedWordId) {
                return tv
            }
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed
        viewModel.stopMediaPlayer()
    }
}