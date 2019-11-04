package io.arct.ftclib.hardware.motor

import com.qualcomm.robotcore.hardware.CRServo
import io.arct.ftclib.hardware.controller.ServoController

/**
 * A hardware continuous rotation servo
 */
class ContinuousServo internal constructor(private val sdk: CRServo) : BasicMotor(sdk) {
    val controller: ServoController = ServoController(sdk.controller, port)

    val port: Int
        get() = sdk.portNumber

    /**
     * Move the s a particular distance
     *
     * @param distance The distance to move the continuous servo
     *
     * @return @this
     */
    fun move(distance: Double): ContinuousServo {
        sdk.controller.setServoPosition(port, sdk.controller.getServoPosition(port) + distance)

        return this
    }

    companion object {
        val sdk = CRServo::class.java
    }
}
