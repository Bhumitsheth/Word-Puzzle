package com.wordpuzzle.app.android.main.ui.select_book_pdf.di

import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.di.module.ViewComponentBuilder
import com.wordpuzzle.app.android.di.module.ViewModule
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SelectBookPdfScope

@Module
class SelectBookPdfModule : ViewModule {
    @SelectBookPdfScope
    @Provides
    internal fun provideFactory(
        appPrefs: AppPrefs,
        repository: UserRepository
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            SelectBookPdfViewModel(
                appPrefs,
                repository
            )
        }
    }
}

@SelectBookPdfScope
@Subcomponent(modules = [SelectBookPdfModule::class])
interface SelectBookPdfComponent : ViewComponent<SelectBookPdfActivity> {
    @Subcomponent.Builder
    interface Builder : ViewComponentBuilder<SelectBookPdfComponent, SelectBookPdfModule>
}
