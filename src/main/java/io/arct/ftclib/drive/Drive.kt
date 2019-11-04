package io.arct.ftclib.drive

import io.arct.ftclib.gamepad.Gamepad
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.util.Direction

/**
 * A controller that controls robot movement
 *
 * @param robot The robot this controller controls
 */
abstract class Drive(val robot: Robot) {

    /**
     * Programmatically move the robot
     *
     * @param direction A [Double] representing the direction the robot should move
     * @param power The power of the motors
     * @param distance How far the robot should move
     */
    abstract fun move(direction: Double, power: Double, distance: Double? = null): Drive

    fun move(direction: Direction, power: Double, distance: Double? = null) =
        move(direction.value, power, distance)

    /**
     * Programmatically rotate the robot
     *
     * @param power The power to set the motors at
     * @param distance How many degrees the robot should rotate
     */
    abstract fun rotate(power: Double, distance: Double? = null): Drive

    fun stop(): Drive =
        move(0.0, 0.0)

    abstract fun gamepad(gamepad: Gamepad): Drive
}
