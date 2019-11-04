package io.arct.ftclib.hardware.controller

import com.qualcomm.robotcore.hardware.DcMotorController
import io.arct.ftclib.hardware.Device

class MotorController internal constructor(private val sdk: DcMotorController, private val port: Int) : Device(sdk)
