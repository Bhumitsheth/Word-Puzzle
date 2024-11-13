package com.wordpuzzle.app.android.di.component

import android.content.Context
import com.wordpuzzle.app.android.di.module.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class ComponentsHolder(private val context: Context) {

    @Inject
    lateinit var builders: MutableMap<Class<*>, Provider<ViewComponentBuilder<*, *>>>

    private var components: MutableMap<Class<*>, ViewComponent<*>?> = HashMap()

    var appComponent: AppComponent? = null

    fun init() {
        appComponent = DaggerAppComponent.builder().appModule(
            AppModule(
                context
            )
        ).build()

        appComponent?.injectComponentsHolder(this)
    }

    fun getViewComponent(cls: Class<*>): ViewComponent<*> {
        var component: ViewComponent<*>? = components[cls]
        if (component == null) {
            val builder: ViewComponentBuilder<*, *>? = builders[cls]?.get()
            component = builder?.build()
            components[cls] = component
        }
        return component!!
    }

    fun releaseViewComponent(cls: Class<*>) {
        components[cls] = null
        printComponents()
    }

    fun printComponents() {
        for ((cl, component) in components) {
            Timber.d("${cl.simpleName} = ${component}")
        }
    }
}