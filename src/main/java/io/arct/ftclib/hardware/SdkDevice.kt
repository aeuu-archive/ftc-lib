package io.arct.ftclib.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice

/**
 * Represents a hardware device
 *
 * @property connectionInfo The connection information of the device
 * @property name The name of the device
 * @property manufacturer The manufacturer of the device
 * @property version The devices version number
 */
interface SdkDevice<T : HardwareDevice> : Device {
    val sdk: T

    open class Impl<T : HardwareDevice> internal constructor(override val sdk: T) : SdkDevice<T> {
        override val connectionInfo: String = sdk.connectionInfo
        override val name: String = sdk.deviceName
        override val manufacturer: Device.Manufacturer = Device.Manufacturer.fromSdk(sdk.manufacturer)
        override val version: Int = sdk.version

        /**
         * Close the device
         *
         * @return @this
         */
        override fun close(): SdkDevice<T> {
            sdk.close()

            return this
        }

        override fun reset(): SdkDevice<T> {
            sdk.resetDeviceConfigurationForOpMode()

            return this
        }

        companion object {
            val sdk = HardwareDevice::class.java
        }
    }
}
