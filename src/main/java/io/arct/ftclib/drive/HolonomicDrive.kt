package io.arct.ftclib.drive

import io.arct.ftclib.extentions.round
import io.arct.ftclib.gamepad.Gamepad
import io.arct.ftclib.hardware.motor.Motor
import io.arct.ftclib.robot.Robot
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A drive controller for a robot using holonomic drive.
 *
 * @param robot The robot this controller is controlling.
 * @param motors A [List] of motors in the holonomic drive array.
 */
class HolonomicDrive(override val robot: Robot, vararg motors: Motor) : Drive {
    private val lfm: Motor = motors[0]
    private val rfm: Motor = motors[1]
    private val lbm: Motor = motors[2]
    private val rbm: Motor = motors[3]

    init {
        lfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        lbm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rbm.zeroPower = Motor.ZeroPowerBehavior.Brake
    }

    /**
     * @see Drive
     */
    override fun move(direction: Double, power: Double, distance: Double?, turn: Double): HolonomicDrive =
        if (distance != null) {
            val x = sin(direction * PI / 180).round(14)
            val y = cos(direction * PI / 180).round(14)

            lfm.move((+y + x) * power, distance * distanceConstant, wait = false)
            rfm.move((-y + x) * power, distance * distanceConstant, wait = false)
            lbm.move((+y - x) * power, distance * distanceConstant, wait = false)
            rbm.move((-y - x) * power, distance * distanceConstant, wait = false)

            while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
                Thread.sleep(1)

            this
        } else {
            val x = sin(direction * PI / 180).round(14)
            val y = cos(direction * PI / 180).round(14)

            lfm.power = (+y + x) * power
            rfm.power = (-y + x) * power
            lbm.power = (+y - x) * power
            rbm.power = (-y - x) * power

            this
        }

    /**
     * @see Drive
     */
    override fun rotate(power: Double, distance: Double?): HolonomicDrive = if (distance != null) {
        lfm.move(power, distance * rotationConstant, wait = false)
        rfm.move(power, distance * rotationConstant, wait = false)
        lbm.move(power, distance * rotationConstant, wait = false)
        rbm.move(power, distance * rotationConstant, wait = false)

        while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
            Thread.sleep(1)

        this
    } else {
        lfm.power = power
        rfm.power = power
        lbm.power = power
        rbm.power = power

        this
    }

    /**
     * @see Drive
     */
    override fun gamepad(gamepad: Gamepad, invert: Boolean): HolonomicDrive {
        val main = if (invert) gamepad.right else gamepad.left
        val rotation = if (invert) gamepad.left else gamepad.right

        val x = main.x
        val y = main.y
        val r = rotation.x

        lfm.power = (+y + x + r)
        rfm.power = (-y + x + r)
        lbm.power = (+y - x + r)
        rbm.power = (-y - x + r)

        return this
    }

    companion object {
        /**
         * The ratio between a holonomic drive unit and a centimeter.
         */
        var distanceConstant = 1.0

        /**
         * The ratio between a holonomic drive rotation unit and a degree.
         */
        var rotationConstant = 1.0
    }
}
