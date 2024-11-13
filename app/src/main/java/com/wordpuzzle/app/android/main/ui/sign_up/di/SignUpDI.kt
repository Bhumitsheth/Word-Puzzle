package com.wordpuzzle.app.android.main.ui.sign_up.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.utils.viewModelFactory
import com.wordpuzzle.app.android.service.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SignUpScope

@Module
class SignUpModule : ViewModule {
    @SignUpScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            SignUpViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@SignUpScope
@Subcomponent(modules = [SignUpModule::class])
interface SignUpComponent : ViewComponent<SignUpActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<SignUpComponent, SignUpModule>
}