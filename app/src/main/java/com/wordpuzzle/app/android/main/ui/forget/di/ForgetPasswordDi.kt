package com.wordpuzzle.app.android.main.ui.forget.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule

import com.wordpuzzle.app.android.main.ui.forget.ForgetPasswordActivity
import com.wordpuzzle.app.android.main.ui.forget.ForgetPasswordViewModel

import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ForgetPasswordScope

@Module
class ForgetPasswordModule : ViewModule {
    @ForgetPasswordScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            ForgetPasswordViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@ForgetPasswordScope
@Subcomponent(modules = [ForgetPasswordModule::class])
interface ForgetPasswordComponent : ViewComponent<ForgetPasswordActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<ForgetPasswordComponent, ForgetPasswordModule>
}
