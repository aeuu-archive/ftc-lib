package io.arct.ftclib.telemetry

/**
 * Sends information to the Driver Station
 *
 * @see io.arct.ftclib.robot.Robot
 */
class Telemetry internal constructor(private val sdk: org.firstinspires.ftc.robotcore.external.Telemetry) {
    /**
     * Automatic log clearing on [Telemetry.update]
     */
    var autoClear: Boolean
        get() = sdk.isAutoClear
        set(v) { sdk.isAutoClear = v }

    /**
     * Add a line to telemetry
     *
     * @param line The line to add
     * @return The telemetry instance
     */
    fun add(line: String): Telemetry {
        sdk.addLine(line)

        return this
    }

    /**
     * Add lines to telemetry
     *
     * @param data A [List] of lines to add
     * @return The [Telemetry] instance
     */
    fun add(data: List<String>): Telemetry {
        for (line in data)
            sdk.addLine(line)

        return this
    }

    /**
     * Update/Send queued telemetry data
     */
    fun update(): Telemetry {
        sdk.update()

        return this
    }

    fun clear(): Telemetry {
        sdk.clear()

        return this
    }

    fun clearAll(): Telemetry {
        sdk.clearAll()

        return this
    }
}
