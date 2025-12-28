package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single {
        DatabaseDriverFactory(androidContext())
    }
}