package com.wordpuzzle.app.android.data.sqlite

import android.content.ContentValues
import com.wordpuzzle.app.android.domain.data.entity.GameRoundEntity
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.GameRoundCallback
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.InfosCallback
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.StatCallback
import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.domain.model.GameRoundStat
import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.domain.model.UsedWord.AnswerLine
import java.lang.Boolean
import javax.inject.Inject
import kotlin.Int
import kotlin.String
import kotlin.arrayOf

/**
 * Created by abdularis on 18/07/17.
 */
class GameRoundSQLiteDataSource @Inject constructor(private val mHelper: DbHelper) :
    GameRoundDataSource {
    override fun getGameRound(gid: Int, callback: GameRoundCallback?) {
        val db = mHelper.readableDatabase
        val cols = arrayOf<String>(
            DbContract.GameRound._ID,
            DbContract.GameRound.COL_NAME,
            DbContract.GameRound.COL_DURATION,
            DbContract.GameRound.COL_GRID_ROW_COUNT,
            DbContract.GameRound.COL_GRID_COL_COUNT,
            DbContract.GameRound.COL_GRID_DATA
        )
        val sel: String = DbContract.GameRound._ID + "=?"
        val selArgs = arrayOf(gid.toString())
        val c = db.query(DbContract.GameRound.TABLE_NAME, cols, sel, selArgs, null, null, null)
        var ent: GameRoundEntity? = null
        if (c.moveToFirst()) {
            val info = GameRound.Info()
            info.id = c.getInt(0)
            info.name = c.getString(1)
            info.duration = c.getInt(2)
            ent = GameRoundEntity()
            ent.info = info
            ent.gridRowCount = c.getInt(3)
            ent.gridColCount = c.getInt(4)
            ent.gridData = c.getString(5)
            ent.usedWords = getUsedWords(gid)
        }
        c.close()
        callback!!.onLoaded(ent)
    }

    override fun getGameRoundInfos(callback: InfosCallback?) {
        val db = mHelper.readableDatabase
        val cols = arrayOf<String>(
            DbContract.GameRound._ID,
            DbContract.GameRound.COL_NAME,
            DbContract.GameRound.COL_DURATION
        )
        val order: String = DbContract.GameRound._ID + " DESC"
        val c = db.query(DbContract.GameRound.TABLE_NAME, cols, null, null, null, null, order)
        val infoList: MutableList<GameRound.Info> = ArrayList()
        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                val info = GameRound.Info()
                info.id = c.getInt(0)
                info.name = c.getString(1)
                info.duration = c.getInt(2)
                infoList.add(info)
                c.moveToNext()
            }
        }
        c.close()
        callback!!.onLoaded(infoList)
    }

    override fun getGameRoundStat(gid: Int, callback: StatCallback?) {
        val subQ = "(SELECT COUNT(*) FROM " + DbContract.UsedWord.TABLE_NAME + " WHERE " +
                DbContract.UsedWord.COL_GAME_ROUND_ID + "=" + gid + ")"
        val q = "SELECT " +
                DbContract.GameRound.COL_NAME + "," +
                DbContract.GameRound.COL_DURATION + "," +
                DbContract.GameRound.COL_GRID_ROW_COUNT + "," +
                DbContract.GameRound.COL_GRID_COL_COUNT + "," +
                subQ +
                " FROM " + DbContract.GameRound.TABLE_NAME + " WHERE " + DbContract.GameRound._ID +
                "=" + gid
        val db = mHelper.readableDatabase
        val c = db.rawQuery(q, null)
        if (c.moveToFirst()) {
            val stat = GameRoundStat()
            stat.name = c.getString(0)
            stat.duration = c.getInt(1)
            stat.gridRowCount = c.getInt(2)
            stat.gridColCount = c.getInt(3)
            stat.usedWordCount = c.getInt(4)
            callback!!.onLoaded(stat)
        }
        c.close()
    }

    override fun saveGameRound(gameRound: GameRoundEntity?) {
        val db = mHelper.writableDatabase
        val values = ContentValues()
        values.put(DbContract.GameRound.COL_NAME, gameRound!!.info!!.name)
        values.put(DbContract.GameRound.COL_DURATION, gameRound.info!!.duration)
        values.put(DbContract.GameRound.COL_GRID_ROW_COUNT, gameRound.gridRowCount)
        values.put(DbContract.GameRound.COL_GRID_COL_COUNT, gameRound.gridColCount)
        values.put(DbContract.GameRound.COL_GRID_DATA, gameRound.gridData)
        val gid = db.insert(DbContract.GameRound.TABLE_NAME, "null", values)
        gameRound!!.info!!.id = gid.toInt()
        for (usedWord in gameRound.usedWords!!) {
            values.clear()
            values.put(DbContract.UsedWord.COL_GAME_ROUND_ID, gid)
            values.put(DbContract.UsedWord.COL_WORD_STRING, usedWord.string)
            values.put(
                DbContract.UsedWord.COL_IS_MYSTERY,
                if (usedWord.isMystery) "true" else "false"
            )
            values.put(DbContract.UsedWord.COL_REVEAL_COUNT, usedWord.revealCount)
            if (usedWord.answerLine != null) {
                values.put(DbContract.UsedWord.COL_ANSWER_LINE_DATA, usedWord.answerLine.toString())
                values.put(DbContract.UsedWord.COL_LINE_COLOR, usedWord.answerLine!!.color)
            }
            val insertedId = db.insert(DbContract.UsedWord.TABLE_NAME, "null", values)
            usedWord.id = insertedId.toInt()
        }
    }

    override fun deleteGameRound(gid: Int) {
        val db = mHelper.writableDatabase
        var sel: String = DbContract.GameRound._ID + "=?"
        val selArgs = arrayOf(gid.toString())
        db.delete(DbContract.GameRound.TABLE_NAME, sel, selArgs)
        sel = DbContract.UsedWord.COL_GAME_ROUND_ID + "=?"
        db.delete(DbContract.UsedWord.TABLE_NAME, sel, selArgs)
    }

    override fun deleteGameRounds() {
        val db = mHelper.writableDatabase
        db.delete(DbContract.GameRound.TABLE_NAME, null, null)
        db.delete(DbContract.UsedWord.TABLE_NAME, null, null)
    }

    override fun saveGameRoundDuration(gid: Int, newDuration: Int) {
        val db = mHelper.readableDatabase
        val values = ContentValues()
        values.put(DbContract.GameRound.COL_DURATION, newDuration)
        val where: String = DbContract.GameRound._ID + "=?"
        val whereArgs = arrayOf(gid.toString())
        db.update(DbContract.GameRound.TABLE_NAME, values, where, whereArgs)
    }

    override fun markWordAsAnswered(usedWord: UsedWord?) {
        val db = mHelper.writableDatabase
        val values = ContentValues()
        values.put(DbContract.UsedWord.COL_ANSWER_LINE_DATA, usedWord!!.answerLine.toString())
        values.put(DbContract.UsedWord.COL_LINE_COLOR, usedWord.answerLine!!.color)
        val where: String = DbContract.UsedWord._ID + "=?"
        val whereArgs = arrayOf(usedWord.id.toString())
        db.update(DbContract.UsedWord.TABLE_NAME, values, where, whereArgs)
    }

    private fun getUsedWords(gid: Int): List<UsedWord> {
        val db = mHelper.readableDatabase
        val cols = arrayOf<String>(
            DbContract.UsedWord._ID,
            DbContract.UsedWord.COL_WORD_STRING,
            DbContract.UsedWord.COL_ANSWER_LINE_DATA,
            DbContract.UsedWord.COL_LINE_COLOR,
            DbContract.UsedWord.COL_IS_MYSTERY,
            DbContract.UsedWord.COL_REVEAL_COUNT
        )
        val sel = DbContract.UsedWord.COL_GAME_ROUND_ID + "=?"
        val selArgs = arrayOf(gid.toString())
        val c = db.query(DbContract.UsedWord.TABLE_NAME, cols, sel, selArgs, null, null, null)
        val usedWordList: MutableList<UsedWord> = ArrayList()
        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                val id = c.getInt(0)
                val str = c.getString(1)
                val lineData = c.getString(2)
                val col = c.getInt(3)
                var answerLine: AnswerLine? = null
                if (lineData != null) {
                    answerLine = AnswerLine()
                    answerLine.fromString(lineData)
                    answerLine.color = col
                }
                val usedWord = UsedWord()
                usedWord.id = id
                usedWord.string = str
                usedWord.isAnswered = lineData != null
                usedWord.answerLine = answerLine
                usedWord.isMystery = Boolean.valueOf(c.getString(4))
                usedWord.revealCount = c.getInt(5)
                usedWordList.add(usedWord)
                c.moveToNext()
            }
        }
        c.close()
        return usedWordList
    }
}
