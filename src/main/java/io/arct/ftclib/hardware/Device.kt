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
abstract class Device internal constructor(private val sdk: HardwareDevice) {
    val connectionInfo: String = sdk.connectionInfo
    val name: String = sdk.deviceName
    val manufacturer: Manufacturer = Manufacturer.fromSdk(sdk.manufacturer)
    val version: Int = sdk.version

    /**
     * Close the device
     *
     * @return @this
     */
    fun close(): Device {
        sdk.close()

        return this
    }

    fun reset(): Device {
        sdk.resetDeviceConfigurationForOpMode()

        return this
    }

    enum class Manufacturer {
        Adafruit,
        AMS,
        Broadcom,
        HiTechnic,
        Lego,
        Lynx,
        Matrix,
        ModernRobotics,
        STMicroelectronics,
        Other,
        Unknown;

        internal val sdk: HardwareDevice.Manufacturer
            get() = when (this) {
                Adafruit -> HardwareDevice.Manufacturer.Adafruit
                AMS -> HardwareDevice.Manufacturer.AMS
                Broadcom -> HardwareDevice.Manufacturer.Broadcom
                HiTechnic -> HardwareDevice.Manufacturer.HiTechnic
                Lego -> HardwareDevice.Manufacturer.Lego
                Lynx -> HardwareDevice.Manufacturer.Lynx
                Matrix -> HardwareDevice.Manufacturer.Matrix
                ModernRobotics -> HardwareDevice.Manufacturer.ModernRobotics
                STMicroelectronics -> HardwareDevice.Manufacturer.STMicroelectronics
                Other -> HardwareDevice.Manufacturer.Other
                else -> HardwareDevice.Manufacturer.Unknown
            }

        companion object {
            internal fun fromSdk(value: HardwareDevice.Manufacturer): Manufacturer = when (value) {
                HardwareDevice.Manufacturer.Adafruit -> Adafruit
                HardwareDevice.Manufacturer.AMS -> AMS
                HardwareDevice.Manufacturer.Broadcom -> Broadcom
                HardwareDevice.Manufacturer.HiTechnic -> HiTechnic
                HardwareDevice.Manufacturer.Lego -> Lego
                HardwareDevice.Manufacturer.Lynx -> Lynx
                HardwareDevice.Manufacturer.Matrix -> Matrix
                HardwareDevice.Manufacturer.ModernRobotics -> ModernRobotics
                HardwareDevice.Manufacturer.STMicroelectronics -> STMicroelectronics
                HardwareDevice.Manufacturer.Other -> Other
                else -> Unknown
            }
        }
    }

    companion object {
        val sdk = HardwareDevice::class.java
    }
}
