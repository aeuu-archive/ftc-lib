package io.arct.ftclib.hardware.sensor

import com.qualcomm.robotcore.hardware.I2cAddr
import io.arct.ftclib.bindings.types.SdkColorSensor
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface ColorSensor : Device {
    val alpha: Int
    val argb: Int
    val red: Int
    val green: Int
    val blue: Int

    var led: Boolean
    var i2cAddr: I2cAddr

    class Impl<T : SdkColorSensor>(sdk: T) : ColorSensor, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override val alpha: Int
            get() = sdk.alpha()

        override val argb: Int
            get() = sdk.argb()

        override val red: Int
            get() = sdk.red()

        override val green: Int
            get() = sdk.green()

        override val blue: Int
            get() = sdk.blue()

        override var led: Boolean = false
            set(v) {
                sdk.enableLed(v); field = v; }

        override var i2cAddr: I2cAddr
            get() = TODO("Not implemented")
            set(v) {
                throw TODO("Not implemented")
            }

        companion object {
            val sdk = SdkColorSensor::class.java
        }
    }
}
