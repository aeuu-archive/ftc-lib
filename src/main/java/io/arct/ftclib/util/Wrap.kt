package io.arct.ftclib.util

import kotlin.reflect.KProperty

class Wrap<T>(private val get: () -> T, private val set: ((T) -> Unit)? = null) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        get.invoke()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        set!!.invoke(value)
}
