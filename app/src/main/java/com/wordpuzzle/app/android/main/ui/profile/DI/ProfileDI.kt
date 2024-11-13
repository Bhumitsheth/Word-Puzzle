package com.wordpuzzle.app.android.main.ui.profile.DI

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.profile.ProfileActivity
import com.wordpuzzle.app.android.main.ui.profile.ProfileViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ProfileScope

@Module
class ProfileModule : ViewModule {
    @ProfileScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            ProfileViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@ProfileScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent : ViewComponent<ProfileActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<ProfileComponent, ProfileModule>
}
