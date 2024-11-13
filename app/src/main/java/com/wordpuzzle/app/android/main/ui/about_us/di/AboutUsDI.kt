package com.wordpuzzle.app.android.main.ui.about_us.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.about_us.AboutUsActivity
import com.wordpuzzle.app.android.main.ui.about_us.AboutUsViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AboutUsScope

@Module
class AboutUsModule : ViewModule {
    @AboutUsScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            AboutUsViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@AboutUsScope
@Subcomponent(modules = [AboutUsModule::class])
interface AboutUsComponent : ViewComponent<AboutUsActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<AboutUsComponent, AboutUsModule>
}