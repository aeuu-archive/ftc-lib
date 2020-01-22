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
    }

    open class Impl<T : SdkBasicMotor>(sdk: T) : BasicMotor, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override var direction: Direction
            get() = fromSdk(sdk.direction)
            set(v) {
                sdk.direction = toSdk(v)
            }

        override var power: Double
            get() = sdk.power
            set(v) { sdk.power = v }

        companion object {
            val sdk = SdkBasicMotor::class.java

            private fun fromSdk(value: DcMotorSimple.Direction): Direction = when (value) {
                DcMotorSimple.Direction.FORWARD -> Direction.Forward
                DcMotorSimple.Direction.REVERSE -> Direction.Reverse
                else -> Direction.Unknown
            }

            private fun toSdk(value: Direction): DcMotorSimple.Direction? = when (value) {
                Direction.Forward -> DcMotorSimple.Direction.FORWARD
                Direction.Reverse -> DcMotorSimple.Direction.REVERSE
                else -> null
            }
        }
    }
}
