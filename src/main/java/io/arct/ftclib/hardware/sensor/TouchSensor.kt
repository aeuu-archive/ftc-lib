package io.arct.ftclib.hardware.sensor

import io.arct.ftclib.bindings.types.SdkTouchSensor
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface TouchSensor : Device {
    val value: Double
    val pressed: Boolean

    class Impl<T : SdkTouchSensor>(sdk: T) : TouchSensor, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override val value: Double
            get() = sdk.value

        override val pressed: Boolean
            get() = sdk.isPressed

        companion object {
            val sdk = SdkTouchSensor::class.java
        }
    }
}
