package io.arct.ftclib.exceptions

/**
 * An exception of
 *
 * @param device The type of the device
 * @param identifier The identifier of the device
 */
class CouldNotFindDeviceException(device: String?, identifier: String) : Exception("Could not find a ${device ?: "Device"} with identifier $identifier")
