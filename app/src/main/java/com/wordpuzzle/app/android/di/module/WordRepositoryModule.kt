package com.wordpuzzle.app.android.di.module

import android.content.Context
import com.wordpuzzle.app.android.service.main.ServerApi
import com.wordpuzzle.app.android.service.repository.WordRepository
import dagger.Module
import dagger.Provides

@Module
class WordRepositoryModule {
    @AppScope
    @Provides
    internal fun provideRepository(
        context: Context,
        serverApi: ServerApi
    ): WordRepository {
        return WordRepository(context, serverApi)
    }
}