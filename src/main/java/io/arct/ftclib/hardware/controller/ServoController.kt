package io.arct.ftclib.hardware.controller

import com.qualcomm.robotcore.hardware.ServoController
import io.arct.ftclib.hardware.Device

class ServoController internal constructor(private val sdk: ServoController, private val port: Int) : Device(sdk) {
    var pwm: PwmStatus
        get() = PwmStatus.fromSdk(sdk.pwmStatus)
        set(v) { if (v == PwmStatus.Enabled) sdk.pwmEnable() else if (v == PwmStatus.Disabled) sdk.pwmDisable() }

    var position: Double
        get() = sdk.getServoPosition(port)
        set(v) = sdk.setServoPosition(port, v)

    enum class PwmStatus {
        Enabled,
        Disabled,
        Mixed,
        Unknown;

        companion object {
            fun fromSdk(value: ServoController.PwmStatus): PwmStatus = when (value) {
                ServoController.PwmStatus.ENABLED -> Enabled
                ServoController.PwmStatus.DISABLED -> Disabled
                ServoController.PwmStatus.MIXED -> Mixed
                else -> Unknown
            }

            fun toSdk(value: PwmStatus): ServoController.PwmStatus? = when (value) {
                Enabled -> ServoController.PwmStatus.ENABLED
                Disabled -> ServoController.PwmStatus.DISABLED
                Mixed -> ServoController.PwmStatus.MIXED
                else -> null
            }
        }
    }

    companion object {
        val sdk = com.qualcomm.robotcore.hardware.ServoController::class.java
    }
}
