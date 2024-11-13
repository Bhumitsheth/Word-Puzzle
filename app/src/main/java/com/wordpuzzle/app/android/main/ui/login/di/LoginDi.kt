package com.wordpuzzle.app.android.main.ui.login.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.main.ui.login.LoginViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginScope

@Module
class LoginModule : ViewModule {
    @LoginScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            LoginViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@LoginScope
@Subcomponent(modules = [LoginModule::class])
interface LoginComponent : ViewComponent<LoginActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<LoginComponent, LoginModule>
}
