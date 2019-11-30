package io.arct.ftclib.hardware.controller

import io.arct.ftclib.bindings.types.SdkServoController
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface ServoController : Device {
    var pwm: PwmStatus

    fun position(port: Int): Double
    fun position(port: Int, new: Double)

    enum class PwmStatus {
        Enabled,
        Disabled,
        Mixed,
        Unknown;

        companion object {
            fun fromSdk(value: com.qualcomm.robotcore.hardware.ServoController.PwmStatus): PwmStatus = when (value) {
                com.qualcomm.robotcore.hardware.ServoController.PwmStatus.ENABLED -> Enabled
                com.qualcomm.robotcore.hardware.ServoController.PwmStatus.DISABLED -> Disabled
                com.qualcomm.robotcore.hardware.ServoController.PwmStatus.MIXED -> Mixed
                else -> Unknown
            }

            fun toSdk(value: PwmStatus): com.qualcomm.robotcore.hardware.ServoController.PwmStatus? = when (value) {
                Enabled -> com.qualcomm.robotcore.hardware.ServoController.PwmStatus.ENABLED
                Disabled -> com.qualcomm.robotcore.hardware.ServoController.PwmStatus.DISABLED
                Mixed -> com.qualcomm.robotcore.hardware.ServoController.PwmStatus.MIXED
                else -> null
            }
        }
    }

    class Impl<T : SdkServoController>(sdk: T) : ServoController, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override var pwm: PwmStatus
            get() = PwmStatus.fromSdk(sdk.pwmStatus)
            set(v) { if (v == PwmStatus.Enabled) sdk.pwmEnable() else if (v == PwmStatus.Disabled) sdk.pwmDisable() }

        override fun position(port: Int) =
            sdk.getServoPosition(port)

        override fun position(port: Int, new: Double) =
            sdk.setServoPosition(port, new)

        companion object {
            val sdk = SdkServoController::class.java
        }
    }
}
