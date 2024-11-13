package com.wordpuzzle.app.android.main.ui.loading_animation.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import com.wordpuzzle.app.android.main.ui.loading_animation.LoadingAnimationActivity
import com.wordpuzzle.app.android.main.ui.loading_animation.LoadingAnimationViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.presentation.AndroidUseCaseExecutor
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.service.repository.WordRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadingAnimationScope

@Module
class LoadingAnimationModule : ViewModule {
    @LoadingAnimationScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: WordRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            LoadingAnimationViewModel(
                appPrefs,
                repository
            )
        }
    }

    @LoadingAnimationScope
    @Provides
    fun provideUseCaseExecutor(): UseCaseExecutor {
        return AndroidUseCaseExecutor()
    }
}

@LoadingAnimationScope
@Subcomponent(modules = [LoadingAnimationModule::class])
interface LoadingAnimationComponent : ViewComponent<LoadingAnimationActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<LoadingAnimationComponent, LoadingAnimationModule>
}
