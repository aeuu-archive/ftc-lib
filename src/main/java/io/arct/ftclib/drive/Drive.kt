package io.arct.ftclib.drive

import io.arct.ftclib.gamepad.Gamepad
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.util.Direction

/**
 * A controller that controls robot movement.
 *
 * @property robot The robot this controller controls.
 */
interface Drive {
    val robot: Robot

    /**
     * Programmatically move the robot.
     *
     * @param direction A [Double] representing the direction the robot should move
     * @param power The power of the motors
     * @param distance How far the robot should move (null will set power)
     *
     * @return @this
     */
    fun move(direction: Double, power: Double, distance: Double? = null): Drive

    fun move(direction: Direction, power: Double, distance: Double? = null) =
        move(direction.value, power, distance)

    /**
     * Programmatically rotate the robot.
     *
     * @param power The power to set the motors at
     * @param distance How many degrees the robot should rotate (null will set power)
     *
     * @return @this
     */
    fun rotate(power: Double, distance: Double? = null): Drive

    /**
     * Stop the robot.
     *
     * @return @this
     */
    fun stop(): Drive =
        move(0.0, 0.0)

    /**
     * Control the drivetrain using a gamepad.
     *
     * @param gamepad The gamepad used to control the drivetrain
     * @return @this
     */
    fun gamepad(gamepad: Gamepad, invert: Boolean = false): Drive
}
