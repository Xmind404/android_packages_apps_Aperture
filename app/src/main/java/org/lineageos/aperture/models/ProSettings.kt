/*
 * SPDX-FileCopyrightText: 2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.aperture.models

/**
 * Manual capture settings used by [CameraMode.PRO]. A null value (or [WhiteBalance.Auto]) leaves
 * the setting to the camera's automatic control.
 */
data class ProSettings(
    /**
     * Sensor sensitivity (ISO). Setting it, or [exposureTimeNs], turns auto exposure off.
     */
    val iso: Int? = null,

    /**
     * Sensor exposure time in nanoseconds.
     */
    val exposureTimeNs: Long? = null,

    val whiteBalance: WhiteBalance = WhiteBalance.Auto,

    /**
     * Lens focus distance in diopters (0 = infinity), turns autofocus off.
     */
    val focusDistance: Float? = null,
) {
    val isManualExposure = iso != null || exposureTimeNs != null

    sealed interface WhiteBalance {
        data object Auto : WhiteBalance

        /**
         * One of the CameraMetadata.CONTROL_AWB_MODE_* presets.
         */
        data class Preset(val awbMode: Int) : WhiteBalance

        /**
         * A manual color temperature in Kelvin.
         */
        data class Temperature(val kelvin: Int) : WhiteBalance
    }
}

/**
 * What the current camera allows to be controlled manually in [CameraMode.PRO], read from its
 * CameraCharacteristics. Controls whose value here is null or empty are not offered.
 */
data class ProCapabilities(
    /**
     * Standard ISO stops within SENSOR_INFO_SENSITIVITY_RANGE, if MANUAL_SENSOR is supported.
     */
    val isoValues: List<Int>,

    /**
     * Standard exposure times (ns) within SENSOR_INFO_EXPOSURE_TIME_RANGE, capped at one second
     * to keep the preview usable, if MANUAL_SENSOR is supported.
     */
    val exposureTimeValues: List<Long>,

    /**
     * Supported CONTROL_AWB_MODE presets, excluding AUTO and OFF.
     */
    val awbPresets: List<Int>,

    /**
     * Whether a manual color temperature can be applied (MANUAL_POST_PROCESSING).
     */
    val supportsColorTemperature: Boolean,

    /**
     * LENS_INFO_MINIMUM_FOCUS_DISTANCE in diopters, or null without manual focus.
     */
    val minimumFocusDistance: Float?,
)
