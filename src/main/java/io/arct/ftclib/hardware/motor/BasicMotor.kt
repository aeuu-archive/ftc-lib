package io.arct.ftclib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.arct.ftclib.bindings.types.SdkBasicMotor
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface BasicMotor : Device {
    var direction: Direction
    var power: Double

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

    open class Impl<T : SdkBasicMotor>(sdk: T) : BasicMotor, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override var direction: Direction
            get() = Direction.fromSdk(sdk.direction)
            set(v) { sdk.direction = v.sdk }

        override var power: Double
            get() = sdk.power
            set(v) { sdk.power = v }

        companion object {
            val sdk = SdkBasicMotor::class.java
        }
    }
}
