package io.arct.ftclib.hardware.device

import com.qualcomm.robotcore.hardware.DigitalChannel
import io.arct.ftclib.bindings.types.SdkDigitalDevice
import io.arct.ftclib.hardware.Device
import io.arct.ftclib.hardware.SdkDevice

interface DigitalDevice : Device {
    var value: Boolean
    var mode: Mode

    enum class Mode {
        Input,
        Output,
        Both
    }

    open class Impl<T : SdkDigitalDevice>(sdk: T) : DigitalDevice, SdkDevice<T> by SdkDevice.Impl(sdk) {
        private var modeBoth = true

        override var value
            get() = when (mode) {
                Mode.Both -> {
                    sdk.mode = DigitalChannel.Mode.OUTPUT
                    sdk.state
                }
                Mode.Output -> sdk.state
                else -> throw Exception("Cannot Get Value of Digital Device on Input Mode!")
            }
            set(v) = when (mode) {
                Mode.Both -> {
                    sdk.mode = DigitalChannel.Mode.INPUT
                    sdk.state = v
                }
                Mode.Input -> sdk.state = v
                else -> throw Exception("Cannot Set Value of Digital Device on Output Mode!")
            }

        override var mode: Mode
            get() = if (modeBoth) Mode.Both else when (sdk.mode) {
                DigitalChannel.Mode.INPUT -> Mode.Input
                DigitalChannel.Mode.OUTPUT -> Mode.Output
                else -> Mode.Both
            }
            set(v) = when (v) {
                Mode.Input -> {
                    modeBoth = false; sdk.mode = DigitalChannel.Mode.INPUT
                }
                Mode.Output -> {
                    modeBoth = false; sdk.mode = DigitalChannel.Mode.OUTPUT
                }
                Mode.Both -> modeBoth = true
            }

        companion object {
            val sdk = SdkDigitalDevice::class.java
        }
    }
}
