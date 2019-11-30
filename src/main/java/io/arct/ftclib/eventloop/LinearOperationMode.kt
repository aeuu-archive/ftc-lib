package io.arct.ftclib.eventloop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

/**
 * Represents an linear operation mode.
 *
 * @property started Whether the operation mode is started.
 * @property stopRequested Whether a stop has been requested.
 * @property active Whether the operation mode is active currently.
 *
 * @see OperationMode
 */
abstract class LinearOperationMode : OperationMode() {
    private val sdk: LinearOpMode = curr!! as LinearOpMode

    val started: Boolean
        get() = sdk.isStarted

    val stopRequested: Boolean
        get() = sdk.isStopRequested

    val active: Boolean
        get() = sdk.opModeIsActive()

    /**
     * Tell the operation mode to idle for a bit.
     *
     * @return @this
     */
    fun idle(): LinearOperationMode {
        sdk.idle()

        return this
    }

    /**
     * Sleep for a specified amount of time.
     *
     * @param ms Time to wait in milliseconds
     *
     * @return @this
     */
    fun sleep(ms: Long): LinearOperationMode {
        sdk.sleep(ms)

        return this
    }

    /**
     * Code to execute when the operation mode is run.
     */
    abstract fun run()

    override fun init() {}

    final override fun loop() {}
    final override fun initLoop() {}
}
