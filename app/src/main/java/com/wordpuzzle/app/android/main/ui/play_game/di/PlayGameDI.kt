package com.wordpuzzle.app.android.main.ui.play_game.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import com.wordpuzzle.app.android.main.ui.play_game.PlayGameActivity
import com.wordpuzzle.app.android.main.ui.play_game.PlayGameViewModel

import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.presentation.AndroidUseCaseExecutor
import com.wordpuzzle.app.android.service.repository.WordRepository
import com.wordpuzzle.app.android.utils.viewModelFactory

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PlayGameScope

@Module
class PlayGameModule : ViewModule {
    @PlayGameScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: WordRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            PlayGameViewModel(
                appPrefs,
                repository
            )
        }
    }

    @PlayGameScope
    @Provides
    fun provideUseCaseExecutor(): UseCaseExecutor {
        return AndroidUseCaseExecutor()
    }
}

@PlayGameScope
@Subcomponent(modules = [PlayGameModule::class])
interface PlayGameComponent : ViewComponent<PlayGameActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<PlayGameComponent, PlayGameModule>
}
