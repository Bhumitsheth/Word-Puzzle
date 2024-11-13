package com.wordpuzzle.app.android.di.module

import javax.inject.Scope

interface ViewModule

interface ViewComponent<in V> {
    fun inject(view: V)
}

interface ViewComponentBuilder<out C : ViewComponent<*>, in M : ViewModule> {
    fun build(): C
    fun module(module: M): ViewComponentBuilder<C, M>
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope