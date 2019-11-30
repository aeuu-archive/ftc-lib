package io.arct.ftclib.robot

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.arct.ftclib.gamepad.Gamepad

/**
 * A hardware robot
 *
 * @property map A [HardwareMap] of connected devices
 * @property gamepad The [Gamepad]s linked to the robot
 */
class Robot internal constructor(private val sdk: OpMode) {
    var map: HardwareMap = HardwareMap(sdk)
    val gamepad: List<Gamepad>
        get() = listOf(Gamepad.from(sdk.gamepad1), Gamepad.from(sdk.gamepad2))
}
