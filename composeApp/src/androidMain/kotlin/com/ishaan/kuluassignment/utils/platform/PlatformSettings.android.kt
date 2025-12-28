package com.ishaan.kuluassignment.utils.platform

import com.ishaan.kuluassignment.BuildConfig

actual class PlatformSettings {
    actual val platform: Platform
        get() = Platform.ANDROID
    actual val isReleaseVariant: Boolean
        get() = !BuildConfig.DEBUG
}