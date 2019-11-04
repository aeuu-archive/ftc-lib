package io.arct.ftclib.drive

import io.arct.ftclib.extentions.map
import io.arct.ftclib.gamepad.Gamepad
import io.arct.ftclib.hardware.motor.Motor
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.util.Angles
import kotlin.math.hypot

class MecanumDrive(robot: Robot, motors: List<String>) : Drive(robot) {
    private val lfm: Motor = robot.map(motors[0])
    private val rfm: Motor = robot.map(motors[1])
    private val lbm: Motor = robot.map(motors[2])
    private val rbm: Motor = robot.map(motors[3])

    init {
        lfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rfm.zeroPower = Motor.ZeroPowerBehavior.Brake
        lbm.zeroPower = Motor.ZeroPowerBehavior.Brake
        rbm.zeroPower = Motor.ZeroPowerBehavior.Brake
    }

    override fun move(direction: Double, power: Double, distance: Double?): MecanumDrive = if (distance != null) {
        val a = speed(direction - 90)
        val b = speed(direction)

        lfm.move(a * power, distance, wait = false)
        rfm.move(b * power, distance, wait = false)
        lbm.move(-b * power, distance, wait = false)
        rbm.move(-a * power, distance, wait = false)

        while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
            Thread.sleep(1)

        this
    } else {
        val a = speed(direction - 90)
        val b = speed(direction)

        lfm.power = a * power
        rfm.power = b * power
        lbm.power = -b * power
        rbm.power = -a * power

        this
    }

    override fun rotate(power: Double, distance: Double?): MecanumDrive = if (distance != null) {
        lfm.move(-power, distance, wait = false)
        lbm.move(-power, distance, wait = false)
        rfm.move(-power, distance, wait = false)
        rbm.move(-power, distance, wait = false)

        while (lfm.busy || rfm.busy || lbm.busy || rbm.busy)
            Thread.sleep(1)

        this
    } else {
        lfm.power = -power
        lbm.power = -power
        rfm.power = -power
        rbm.power = -power

        this
    }

    override fun gamepad(gamepad: Gamepad): MecanumDrive {
        move(Angles.fromCoordinates(gamepad.right.x, gamepad.right.y), hypot(gamepad.right.x, gamepad.right.y))

        return this
    }

    private fun speed(angle: Double): Double {
        val a = Angles.generalAngle(angle)

        return if (a in 0.0..90.0)
            1.0
        else if (a > 90 && a < 180)
            a.map(a, 90.0..180.0, 1.0..-1.0)
        else if (a in 180.0..270.0)
            -1.0
        else
            a.map(a, 270.0..360.0, -1.0..1.0)
    }
}
