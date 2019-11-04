package io.arct.ftclib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.arct.ftclib.hardware.Device

open class BasicMotor internal constructor(private val sdk: DcMotorSimple) : Device(sdk) {
    var direction: Direction
        get() = Direction.fromSdk(sdk.direction)
        set(v) { sdk.direction = v.sdk }

    var power: Double
        get() = sdk.power
        set(v) { sdk.power = v }

    enum class Direction {
        Forward,
        Reverse,
        Unknown;

        val inverse: Direction
            get() = when (this) {
                Forward -> Reverse
                Reverse -> Forward
                else -> this
            }

        internal val sdk: DcMotorSimple.Direction?
            get() = when (this) {
                Forward -> DcMotorSimple.Direction.FORWARD
                Reverse -> DcMotorSimple.Direction.REVERSE
                else -> null
            }

        companion object {
            internal fun fromSdk(value: DcMotorSimple.Direction): Direction = when (value) {
                DcMotorSimple.Direction.FORWARD -> Forward
                DcMotorSimple.Direction.REVERSE -> Reverse
                else -> Unknown
            }
        }
    }

    companion object {
        val sdk = DcMotorSimple::class.java
    }
}
