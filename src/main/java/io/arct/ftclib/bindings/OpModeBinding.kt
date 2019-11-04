package io.arct.ftclib.bindings

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.arct.ftclib.eventloop.OperationMode

abstract class OpModeBinding : OpMode() {
    abstract val mode: OperationMode

    override fun init() = mode.init()
    override fun init_loop() = mode.initLoop()
    override fun start() = mode.start()
    override fun loop() = mode.loop()
    override fun stop() = mode.stop()
}
