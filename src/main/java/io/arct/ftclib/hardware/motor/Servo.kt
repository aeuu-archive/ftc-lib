package io.arct.ftclib.hardware.motor

import io.arct.ftclib.bindings.types.SdkServo
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice
import io.arct.ftclib.hardware.controller.ServoController

/**
 * A hardware servo
 */
interface Servo : Device {
    val controller: ServoController
    var position: Double

    fun move(target: Double, time: Long, stepsPerSecond: Int = 500): Servo

    open class Impl<T : SdkServo>(sdk: T) : Servo, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override val controller: ServoController
            get() = ServoController.Impl(sdk.controller)

        override var position: Double
            get() = sdk.position
            set(v) {
                sdk.position = v
            }

        override fun move(target: Double, time: Long, stepsPerSecond: Int): Servo {
            val steps = (stepsPerSecond * target / 1000).toInt()
            val stepDistance = (position - target) / steps
            val delay = 1000L / stepsPerSecond

            val originalPosition = position

            for (i in 0..steps) {
                position = originalPosition + i * stepDistance
                Thread.sleep(delay)
            }

            return this
        }

        companion object {
            val sdk = SdkServo::class.java
        }
    }
}
