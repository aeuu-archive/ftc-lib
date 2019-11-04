package io.arct.ftclib.hardware.sensor

import io.arct.ftclib.hardware.Device

class GyroSensor internal constructor(private val sdk: com.qualcomm.robotcore.hardware.GyroSensor) : Device(sdk) {
    val calibrating: Boolean
        get() = sdk.isCalibrating

    val heading: Int
        get() = sdk.heading

    val rotationFraction: Double
        get() = sdk.rotationFraction

    val x: Int
        get() = sdk.rawX()

    val y: Int
        get() = sdk.rawY()

    val z: Int
        get() = sdk.rawZ()

    fun calibrate() =
        sdk.calibrate()

    fun resetZ() =
        sdk.resetZAxisIntegrator()

    companion object {
        val sdk = com.qualcomm.robotcore.hardware.GyroSensor::class.java
    }
}
