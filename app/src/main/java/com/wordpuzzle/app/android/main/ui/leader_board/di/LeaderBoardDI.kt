package com.wordpuzzle.app.android.main.ui.leader_board.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardActivity
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LeaderBoardScope

@Module
class LeaderBoardModule : ViewModule {
    @LeaderBoardScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            LeaderBoardViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@LeaderBoardScope
@Subcomponent(modules = [LeaderBoardModule::class])
interface LeaderBoardComponent : ViewComponent<LeaderBoardActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<LeaderBoardComponent, LeaderBoardModule>
}