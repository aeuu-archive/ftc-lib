package io.arct.ftclib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import io.arct.ftclib.bindings.types.SdkMotor
import io.arct.ftclib.hardware.controller.MotorController
import io.arct.ftclib.hardware.motor.Motor.ZeroPowerBehavior

/**
 * A hardware DC Motor
 *
 * @property position The current position of the motor
 * @property busy Is the motor currently busy
 * @property zeroPower The [ZeroPowerBehavior] of the motor when a power of zero is applied
 */
interface Motor : BasicMotor {
    val busy: Boolean
    val controller: MotorController
    val extended: Boolean
    var mode: Mode
    val port: Int
    var targetPosition: Int
    var targetPositionTolerance: Int?
    val position: Int
    var velocity: Double?
    var enabled: Boolean?
    var zeroPower: ZeroPowerBehavior

    /**
     * Move the motor to a position
     *
     * @param power The power to set
     * @param position The position to move to
     * @param wait Wait for the motor to reach the specified position
     *
     * @return @this
     */
    fun move(power: Double, position: Double, wait: Boolean = true): Motor
    fun move(power: Double, position: Double, callback: (Motor) -> Unit): Motor

    /**
     * Stop the motor
     *
     * @return @this
     */
    fun stop(): Motor

    /**
     * The behavior of a motor when a power of zero is applied
     */
    enum class ZeroPowerBehavior {
        /**
         * Let the motor coast and do not attempt to brake
         */
        Coast,

        /**
         * Apply brake
         */
        Brake,

        /**
         * Behavior is unknown
         */
        Unknown;
    }

    enum class Mode {
        Position,
        Encoder,
        Simple,
        Reset,
        Unknown;
    }

    open class Impl<T : SdkMotor>(val sdk: T) : Motor, BasicMotor by BasicMotor.Impl(sdk) {
        private val ext: DcMotorEx?
            get() = if (extended) sdk as DcMotorEx else null

        override val busy: Boolean
            get() = sdk.isBusy

        override val controller = MotorController.Impl(sdk.controller)

        override val extended: Boolean
            get() = sdk is DcMotorEx

        override var mode: Mode
            get() = fromSdk(sdk.mode)
            set(v) {
                sdk.mode = toSdk(v)
            }

        override val port: Int
            get() = sdk.portNumber

        override var targetPosition: Int
            get() = sdk.targetPosition
            set(v) { sdk.targetPosition = v }

        override var targetPositionTolerance: Int?
            get() = ext?.targetPositionTolerance
            set(v) { if (v != null) ext?.targetPositionTolerance = v }

        override val position: Int
            get() = sdk.currentPosition

        override var velocity: Double?
            get() = ext?.velocity
            set(v) { if (v != null) ext?.velocity = v }

        override var enabled: Boolean?
            get() = ext?.isMotorEnabled
            set(v) { if (v == true) ext?.setMotorEnable() else ext?.setMotorDisable() }

        override var zeroPower: ZeroPowerBehavior
            get() = fromSdk(sdk.zeroPowerBehavior)
            set(v) {
                sdk.zeroPowerBehavior = toSdk(v)
            }

        override fun move(power: Double, position: Double, wait: Boolean): Motor {
            mode = Mode.Reset

            targetPosition = (position * distanceConstant).toInt()

            mode = Mode.Position
            sdk.power = power

            if (wait) {
                while (busy)
                    Thread.sleep(1)

                stop()
            }

            return this
        }

        override fun move(power: Double, position: Double, callback: (Motor) -> Unit): Motor {
            move(power, position)
            callback.invoke(this)

            return this
        }

        override fun stop(): Motor {
            sdk.power = 0.0

            return this
        }

        companion object {
            val sdk = SdkMotor::class.java

            private fun fromSdk(value: DcMotor.RunMode): Mode = when (value) {
                DcMotor.RunMode.RUN_TO_POSITION -> Mode.Position
                DcMotor.RunMode.RUN_USING_ENCODER -> Mode.Encoder
                DcMotor.RunMode.RUN_WITHOUT_ENCODER -> Mode.Simple
                DcMotor.RunMode.STOP_AND_RESET_ENCODER -> Mode.Reset
                else -> Mode.Unknown
            }

            private fun toSdk(value: Mode): DcMotor.RunMode? = when (value) {
                Mode.Position -> DcMotor.RunMode.RUN_TO_POSITION
                Mode.Encoder -> DcMotor.RunMode.RUN_USING_ENCODER
                Mode.Simple -> DcMotor.RunMode.RUN_WITHOUT_ENCODER
                Mode.Reset -> DcMotor.RunMode.STOP_AND_RESET_ENCODER
                else -> null
            }

            private fun fromSdk(value: DcMotor.ZeroPowerBehavior): ZeroPowerBehavior = when (value) {
                DcMotor.ZeroPowerBehavior.BRAKE -> ZeroPowerBehavior.Brake
                DcMotor.ZeroPowerBehavior.FLOAT -> ZeroPowerBehavior.Coast
                else -> ZeroPowerBehavior.Unknown
            }

            private fun toSdk(value: ZeroPowerBehavior): DcMotor.ZeroPowerBehavior = when (value) {
                ZeroPowerBehavior.Brake -> DcMotor.ZeroPowerBehavior.BRAKE
                ZeroPowerBehavior.Coast -> DcMotor.ZeroPowerBehavior.FLOAT
                else -> DcMotor.ZeroPowerBehavior.UNKNOWN
            }
        }
    }

    companion object {
        /**
         * The ratio between a motor encoder step and a degree
         */
        var distanceConstant = 4.0
    }
}
