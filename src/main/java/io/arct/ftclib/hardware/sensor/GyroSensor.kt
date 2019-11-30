package io.arct.ftclib.hardware.sensor

import io.arct.ftclib.bindings.types.SdkGyroSensor
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface GyroSensor : Device {
    val calibrating: Boolean
    val heading: Int
    val rotationFraction: Double

    val x: Int
    val y: Int
    val z: Int

    fun calibrate()
    fun resetZ()

    class Impl<T : SdkGyroSensor>(sdk: T) : GyroSensor, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override val calibrating: Boolean
            get() = sdk.isCalibrating

        override val heading: Int
            get() = sdk.heading

        override val rotationFraction: Double
            get() = sdk.rotationFraction

        override val x: Int
            get() = sdk.rawX()

        override val y: Int
            get() = sdk.rawY()

        override val z: Int
            get() = sdk.rawZ()

        override fun calibrate() =
            sdk.calibrate()

        override fun resetZ() =
            sdk.resetZAxisIntegrator()

        companion object {
            val sdk = SdkGyroSensor::class.java
        }
    }
}
