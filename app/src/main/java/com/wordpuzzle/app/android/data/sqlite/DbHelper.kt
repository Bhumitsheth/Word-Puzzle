package com.wordpuzzle.app.android.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by abdularis on 18/07/17.
 */
class DbHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USED_WORD)
        db.execSQL(SQL_CREATE_TABLE_GAME_ROUND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val DB_NAME = "data.db"
        private const val DB_VERSION = 1
        private val SQL_CREATE_TABLE_USED_WORD =
            "CREATE TABLE " + DbContract.UsedWord.TABLE_NAME + " (" +
                    DbContract.UsedWord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DbContract.UsedWord.COL_GAME_ROUND_ID + " INTEGER," +
                    DbContract.UsedWord.COL_WORD_STRING + " TEXT," +
                    DbContract.UsedWord.COL_ANSWER_LINE_DATA + " TEXT," +
                    DbContract.UsedWord.COL_LINE_COLOR + " INTEGER," +
                    DbContract.UsedWord.COL_IS_MYSTERY + " TEXT," +
                    DbContract.UsedWord.COL_REVEAL_COUNT + " INTEGER)"
        private val SQL_CREATE_TABLE_GAME_ROUND =
            "CREATE TABLE " + DbContract.GameRound.TABLE_NAME + " (" +
                    DbContract.GameRound._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DbContract.GameRound.COL_NAME + " TEXT," +
                    DbContract.GameRound.COL_DURATION + " INTEGER," +
                    DbContract.GameRound.COL_GRID_ROW_COUNT + " INTEGER," +
                    DbContract.GameRound.COL_GRID_COL_COUNT + " INTEGER," +
                    DbContract.GameRound.COL_GRID_DATA + " TEXT)"
    }
}
