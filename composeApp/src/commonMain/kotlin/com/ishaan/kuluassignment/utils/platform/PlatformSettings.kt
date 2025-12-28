package com.ishaan.kuluassignment.utils.platform

expect class PlatformSettings {
    val platform: Platform
    val isReleaseVariant: Boolean
}