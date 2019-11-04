package io.arct.ftclib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import io.arct.ftclib.hardware.controller.MotorController
import io.arct.ftclib.hardware.motor.Motor.ZeroPowerBehavior

/**
 * A hardware DC Motor
 *
 * @property position The current position of the motor
 * @property busy Is the motor currently busy
 * @property zeroPower The [ZeroPowerBehavior] of the motor when a power of zero is applied
 */
class Motor internal constructor(private val sdk: DcMotor) : BasicMotor(sdk) {
    private val ext: DcMotorEx? = if (extended) sdk as DcMotorEx else null

    val busy: Boolean
        get() = sdk.isBusy

    val controller: MotorController =
        MotorController(sdk.controller, port)

    val extended: Boolean
        get() = sdk is DcMotorEx

    var mode: Mode
        get() = Mode.fromSdk(sdk.mode)
        set(v) { sdk.mode = v.sdk }

    val port: Int
        get() = sdk.portNumber

    var targetPosition: Int
        get() = sdk.targetPosition
        set(v) { sdk.targetPosition = v }

    var targetPositionTolerance: Int?
        get() = ext?.targetPositionTolerance
        set(v) { if (v != null) ext?.targetPositionTolerance = v }

    val position: Int
        get() = sdk.currentPosition

    var velocity: Double?
        get() = ext?.velocity
        set(v) { if (v != null) ext?.velocity = v }

    var enabled: Boolean?
        get() = ext?.isMotorEnabled
        set(v) { if (v == true) ext?.setMotorEnable() else ext?.setMotorDisable() }

    var zeroPower: ZeroPowerBehavior
        get() = ZeroPowerBehavior.fromSdk(sdk.zeroPowerBehavior)
        set(v) { sdk.zeroPowerBehavior = v.sdk }

    /**
     * Move the motor to a position
     *
     * @param power The power to set
     * @param position The position to move to
     * @param wait Wait for the motor to reach the specified position
     *
     * @return @this
     */
    fun move(power: Double, position: Double, wait: Boolean = true): Motor {
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

    fun move(power: Double, position: Double, callback: (Motor) -> Unit): Motor {
        move(power, position)
        callback.invoke(this)

        return this
    }

    /**
     * Stop the motor
     *
     * @return @this
     */
    fun stop(): Motor {
        sdk.power = 0.0

        return this
    }

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

        internal val sdk: DcMotor.ZeroPowerBehavior
            get() = when (this) {
                Brake -> DcMotor.ZeroPowerBehavior.BRAKE
                Coast -> DcMotor.ZeroPowerBehavior.FLOAT
                else -> DcMotor.ZeroPowerBehavior.UNKNOWN
            }

        companion object {
            internal fun fromSdk(value: DcMotor.ZeroPowerBehavior): ZeroPowerBehavior = when (value) {
                DcMotor.ZeroPowerBehavior.BRAKE -> Brake
                DcMotor.ZeroPowerBehavior.FLOAT -> Coast
                else -> Unknown
            }
        }
    }

    enum class Mode {
        Position,
        Encoder,
        Simple,
        Reset,
        Unknown;

        internal val sdk: DcMotor.RunMode?
            get() = when (this) {
                Position -> DcMotor.RunMode.RUN_TO_POSITION
                Encoder -> DcMotor.RunMode.RUN_USING_ENCODER
                Simple -> DcMotor.RunMode.RUN_WITHOUT_ENCODER
                Reset -> DcMotor.RunMode.STOP_AND_RESET_ENCODER
                else -> null
            }

        companion object {
            internal fun fromSdk(value: DcMotor.RunMode): Mode = when (value) {
                DcMotor.RunMode.RUN_TO_POSITION -> Position
                DcMotor.RunMode.RUN_USING_ENCODER -> Encoder
                DcMotor.RunMode.RUN_WITHOUT_ENCODER -> Simple
                DcMotor.RunMode.STOP_AND_RESET_ENCODER -> Reset
                else -> Unknown
            }
        }
    }

    companion object {
        val sdk = DcMotor::class.java

        /**
         * The ratio between a motor encoder step and a degree
         */
        var distanceConstant = 4.0
    }
}
