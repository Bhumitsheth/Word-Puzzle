package com.wordpuzzle.app.android.di.component

import com.wordpuzzle.app.android.di.module.*
import com.wordpuzzle.app.android.di.network.NetworkModule
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        SharedPrefModule::class,
        NetworkModule::class,
        UserRepositoryModule::class,
        WordRepositoryModule::class,
        DataSourceModule::class,
    ]
)

interface AppComponent {
    fun injectComponentsHolder(componentsHolder: ComponentsHolder)
}
