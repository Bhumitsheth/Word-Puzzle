package com.wordpuzzle.app.android.di.module

import android.content.Context
import com.wordpuzzle.app.android.preferences.AppPrefs
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule {
    @AppScope
    @Provides
    internal fun provide(context: Context): AppPrefs {
        return AppPrefs(context)
    }
}