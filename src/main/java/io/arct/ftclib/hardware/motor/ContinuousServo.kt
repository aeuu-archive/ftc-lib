package io.arct.ftclib.hardware.motor

import io.arct.ftclib.bindings.types.SdkContinuousServo
import io.arct.ftclib.hardware.controller.ServoController

/**
 * A hardware continuous rotation servo
 */
interface ContinuousServo : BasicMotor {
    val controller: ServoController
    val port: Int

    /**
     * Move the continuous servo a particular distance
     *
     * @param distance The distance to move the continuous servo
     *
     * @return @this
     */
    fun move(distance: Double): ContinuousServo

    open class Impl<T : SdkContinuousServo>(sdk: T) : ContinuousServo, BasicMotor.Impl<T>(sdk) {
        override val controller: ServoController = ServoController.Impl(sdk.controller)

        override val port: Int
            get() = sdk.portNumber

        /**
         * Move the s a particular distance
         *
         * @param distance The distance to move the continuous servo
         *
         * @return @this
         */
        override fun move(distance: Double): ContinuousServo {
            sdk.controller.setServoPosition(port, sdk.controller.getServoPosition(port) + distance)

            return this
        }

        companion object {
            val sdk = SdkContinuousServo::class.java
        }
    }
}
