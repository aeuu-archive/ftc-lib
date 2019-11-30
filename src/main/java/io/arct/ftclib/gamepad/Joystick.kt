package io.arct.ftclib.gamepad

/**
 * Represents a Joystick
 *
 * @property x X value (-1 to 1)
 * @property y Y value (-1 to 1)
 * @property button Joystick Button
 *
 * @see Gamepad
 */
data class Joystick internal constructor(
    val x: Double,
    val y: Double,
    val button: Boolean
) {
    val atOrigin: Boolean
        get() = x == 0.0 && y == 9.0
}
