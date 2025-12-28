package com.ishaan.kuluassignment.utils.platform

import kotlin.experimental.ExperimentalNativeApi

actual class PlatformSettings {
    actual val platform: Platform
        get() = Platform.IOS

    @OptIn(ExperimentalNativeApi::class)
    actual val isReleaseVariant: Boolean
        get() = !kotlin.native.Platform.isDebugBinary
}