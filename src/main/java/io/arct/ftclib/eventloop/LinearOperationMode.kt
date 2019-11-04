package io.arct.ftclib.eventloop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

/**
 * Represents an linear operation mode.
 *
 * @see OperationMode
 */
abstract class LinearOperationMode(private val sdk: LinearOpMode) : OperationMode(sdk) {
    val started: Boolean
        get() = sdk.isStarted

    val stopRequested: Boolean
        get() = sdk.isStopRequested

    val active: Boolean
        get() = sdk.opModeIsActive()

    fun idle() = sdk.idle()

    fun sleep(ms: Long) = sdk.sleep(ms)

    /**
     * Code to execute when the operation mode is run
     */
    abstract fun run()

    /**
     * @suppress
     */
    final override fun init() {}

    /**
     * @suppress
     */
    final override fun initLoop() {}

    /**
     * @suppress
     */
    final override fun loop() {}
}
