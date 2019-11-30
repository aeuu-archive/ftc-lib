package io.arct.ftclib.drive

import io.arct.ftclib.extentions.map
import io.arct.ftclib.gamepad.Gamepad
import io.arct.ftclib.hardware.motor.Motor
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.util.Angles
import io.arct.ftclib.util.Direction
import kotlin.math.hypot

/**
 * A drive controller for a robot using mecanum drive.
 *
 * @param robot The robot this controller is controlling.
 * @param motors A [List] of motors in the mecanum drive array.
 */
class MecanumDrive(override val robot: Robot, vararg motors: Motor, private var rotation: (() -> Number)? = null) : Drive {
    private val lfm: Motor = motors[0].also { it.direction = it.direction.inverse }
    private val rfm: Motor = motors[1]
    private val lbm: Motor = motors[2].also { it.direction = it.direction.inverse }
    private val rbm: Motor = motors[3]

    var fieldCentric: Boolean = true
        get() = rotation != null && field

    init {
        lfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        lbm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rbm.zeroPower = Motor.ZeroPowerBehavior.Brake
    }

    /**
     * @see Drive
     */
    override fun move(direction: Double, power: Double, distance: Double?): MecanumDrive = if (distance != null) {
        val dir = direction + if (fieldCentric) rotation!!().toDouble() else 0.0

        val a = speed(-dir + 90)
        val b = speed(-dir)

        lfm.move(a * power, distance * distanceConstant, wait = false)
        rfm.move(b * power, distance * distanceConstant, wait = false)
        lbm.move(b * power, distance * distanceConstant, wait = false)
        rbm.move(a * power, distance * distanceConstant, wait = false)

        while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
            Thread.sleep(1)

        this
    } else {
        val dir = direction + if (fieldCentric) rotation!!().toDouble() else 0.0

        val a = speed(-dir + 90)
        val b = speed(-dir)

        lfm.power = a * power
        rfm.power = b * power
        lbm.power = b * power
        rbm.power = a * power

        this
    }

    /**
     * @see Drive
     */
    override fun rotate(power: Double, distance: Double?): MecanumDrive = if (distance != null) {
        lfm.move(power, distance * rotationConstant, wait = false)
        lbm.move(power, distance * rotationConstant, wait = false)
        rfm.move(-power, distance * rotationConstant, wait = false)
        rbm.move(-power, distance * rotationConstant, wait = false)

        while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
            Thread.sleep(1)

        this
    } else {
        lfm.power = power
        lbm.power = power
        rfm.power = -power
        rbm.power = -power

        this
    }

    /**
     * @see Drive
     */
    override fun gamepad(gamepad: Gamepad, invert: Boolean): MecanumDrive {
        val main = if (invert) gamepad.right else gamepad.left
        val rotation = if (invert) gamepad.left else gamepad.right

        if (gamepad.dpad.up || gamepad.dpad.down || gamepad.dpad.left || gamepad.dpad.right) {
            move(when {
                gamepad.dpad.up -> Direction.Forward
                gamepad.dpad.down -> Direction.Backward
                gamepad.dpad.left -> Direction.Left
                gamepad.dpad.right -> Direction.Right

                else -> return this
            }, 0.2)

            return this
        }

        if (rotation.x == 0.0) {
            move(Angles.fromCoordinates(main.x, main.y), hypot(main.x, main.y))

            return this
        }

        rotate(rotation.x)

        return this
    }

    private fun speed(angle: Double): Double {
        val a = Angles.generalAngle(angle)

        return if (a in 0.0..90.0)
            1.0
        else if (a > 90 && a < 180)
            a.map(90.0..180.0, 1.0..-1.0)
        else if (a in 180.0..270.0)
            -1.0
        else
            a.map(270.0..360.0, -1.0..1.0)
    }

    companion object {
        var distanceConstant = 1.0
        var rotationConstant = 1.0
    }
}
