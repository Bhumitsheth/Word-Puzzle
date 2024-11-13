package com.wordpuzzle.app.android.main.ui.level_complete.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.level_complete.LevelCompleteActivity
import com.wordpuzzle.app.android.main.ui.level_complete.LevelCompleteViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.WordRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LevelCompleteScope

@Module
class LevelCompleteModule : ViewModule {
    @LevelCompleteScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: WordRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            LevelCompleteViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@LevelCompleteScope
@Subcomponent(modules = [LevelCompleteModule::class])
interface LevelCompleteComponent : ViewComponent<LevelCompleteActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<LevelCompleteComponent, LevelCompleteModule>
}