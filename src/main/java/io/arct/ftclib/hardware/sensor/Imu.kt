package io.arct.ftclib.hardware.sensor

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.I2cAddr
import io.arct.ftclib.bindings.types.SdkImu
import io.arct.ftclib.hardware.Device
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion
import org.firstinspires.ftc.robotcore.external.navigation.Temperature
import org.firstinspires.ftc.robotcore.external.navigation.Velocity

interface Imu : Device {
    val acceleration: Acceleration
    val orientation: Orientation
    val angularVelocity: AngularVelocity?
    val calibrationStatus: Byte
    val gravity: Acceleration
    val acceleratorCalibrated: Boolean
    val gyroCalibrated: Boolean
    val magnetometerCalibrated: Boolean
    val systemCalibrated: Boolean
    val linearAcceleration: Acceleration
    val magneticFlux: MagneticFlux
    val overallAcceleration: Acceleration
    val position: Position
    val quaternionOrientation: Quaternion?
    val temperature: Temperature
    val velocity: Velocity

    fun init(
        accelerationBandwidth: BNO055IMU.AccelBandwidth? = null,
        accelerationPowerMode: BNO055IMU.AccelPowerMode? = null,
        accelerationRange: BNO055IMU.AccelRange? = null,
        accelerationUnit: BNO055IMU.AccelUnit? = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC,
        accelerationIntegration: BNO055IMU.AccelerationIntegrator? = null,
        angleUnit: BNO055IMU.AngleUnit? = BNO055IMU.AngleUnit.DEGREES,
        calibrationData: BNO055IMU.CalibrationData? = null,
        calibrationDataFile: String? = null,
        gyroBandwidth: BNO055IMU.GyroBandwidth? = null,
        gyroPowerMode: BNO055IMU.GyroPowerMode? = null,
        gyroRange: BNO055IMU.GyroRange? = null,
        i2cAddress: I2cAddr? = null,
        enableLogging: Boolean? = false,
        loggingTag: String? = "IMU",
        magOpMode: BNO055IMU.MagOpMode? = null,
        magPowerMode: BNO055IMU.MagPowerMode? = null,
        magRate: BNO055IMU.MagRate? = null,
        mode: BNO055IMU.SensorMode? = null,
        pitchMode: BNO055IMU.PitchMode? = null,
        temperatureUnit: BNO055IMU.TempUnit? = null,
        useExternalCrystal: Boolean? = null
    ): Imu

    class Impl<T : SdkImu>(val sdk: T) : Imu {
        override val connectionInfo: String = sdk.systemStatus.toString()
        override val name: String = sdk.systemStatus.name
        override val manufacturer: Device.Manufacturer = Device.Manufacturer.Other
        override val version: Int = 0

        override val acceleration: Acceleration
            get() = sdk.acceleration

        override val orientation: Orientation
            get() = sdk.angularOrientation

        override val angularVelocity: AngularVelocity?
            get() = sdk.angularVelocity

        override val calibrationStatus: Byte
            get() = sdk.calibrationStatus.calibrationStatus

        override val gravity: Acceleration
            get() = sdk.gravity

        override val acceleratorCalibrated: Boolean
            get() = sdk.isAccelerometerCalibrated

        override val gyroCalibrated: Boolean
            get() = sdk.isGyroCalibrated

        override val magnetometerCalibrated: Boolean
            get() = sdk.isMagnetometerCalibrated

        override val systemCalibrated: Boolean
            get() = sdk.isSystemCalibrated

        override val linearAcceleration: Acceleration
            get() = sdk.linearAcceleration

        override val magneticFlux: MagneticFlux
            get() = sdk.magneticFieldStrength

        override val overallAcceleration: Acceleration
            get() = sdk.overallAcceleration

        override val position: Position
            get() = sdk.position

        override val quaternionOrientation: Quaternion?
            get() = sdk.quaternionOrientation

        override val temperature: Temperature
            get() = sdk.temperature

        override val velocity: Velocity
            get() = sdk.velocity

        override fun close(): Device {
            sdk.close()

            return this
        }

        override fun reset(): Device =
            init()

        override fun init(
            accelerationBandwidth: BNO055IMU.AccelBandwidth?,
            accelerationPowerMode: BNO055IMU.AccelPowerMode?,
            accelerationRange: BNO055IMU.AccelRange?,
            accelerationUnit: BNO055IMU.AccelUnit?,
            accelerationIntegration: BNO055IMU.AccelerationIntegrator?,
            angleUnit: BNO055IMU.AngleUnit?,
            calibrationData: BNO055IMU.CalibrationData?,
            calibrationDataFile: String?,
            gyroBandwidth: BNO055IMU.GyroBandwidth?,
            gyroPowerMode: BNO055IMU.GyroPowerMode?,
            gyroRange: BNO055IMU.GyroRange?,
            i2cAddress: I2cAddr?,
            enableLogging: Boolean?,
            loggingTag: String?,
            magOpMode: BNO055IMU.MagOpMode?,
            magPowerMode: BNO055IMU.MagPowerMode?,
            magRate: BNO055IMU.MagRate?,
            mode: BNO055IMU.SensorMode?,
            pitchMode: BNO055IMU.PitchMode?,
            temperatureUnit: BNO055IMU.TempUnit?,
            useExternalCrystal: Boolean?
        ): Imu {
            val p = BNO055IMU.Parameters()

            accelerationBandwidth?.let { p.accelBandwidth = it }
            accelerationPowerMode?.let { p.accelPowerMode = it }
            accelerationRange?.let { p.accelRange = it }
            accelerationUnit?.let { p.accelUnit = it }
            accelerationIntegration?.let { p.accelerationIntegrationAlgorithm = it }
            angleUnit?.let { p.angleUnit = it }
            calibrationData?.let { p.calibrationData = it }
            calibrationDataFile?.let { p.calibrationDataFile = it }
            gyroBandwidth?.let { p.gyroBandwidth = it }
            gyroPowerMode?.let { p.gyroPowerMode = it }
            gyroRange?.let { p.gyroRange = it }
            i2cAddress?.let { p.i2cAddr = it }
            enableLogging?.let { p.loggingEnabled = it }
            loggingTag?.let { p.loggingTag = it }
            magOpMode?.let { p.magOpMode = it }
            magPowerMode?.let { p.magPowerMode = it }
            magRate?.let { p.magRate = it }
            mode?.let { p.mode = it }
            pitchMode?.let { p.pitchMode = it }
            temperatureUnit?.let { p.temperatureUnit = it }
            useExternalCrystal?.let { p.useExternalCrystal = it }

            sdk.initialize(p)

            return this
        }

        companion object {
            val sdk = SdkImu::class.java
        }
    }
}
