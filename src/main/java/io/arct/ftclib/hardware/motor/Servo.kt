package io.arct.ftclib.hardware.motor

import io.arct.ftclib.bindings.types.SdkServo
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice
import io.arct.ftclib.hardware.controller.ServoController

/**
 * A hardware servo
 */
interface Servo : Device {
    val controller: ServoController
    var position: Double

    open class Impl<T : SdkServo>(sdk: T) : Servo, SdkDevice<T> by SdkDevice.Impl(sdk) {
        override val controller: ServoController
            get() = ServoController.Impl(sdk.controller)

        override var position: Double
            get() = sdk.position
            set(v) { sdk.position = v }

        companion object {
            val sdk = SdkServo::class.java
        }
    }
}
