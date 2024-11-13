package com.wordpuzzle.app.android.main.ui.home.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.main.ui.home.HomeViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class HomeScope

@Module
class HomeModule : ViewModule {
    @HomeScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            HomeViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@HomeScope
@Subcomponent(modules = [HomeModule::class])
interface HomeComponent : ViewComponent<HomeActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<HomeComponent, HomeModule>
}
