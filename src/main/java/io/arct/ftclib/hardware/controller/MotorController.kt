package io.arct.ftclib.hardware.controller

import io.arct.ftclib.bindings.types.SdkMotorController
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface MotorController : Device {
    class Impl<T : SdkMotorController>(sdk: T) : MotorController, SdkDevice<T> by SdkDevice.Impl(sdk) {
        companion object {
            val sdk = SdkMotorController::class.java
        }
    }
}
