package io.arct.ftclib.robot

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.squareup.kotlinpoet.asTypeName
import io.arct.ftclib.exceptions.CouldNotFindDeviceException
import io.arct.ftclib.hardware.Device
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * A map of hardware devices
 *
 * @see Robot
 */
class HardwareMap internal constructor(val `_`: OpMode) {
    /**
     * Get a hardware device using the standard SDK.
     *
     * @param type Class of device type
     * @param identifier Identifier of the device
     *
     * @return The device
     */
    @Deprecated("Use Library Functions Instead", ReplaceWith("this(identifier)"))
    fun <T : HardwareDevice> get(type: Class<out T>, identifier: String): T =
        `_`.hardwareMap.get(type, identifier)

    /**
     * Get a hardware [Device] by passing a [Class].
     * Using invoke([String]) is strongly preferred over this method.
     * Use only when invoking from Java.
     *
     * @see invoke
     *
     * @return The device
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Device> get(type: Class<T>, identifier: String): T = try {
        val impl: KClass<*> = type.kotlin.nestedClasses.find {
            it.simpleName == "Impl" && it.supertypes.any {
                it.asTypeName() == type.kotlin.asTypeName()
            }
        }!!

        impl.primaryConstructor!!.call(`_`.hardwareMap.get(
            (impl.companionObject!!.memberProperties.first { it.name == "sdk" } as KProperty1<Any, HardwareDevice>).get(impl.companionObjectInstance!!) as Class<HardwareDevice>,
            identifier
        )) as T
    } catch (e: Exception) {
        throw CouldNotFindDeviceException(type.kotlin.simpleName, identifier)
    }

    /**
     * Get a hardware [Device].
     *
     * @param T The type of device (T extends [Device])
     * @param identifier Identifier of the device
     *
     * @return The device
     */
    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified T : Device> invoke(identifier: String, vararg arguments: Any): T = try {
        val impl: KClass<*> = T::class.nestedClasses.find {
            it.simpleName == "Impl" && it.supertypes.any {
                it.asTypeName() == T::class.asTypeName()
            }
        }!!

        impl.primaryConstructor!!.call(`_`.hardwareMap.get(
            (impl.companionObject!!.memberProperties.first { it.name == "sdk" } as KProperty1<Any, HardwareDevice>).get(impl.companionObjectInstance!!) as Class<HardwareDevice>,
            identifier
        ), *arguments) as T
    } catch (e: Exception) {
        throw CouldNotFindDeviceException(T::class.simpleName, identifier)
    }

    /**
     * Alias of invoke([String])
     *
     * @see invoke
     */
    inline operator fun <reified T : Device> get(identifier: String): T =
        invoke(identifier)

    inline operator fun <reified T : Device> getValue(thisRef: Any?, property: KProperty<*>): T =
        invoke(property.name)
}
