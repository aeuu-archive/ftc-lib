package io.arct.ftclib.robot

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import io.arct.ftclib.exceptions.CouldNotFindDeviceException
import io.arct.ftclib.hardware.Device
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

/**
 * A map of hardware devices
 *
 * @see Robot
 */
class HardwareMap internal constructor(val `_`: HardwareMap) {

    /**
     * Get a hardware device using the standard SDK.
     *
     * @param type Class of device type
     * @param identifier Identifier of the device
     *
     * @return The device
     */
    fun <T : HardwareDevice> get(type: Class<out T>, identifier: String): T =
        `_`.get(type, identifier)

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
        type.kotlin.constructors.first().call(`_`.get(
            (type.kotlin.companionObject!!.memberProperties.first { it.name == "sdk" } as KProperty1<Any, HardwareDevice>).get(type.kotlin.companionObjectInstance as Any) as Class<HardwareDevice>,
            identifier
        ))
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
    inline operator fun <reified T : Device> invoke(identifier: String): T = try {
        T::class.constructors.first().call(`_`.get(
            (T::class.companionObject!!.memberProperties.first { it.name == "sdk" } as KProperty1<Any, HardwareDevice>).get(T::class.companionObjectInstance as Any) as Class<HardwareDevice>,
            identifier
        ))
    } catch (e: Exception) {
        throw CouldNotFindDeviceException(T::class.simpleName, identifier)
    }

    /**
     * Alias of invoke([String])
     *
     * @see invoke
     */
    inline operator fun <reified T : Device> get(identifier: String): T = invoke(identifier)
}
