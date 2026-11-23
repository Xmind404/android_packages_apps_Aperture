/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.aperture.ext

import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.ColorSpaceTransform
import android.hardware.camera2.params.RggbChannelVector
import android.os.Build
import androidx.camera.camera2.interop.CaptureRequestOptions
import androidx.core.util.toRange
import org.lineageos.aperture.models.ColorCorrectionAberrationMode
import org.lineageos.aperture.models.DistortionCorrectionMode
import org.lineageos.aperture.models.EdgeMode
import org.lineageos.aperture.models.FrameRate
import org.lineageos.aperture.models.HotPixelMode
import org.lineageos.aperture.models.NoiseReductionMode
import org.lineageos.aperture.models.ProSettings
import org.lineageos.aperture.models.ShadingMode
import org.lineageos.aperture.models.VideoStabilizationMode

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun <ValueT> CaptureRequestOptions.Builder.setOrClearCaptureRequestOption(
    key: CaptureRequest.Key<ValueT>,
    value: ValueT?
): CaptureRequestOptions.Builder = value?.let {
    setCaptureRequestOption(key, it)
} ?: clearCaptureRequestOption(key)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setFrameRate(
    frameRate: FrameRate?
) = setOrClearCaptureRequestOption(
    CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
    frameRate?.range?.toRange(),
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setVideoStabilizationMode(
    videoStabilizationMode: VideoStabilizationMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
    when (videoStabilizationMode) {
        VideoStabilizationMode.OFF -> CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF
        VideoStabilizationMode.ON -> CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON
        VideoStabilizationMode.ON_PREVIEW ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_PREVIEW_STABILIZATION
            } else {
                CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON
            }

        null -> null
    }
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setEdgeMode(
    edgeMode: EdgeMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.EDGE_MODE,
    when (edgeMode) {
        EdgeMode.OFF -> CameraMetadata.EDGE_MODE_OFF
        EdgeMode.FAST -> CameraMetadata.EDGE_MODE_FAST
        EdgeMode.HIGH_QUALITY -> CameraMetadata.EDGE_MODE_HIGH_QUALITY
        EdgeMode.ZERO_SHUTTER_LAG -> CameraMetadata.EDGE_MODE_ZERO_SHUTTER_LAG
        null -> null
    }
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setNoiseReductionMode(
    noiseReductionMode: NoiseReductionMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.NOISE_REDUCTION_MODE,
    when (noiseReductionMode) {
        NoiseReductionMode.OFF -> CameraMetadata.NOISE_REDUCTION_MODE_OFF
        NoiseReductionMode.FAST -> CameraMetadata.NOISE_REDUCTION_MODE_FAST
        NoiseReductionMode.HIGH_QUALITY -> CameraMetadata.NOISE_REDUCTION_MODE_HIGH_QUALITY
        NoiseReductionMode.MINIMAL -> CameraMetadata.NOISE_REDUCTION_MODE_MINIMAL
        NoiseReductionMode.ZERO_SHUTTER_LAG -> CameraMetadata.NOISE_REDUCTION_MODE_ZERO_SHUTTER_LAG
        null -> null
    }
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setShadingMode(
    shadingMode: ShadingMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.SHADING_MODE,
    when (shadingMode) {
        ShadingMode.OFF -> CameraMetadata.SHADING_MODE_OFF
        ShadingMode.FAST -> CameraMetadata.SHADING_MODE_FAST
        ShadingMode.HIGH_QUALITY -> CameraMetadata.SHADING_MODE_HIGH_QUALITY
        null -> null
    }
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setColorCorrectionAberrationMode(
    colorCorrectionAberrationMode: ColorCorrectionAberrationMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE,
    when (colorCorrectionAberrationMode) {
        ColorCorrectionAberrationMode.OFF -> CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_OFF
        ColorCorrectionAberrationMode.FAST -> CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_FAST
        ColorCorrectionAberrationMode.HIGH_QUALITY ->
            CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY

        null -> null
    }
)

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setDistortionCorrectionMode(
    distortionCorrectionMode: DistortionCorrectionMode?
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    setOrClearCaptureRequestOption(
        CaptureRequest.DISTORTION_CORRECTION_MODE,
        when (distortionCorrectionMode) {
            DistortionCorrectionMode.OFF -> CameraMetadata.DISTORTION_CORRECTION_MODE_OFF
            DistortionCorrectionMode.FAST -> CameraMetadata.DISTORTION_CORRECTION_MODE_FAST
            DistortionCorrectionMode.HIGH_QUALITY ->
                CameraMetadata.DISTORTION_CORRECTION_MODE_HIGH_QUALITY

            null -> null
        }
    )
} else {
    this
}

@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setHotPixelMode(
    hotPixelMode: HotPixelMode?
) = setOrClearCaptureRequestOption(
    CaptureRequest.HOT_PIXEL_MODE,
    when (hotPixelMode) {
        HotPixelMode.OFF -> CameraMetadata.HOT_PIXEL_MODE_OFF
        HotPixelMode.FAST -> CameraMetadata.HOT_PIXEL_MODE_FAST
        HotPixelMode.HIGH_QUALITY -> CameraMetadata.HOT_PIXEL_MODE_HIGH_QUALITY
        null -> null
    }
)

/**
 * Exposure time used with a manual ISO while the exposure time itself is automatic, and ISO used
 * with a manual exposure time while ISO is automatic. Auto exposure must be off for either manual
 * value to take effect, so the other one needs a concrete value.
 */
private const val PRO_FALLBACK_EXPOSURE_TIME_NS = 1_000_000_000L / 60
private const val PRO_FALLBACK_ISO = 100

private val IDENTITY_COLOR_TRANSFORM = ColorSpaceTransform(
    intArrayOf(
        1, 1, 0, 1, 0, 1,
        0, 1, 1, 1, 0, 1,
        0, 1, 0, 1, 1, 1,
    )
)

/**
 * Applies [CameraMode.PRO] manual settings. Pass null outside PRO to leave every related key to
 * CameraX and the camera's automatic control.
 */
@androidx.camera.camera2.interop.ExperimentalCamera2Interop
fun CaptureRequestOptions.Builder.setProSettings(
    proSettings: ProSettings?,
): CaptureRequestOptions.Builder {
    if (proSettings == null) {
        return this
    }

    if (proSettings.isManualExposure) {
        setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_OFF)
        setCaptureRequestOption(
            CaptureRequest.SENSOR_SENSITIVITY, proSettings.iso ?: PRO_FALLBACK_ISO
        )
        setCaptureRequestOption(
            CaptureRequest.SENSOR_EXPOSURE_TIME,
            proSettings.exposureTimeNs ?: PRO_FALLBACK_EXPOSURE_TIME_NS
        )
    }

    when (val whiteBalance = proSettings.whiteBalance) {
        ProSettings.WhiteBalance.Auto -> Unit

        is ProSettings.WhiteBalance.Preset -> setCaptureRequestOption(
            CaptureRequest.CONTROL_AWB_MODE, whiteBalance.awbMode
        )

        is ProSettings.WhiteBalance.Temperature -> {
            setCaptureRequestOption(
                CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_OFF
            )
            setCaptureRequestOption(
                CaptureRequest.COLOR_CORRECTION_MODE,
                CameraMetadata.COLOR_CORRECTION_MODE_TRANSFORM_MATRIX
            )
            setCaptureRequestOption(
                CaptureRequest.COLOR_CORRECTION_GAINS, colorTemperatureToGains(whiteBalance.kelvin)
            )
            setCaptureRequestOption(
                CaptureRequest.COLOR_CORRECTION_TRANSFORM, IDENTITY_COLOR_TRANSFORM
            )
        }
    }

    proSettings.focusDistance?.let {
        setCaptureRequestOption(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_OFF)
        setCaptureRequestOption(CaptureRequest.LENS_FOCUS_DISTANCE, it)
    }

    return this
}

/**
 * Approximates the white balance gains for a light source of the given color temperature: the
 * RGB color of a black body at [kelvin] (Tanner Helland's fit of the Planckian locus), inverted
 * and normalized to the green channel.
 */
private fun colorTemperatureToGains(kelvin: Int): RggbChannelVector {
    val t = kelvin.coerceIn(1000, 40000) / 100.0

    val red = if (t <= 66) 255.0 else 329.698727446 * Math.pow(t - 60, -0.1332047592)
    val green = if (t <= 66) {
        99.4708025861 * Math.log(t) - 161.1195681661
    } else {
        288.1221695283 * Math.pow(t - 60, -0.0755148492)
    }
    val blue = when {
        t >= 66 -> 255.0
        t <= 19 -> 0.0
        else -> 138.5177312231 * Math.log(t - 10) - 305.0447927307
    }

    fun channel(value: Double) = value.coerceIn(1.0, 255.0) / 255.0
    val r = channel(red)
    val g = channel(green)
    val b = channel(blue)

    return RggbChannelVector(
        (g / r).toFloat(),
        1f,
        1f,
        (g / b).toFloat(),
    )
}
