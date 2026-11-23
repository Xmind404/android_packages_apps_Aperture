/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.aperture.models

enum class CameraMode(
    val supportedFlashModes: Set<FlashMode>,
) {
    /**
     * Photo capture with manual ISO, exposure time, exposure compensation, white balance and
     * focus. Entries are ordered as shown in the mode selector.
     */
    PRO(
        setOf(
            FlashMode.OFF,
            FlashMode.AUTO,
            FlashMode.ON,
        ),
    ),
    PHOTO(
        setOf(
            FlashMode.OFF,
            FlashMode.AUTO,
            FlashMode.ON,
            FlashMode.SCREEN,
        ),
    ),
    VIDEO(
        setOf(
            FlashMode.OFF,
            FlashMode.TORCH,
        ),
    ),
    QR(
        setOf(
            FlashMode.OFF,
            FlashMode.TORCH,
        ),
    ),
    ;

    /**
     * Whether this mode captures photos through the photo pipeline.
     */
    val isPhoto: Boolean
        get() = this == PHOTO || this == PRO
}
