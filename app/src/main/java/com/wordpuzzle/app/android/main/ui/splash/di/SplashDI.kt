package com.wordpuzzle.app.android.main.ui.splash.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.splash.SplashActivity
import com.wordpuzzle.app.android.main.ui.splash.SplashViewModel
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashScope

@Module
class SplashModule : ViewModule {
    @SplashScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            SplashViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@SplashScope
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent : ViewComponent<SplashActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<SplashComponent, SplashModule>
}