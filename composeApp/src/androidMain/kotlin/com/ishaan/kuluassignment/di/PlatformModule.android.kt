package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.db.DatabaseDriverFactory
import com.ishaan.kuluassignment.utils.platform.PlatformSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    singleOf(::PlatformSettings)
    single {
        DatabaseDriverFactory(androidContext())
    }
}