package io.arct.ftclib.bindings

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.arct.ftclib.eventloop.LinearOperationMode

abstract class LinearOpModeBinding : LinearOpMode() {
    abstract val mode: LinearOperationMode

    override fun runOpMode() {
        mode.init()

        waitForStart()

        mode.start()
        mode.run()
        mode.stop()
    }
}
