package com.wordpuzzle.app.android.di.module

import android.content.Context
import com.wordpuzzle.app.android.service.main.ServerApi
import com.wordpuzzle.app.android.service.repository.UserRepository
import dagger.Module
import dagger.Provides

@Module
class UserRepositoryModule {
    @AppScope
    @Provides
    internal fun provideRepository(
        context: Context,
        serverApi: ServerApi
    ): UserRepository {
        return UserRepository(context, serverApi)
    }
}