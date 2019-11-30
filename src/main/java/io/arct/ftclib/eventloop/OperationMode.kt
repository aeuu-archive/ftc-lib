package io.arct.ftclib.eventloop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.arct.ftclib.robot.Robot
import io.arct.ftclib.telemetry.Telemetry
import kotlin.reflect.full.primaryConstructor

/**
 * Represents an operation mode.
 *
 * An operation mode is a program that can be executed on the robot.
 *
 * Use [Bind] to bind an operation mode to the standard SDK.
 *
 * @property robot The [Robot] associated with this operation mode.
 * @property log The [Telemetry] instance of this operation mode.
 * @property time The time.
 *
 * @see LinearOperationMode
 */
abstract class OperationMode {
    private val sdk: OpMode = curr!!

    val robot: Robot = Robot(sdk)
    val log: Telemetry = Telemetry(sdk.telemetry)

    val time: Double
        get() = sdk.time

    /**
     * Exit the operation mode.
     */
    fun exit(): Nothing {
        sdk.requestOpModeStop()

        while (true)
            Thread.sleep(100)
    }

    /**
     * Code to execute once when the operation mode is in the init phase.
     */
    abstract fun init()

    /**
     * Code to loop when the operation mode is in the init phase (Optional).
     */
    open fun initLoop() {}

    /**
     * Code to execute once when the operation mode is run, before the loop begins (Optional).
     */
    open fun start() {}

    /**
     * Code to loop while running.
     */
    abstract fun loop()

    /**
     * Code to execute once when the operation mode disabled (Optional).
     */
    open fun stop() {}

    companion object {
        // Workaround for passing in constructor values. Dangerous, but works!
        var curr: OpMode? = null

        inline fun <reified T : OperationMode> of(sdk: OpMode): T {
            curr = sdk
            return T::class.primaryConstructor!!.call()
        }
    }

    /**
     * Bind an operation mode to the standard SDK.
     *
     * @param type The type of the operation mode ([Type])
     * @param name The name of the operation mode (Defaults to the class name)
     * @param group The group of the operation mode (Defaults to none)
     *
     * @property type The type of the operation mode ([Type]).
     * @property name The name of the operation mode (Defaults to the class name).
     * @property group The group of the operation mode (Defaults to none).
     */
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.CLASS)
    @MustBeDocumented
    annotation class Bind(val type: Type, val name: String = "", val group: String = "")

    /**
     * The type of an operation mode.
     *
     * @property Autonomous An autonomous operation mode without any user input.
     * @property Operated An operation mode that is user operated.
     * @property Disabled A disabled operation mode.
     */
    enum class Type {
        Autonomous,
        Operated,
        Disabled
    }
}
