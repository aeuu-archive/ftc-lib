package io.arct.ftclib.hardware.motor

import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.controller.ServoController

/**
 * A hardware servo
 */
class Servo internal constructor(private val sdk: com.qualcomm.robotcore.hardware.Servo) : Device(sdk) {
    val controller: ServoController =
        ServoController(sdk.controller, sdk.portNumber)

    /**
     * Move the servo to a position
     *
     * @param position Position to move to
     *
     * @return @this
     */
    fun move(position: Double): Servo {
        sdk.position = position

        return this
    }

    companion object {
        val sdk = com.qualcomm.robotcore.hardware.Servo::class.java
    }
}
