package com.wordpuzzle.app.android.di.module

import android.content.Context
import com.wordpuzzle.app.android.data.sqlite.DbHelper
import com.wordpuzzle.app.android.data.sqlite.GameRoundSQLiteDataSource
import com.wordpuzzle.app.android.data.xml.WordXmlDataSource
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.WordDataSource
import com.wordpuzzle.app.android.service.main.ServerApi
import dagger.Module
import dagger.Provides

/**
 * Created by abdularis on 18/07/17.
 */
@Module
class DataSourceModule {
    @AppScope
    @Provides
    fun provideDbHelper(context: Context?): DbHelper {
        return DbHelper(context)
    }

    @AppScope
    @Provides
    fun provideGameRoundDataSource(dbHelper: DbHelper?): GameRoundDataSource {
        return GameRoundSQLiteDataSource(dbHelper!!)
    }

    //    @Provides
    //    @Singleton
    //    WordDataSource provideWordDataSource(DbHelper dbHelper) {
    //        return new WordSQLiteDataSource(dbHelper);
    //    }
    @AppScope
    @Provides
    fun provideWordDataSource(context: Context?, serverApi: ServerApi): WordDataSource {
        return WordXmlDataSource(context!!, serverApi)
    }
}
