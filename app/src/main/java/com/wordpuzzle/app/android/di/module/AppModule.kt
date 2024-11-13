package com.wordpuzzle.app.android.di.module

import android.content.Context
import com.wordpuzzle.app.android.main.ui.about_us.AboutUsActivity
import com.wordpuzzle.app.android.main.ui.about_us.di.AboutUsComponent
import com.wordpuzzle.app.android.main.ui.forget.ForgetPasswordActivity
import com.wordpuzzle.app.android.main.ui.forget.di.ForgetPasswordComponent
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.main.ui.home.di.HomeComponent
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardActivity
import com.wordpuzzle.app.android.main.ui.leader_board.di.LeaderBoardComponent
import com.wordpuzzle.app.android.main.ui.level_complete.LevelCompleteActivity
import com.wordpuzzle.app.android.main.ui.level_complete.di.LevelCompleteComponent
import com.wordpuzzle.app.android.main.ui.loading_animation.LoadingAnimationActivity
import com.wordpuzzle.app.android.main.ui.loading_animation.di.LoadingAnimationComponent
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.main.ui.login.di.LoginComponent
import com.wordpuzzle.app.android.main.ui.play_game.PlayGameActivity
import com.wordpuzzle.app.android.main.ui.play_game.di.PlayGameComponent
import com.wordpuzzle.app.android.main.ui.profile.DI.ProfileComponent
import com.wordpuzzle.app.android.main.ui.profile.ProfileActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.di.SelectBookPdfComponent
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity
import com.wordpuzzle.app.android.main.ui.sign_up.di.SignUpComponent
import com.wordpuzzle.app.android.main.ui.splash.SplashActivity
import com.wordpuzzle.app.android.main.ui.splash.di.SplashComponent
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(
    subcomponents = [
        SplashComponent::class,
        SignUpComponent::class,
        SelectBookPdfComponent::class,
        LoadingAnimationComponent::class,
        HomeComponent::class,
        ProfileComponent::class,
        LeaderBoardComponent::class,
        PlayGameComponent::class,
        LoginComponent::class,
        ForgetPasswordComponent::class,
        LevelCompleteComponent::class,
        AboutUsComponent::class,
    ]
)

class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context = context

    @Provides
    @IntoMap
    @ClassKey(SplashActivity::class)
    fun provideSplashBuilder(builder: SplashComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(SignUpActivity::class)
    fun provideSignUpBuilder(builder: SignUpComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(SelectBookPdfActivity::class)
    fun provideSelectBookPdfBuilder(builder: SelectBookPdfComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(LoadingAnimationActivity::class)
    fun provideLoadingAnimationBuilder(builder: LoadingAnimationComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(HomeActivity::class)
    fun provideHomeBuilder(builder: HomeComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(ProfileActivity::class)
    fun provideProfileBuilder(builder: ProfileComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(LeaderBoardActivity::class)
    fun provideLeaderBoardBuilder(builder: LeaderBoardComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(PlayGameActivity::class)
    fun providePlayGameBuilder(builder: PlayGameComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(LoginActivity::class)
    fun provideLoginBuilder(builder: LoginComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(ForgetPasswordActivity::class)
    fun provideForgetPasswordBuilder(builder: ForgetPasswordComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(LevelCompleteActivity::class)
    fun provideLevelCompleteBuilder(builder: LevelCompleteComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(AboutUsActivity::class)
    fun provideAboutUsBuilder(builder: AboutUsComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }
}