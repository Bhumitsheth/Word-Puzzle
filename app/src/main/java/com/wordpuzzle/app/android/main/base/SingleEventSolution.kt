package com.wordpuzzle.app.android.main.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wordpuzzle.app.android.R
import java.io.File
import kotlin.reflect.KClass

/*
class LiveEventGeneric<M> : MutableLiveData<Event<M>>() {
    fun produce() {
        value = Event(M)
    }
}
*/

class LiveEvent : MutableLiveData<Event<Boolean>>() {
    fun produce(isSuccess:Boolean=true) {
        value = Event(isSuccess)
    }
}
class FileLiveEvent : MutableLiveData<Event<File>>() {
    fun produce(file: File) {
        value = Event(file)
    }
}

class ErrorLiveEvent : MutableLiveData<Event<AppError>>() {
    fun produce(appError: AppError) {
        value = Event(appError)
    }
}

class StringLiveEvent : MutableLiveData<Event<String>>() {
    fun produce(text: String) {
        value = Event(text)
    }
}

class PairLiveEvent : MutableLiveData<Event<Pair<Int, String>>>() {
    fun produce(code: Int, text: String) {
        value = Event(Pair(code, text))
    }
}

public data class StartActivityItem(
    val clazz: KClass<*>,
    val bundle: Bundle?,
    val code: Int?
)

class StartActivityLiveEvent : MutableLiveData<Event<StartActivityItem>>() {
    fun produce(clazz: KClass<*>, bundle: Bundle? = null) {
        value = Event(StartActivityItem(clazz, bundle, null))
    }
}

class RequestPermissionsItem(var permissions: Array<String>, var requestCode: Int )

class RequestPermissionsLiveEvent : MutableLiveData<Event<RequestPermissionsItem>>() {
    fun produce(permissions: Array<String>, requestCode: Int) {
        value = Event(RequestPermissionsItem(permissions, requestCode))
    }
}

class StartActivityForResultLiveEvent : MutableLiveData<Event<StartActivityItem>>() {
    fun produce(clazz: KClass<*>, bundle: Bundle? = null, code: Int) {
        value = Event(StartActivityItem(clazz, bundle, code))
    }
}

public data class SetResultItem(var result: Int, var intent: Intent)

class SetResultLiveEvent : MutableLiveData<Event<SetResultItem>>() {
    fun produce(result: Int, intent: Intent) {
        value = Event(SetResultItem(result, intent))
    }
}

class FragmentLiveEvent : MutableLiveData<Event<Fragment>>() {
    fun produce(f: Fragment) {
        value = Event(f)
    }
}

class IntLiveEvent : MutableLiveData<Event<Int>>() {
    fun produce(num: Int) {
        value = Event(num)
    }
}

class ContentStateLiveData : MutableLiveData<ContentState>() {
    fun onLoading(message: String? = null) {
        value = ContentState(State.Loading, message = message)
    }

    fun onEmpty(message: String? = null) {
        value = ContentState(State.Empty, message = message)
    }

    fun noResult(message: String? = null) {
        value = ContentState(State.NoResult, message = message)
    }

    fun onError(message: String? = null) {
        value = ContentState(State.Error, message = message)
    }

    fun onConnectionError(context: Context) {
        value = ContentState(
            State.Error, message = "${context.getString(R.string.no_internet_connection_title)}\n${context.getString(
                R.string.no_internet_connection_body)}")
    }

    fun onShowContent(message: String? = null) {
        value = ContentState(State.Content, message = message)
    }
}

class PairIntLiveEvent : MutableLiveData<Event<Pair<Int, Int>>>() {
    fun produce(num1: Int, num2: Int) {
        value = Event(Pair(num1, num2))
    }
}

class LongLiveEvent : MutableLiveData<Event<Long>>() {
    fun produce(num: Long) {
        value = Event(num)
    }
}

data class DialogInfo(
    var titleRe: Int,
    var bodyRe: Int,
    var leftButtonTextRe: Int? = null,
    var rightButtonTextRes: Int? = null
)

class ShowDialogLiveEvent : MutableLiveData<Event<DialogInfo>>() {
    fun produce(titleRes: Int, bodyRes: Int, leftButtonTextRes: Int? = null, rightButtonTextRes: Int? = null) {
        value = Event(DialogInfo(titleRes, bodyRes, leftButtonTextRes, rightButtonTextRes))
    }
}

open class Event<out T>(private val content: T) {
    var wasConsumed = false
        private set

    fun consumeIfCan(): T? {
        return if (wasConsumed) {
            null
        } else {
            wasConsumed = true
            content
        }
    }

    fun consumeWhatever(): T = content
}

fun MutableLiveData<Event<Boolean>>.produce() {
    value = Event(true)
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event.consumeIfCan()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}