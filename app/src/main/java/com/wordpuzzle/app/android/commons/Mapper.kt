package com.wordpuzzle.app.android.commons

/**
 * Created by abdularis on 08/07/17.
 *
 * Base class for type mapper
 */
abstract class Mapper<From, To> {
    abstract fun map(obj: From): To
    abstract fun revMap(obj: To): From
    fun map(objs: List<From>?): List<To>? {
        if (objs == null) return null
        val result: MutableList<To> = ArrayList()
        for (obj in objs) result.add(map(obj))
        return result
    }

    fun revMap(objs: List<To>?): List<From>? {
        if (objs == null) return null
        val result: MutableList<From> = ArrayList()
        for (obj in objs) result.add(revMap(obj))
        return result
    }
}
