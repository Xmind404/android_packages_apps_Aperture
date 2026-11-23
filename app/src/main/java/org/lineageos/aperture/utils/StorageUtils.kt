/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.aperture.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.video.MediaStoreOutputOptions
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

object StorageUtils {
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val STORAGE_DESTINATION = "${Environment.DIRECTORY_DCIM}/Camera"

    /**
     * Returns a new ImageCapture.OutputFileOptions to use to store a photo
     */
    fun getPhotoMediaStoreOutputOptions(
        contentResolver: ContentResolver,
        metadata: ImageCapture.Metadata,
        mimeType: String,
        timestamp: Long,
        outputStream: OutputStream? = null,
        volumeName: String = MediaStore.VOLUME_EXTERNAL,
    ): ImageCapture.OutputFileOptions {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, getTimeString(timestamp))
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, STORAGE_DESTINATION)
            }
            put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        }

        val outputFileOptions = outputStream?.let {
            ImageCapture.OutputFileOptions.Builder(it)
        } ?: ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.getContentUri(volumeName),
            contentValues
        )
        return outputFileOptions
            .setMetadata(metadata)
            .build()
    }

    /**
     * Returns a new OutputFileOptions to use to store a MP4 video
     */
    fun getVideoMediaStoreOutputOptions(
        contentResolver: ContentResolver,
        timestamp: Long,
        location: Location?,
        volumeName: String = MediaStore.VOLUME_EXTERNAL,
    ): MediaStoreOutputOptions {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, getTimeString(timestamp))
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, STORAGE_DESTINATION)
            }
        }

        return MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.getContentUri(volumeName))
            .setContentValues(contentValues)
            .setLocation(location)
            .build()
    }

    /**
     * Returns the MediaStore volume name of a mounted removable SD card, or null if there is none.
     */
    fun getSdCardVolumeName(context: Context): String? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return null
        }
        return context.getSystemService(StorageManager::class.java)?.storageVolumes?.firstOrNull {
            it.isRemovable && !it.isPrimary && it.state == Environment.MEDIA_MOUNTED
        }?.mediaStoreVolumeName
    }

    private fun getTimeString(timestamp: Long): String {
        return SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(timestamp)
    }
}
