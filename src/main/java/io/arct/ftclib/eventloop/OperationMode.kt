package io.arct.ftclib.eventloop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.telemetry.Telemetry

/**
 * Represents an operation mode.
 *
 * An operation mode is a program that can be executed on the robot.
 *
 * Tag a subclass of OperationMode with either [com.qualcomm.robotcore.eventloop.opmode.TeleOp] or [com.qualcomm.robotcore.eventloop.opmode.Autonomous] to specify the category of this operation mode
 *
 * @property robot The [Robot] associated with this operation mode
 * @property log The [Telemetry] instance of this operation mode
 *
 * @see LinearOperationMode
 */
abstract class OperationMode(private val sdk: OpMode) {
    val robot = Robot(sdk)
    val log = Telemetry(sdk.telemetry)

    val time: Double
        get() = sdk.time

    fun exit() = sdk.requestOpModeStop()

    /**
     * Code to execute once when the operation mode is in the init phase
     */
    abstract fun init()

    /**
     * Code to loop when the operation mode is in the init phase (Optional)
     */
    open fun initLoop() {}

    /**
     * Code to execute once when the operation mode is run, before the loop begins (Optional)
     */
    open fun start() {}

    /**
     * Code to loop while running
     */
    abstract fun loop()

    /**
     * Code to execute once when the operation mode disabled (Optional)
     */
    open fun stop() {}

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.CLASS)
    @MustBeDocumented
    annotation class Bind(val type: Type, val name: String = "", val group: String = "")

    enum class Type {
        Autonomous,
        Operated,
        Disabled
    }
}
